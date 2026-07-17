package org.finos.fluxnova.bpm.test.scripting.coverage

import org.finos.fluxnova.bpm.test.scripting.ScriptEngineType
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.ast.expr.ClosureExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.DeclarationExpression
import org.codehaus.groovy.control.CompilePhase
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.Source

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.text.SimpleDateFormat
import org.graalvm.polyglot.Value

class ScriptCoverageImpl {

    static int scriptCounter = 0
    static int fileCounter = 1

    def getInstrumentationObj(String scriptContent, ScriptEngineType scriptEngineType) {
        ArrayList<String> splitScript = scriptContent.split('\n')
        for (int i = 0; i < splitScript.size(); ++i) {
            splitScript[i] = splitScript[i].trim()
        }
        ArrayList<Map> linesToTrackArr
        if (ScriptEngineType.GROOVY == scriptEngineType) {
            linesToTrackArr = getASTGroovy(scriptContent)
            linesToTrackArr.each { lineObj ->
                {
                    injectInstrumentationGroovy(lineObj, splitScript)
                }
            }
        } else {
            linesToTrackArr = getASTJS(scriptContent)
            linesToTrackArr.each { lineObj ->
                {
                    injectInstrumentationJS(lineObj, splitScript)
                }
            }
        }
        def fullScript = String.join("\n", splitScript)
        scriptCounter = 0
        return ["linesToTrack": linesToTrackArr, "instrumentedScript": fullScript]
    }

    static List<Map<String, ?>> getASTGroovy(String scriptContent) {
        def astParentObj = new AstBuilder().buildFromString(CompilePhase.CLASS_GENERATION, false, scriptContent)
        ArrayList<String> linesToTrackArr = []
        if (astParentObj[0] instanceof BlockStatement) {
            runThroughBlockStatementsGroovy(astParentObj[0] as BlockStatement, linesToTrackArr)
        }
        def reservedNames = ['main', 'run']
        for (node in astParentObj[1].getMethods()) {
            def methodName = node.getName()
            def methodCode = node.getCode()
            if (!(methodName in reservedNames) && methodCode instanceof BlockStatement) {
                runThroughBlockStatementsGroovy(methodCode, linesToTrackArr)
            }
        }
        return linesToTrackArr.sort { it.lineNum }
    }

    static def getLineNumberGroovy(Statement statement) {
        return statement.getLineNumber()
    }

    static def getLastLineNumberGroovy(Statement statement) {
        return statement.getLastLineNumber()
    }

    static def handleExpressionGroovy(Statement statement, ArrayList<String> linesToTrackArr) {
        def expression = statement.getExpression()
        if (expression instanceof DeclarationExpression) {
            def rightSide = expression.getRightExpression()
            if (rightSide instanceof ClosureExpression) {
                handleClosures(rightSide, linesToTrackArr)
            } else if (rightSide instanceof MethodCallExpression) {
                handleMethodCalls(rightSide, linesToTrackArr)
            }
        }
        if (expression instanceof MethodCallExpression) {
            handleMethodCalls(expression, linesToTrackArr)
        }
    }

    static def handleMethodCalls(MethodCallExpression methExp, linesToTrackArr) {
        def args = methExp.getArguments()
        for (arg in args) {
            if (arg instanceof ClosureExpression) {
                handleClosures(arg, linesToTrackArr)
            }
        }
    }

    static def handleClosures(ClosureExpression closure, ArrayList<String> linesToTrackArr) {
        def codeBlock = closure.getCode()
        if (codeBlock instanceof BlockStatement) {
            runThroughBlockStatementsGroovy(codeBlock, linesToTrackArr)
        }
    }

    static def getExpStatementLineDetails(statement) {
        def lineType = (statement instanceof ReturnStatement || statement instanceof ThrowStatement) ? 'return' : 'expression'
        def lineNum = getLastLineNumberGroovy(statement)
        def reportline = getLineNumberGroovy(statement)
        def lineObj = ['lineNum': lineNum, 'lineType': lineType, 'lineString': reportline]
        return lineObj
    }

    static def handleIfStatementGroovy(IfStatement statement, ArrayList<String> linesToTrackArr) {
        def ifStatementBody = statement.getIfBlock()
        if (ifStatementBody instanceof BlockStatement) {
            runThroughBlockStatementsGroovy(ifStatementBody, linesToTrackArr)
        } else {

            def lineNum = getLineNumberGroovy(ifStatementBody)
            def lastLine = getLastLineNumberGroovy(ifStatementBody)
            def lineObj = ['lineNum': lineNum, 'lastLine': lastLine, 'lineType': 'singleStatementIf', 'lineString': lineNum]
            linesToTrackArr.add(lineObj)
        }
        def elseBl = statement.getElseBlock()
        if (elseBl != null && !(elseBl instanceof EmptyStatement)) {
            if (elseBl instanceof IfStatement) {
                handleIfStatementGroovy(elseBl, linesToTrackArr)
            } else if (elseBl instanceof BlockStatement) {
                runThroughBlockStatementsGroovy(elseBl, linesToTrackArr)
            } else {
                def lineNum = getLineNumberGroovy(elseBl)
                def lastLine = getLastLineNumberGroovy(elseBl)
                def lineObj = ['lineNum': lineNum, 'lastLine': lastLine, 'lineType': 'singleStatementIf', 'lineString': lineNum]
                linesToTrackArr.add(lineObj)
            }
        }
    }

    static def runThroughBlockStatementsGroovy(BlockStatement block, ArrayList<String> linesToTrackArr) {
        for (statement in block.getStatements()) {
            if (statement instanceof ExpressionStatement) {
                handleExpressionGroovy(statement, linesToTrackArr)
            }
            if (statement instanceof EmptyStatement || getLineNumberGroovy(statement) < 0) {
                continue
            }
            if (statement instanceof ExpressionStatement || statement instanceof AssertStatement || statement instanceof ReturnStatement || statement instanceof ThrowStatement) {
                def lineObj = getExpStatementLineDetails(statement)
                linesToTrackArr.add(lineObj)
            }
            if (statement instanceof IfStatement) {
                handleIfStatementGroovy(statement, linesToTrackArr)
            }
            if (statement instanceof WhileStatement || statement instanceof ForStatement || statement instanceof DoWhileStatement) {
                def loopBlock = statement.getLoopBlock()
                if (loopBlock instanceof EmptyStatement) {
                    continue
                }
                runThroughBlockStatementsGroovy(loopBlock, linesToTrackArr)
            }

            if (statement instanceof SwitchStatement) {
                def caseList = statement.getCaseStatements()
                for (caseObj in caseList) {
                    def lineNum = getLineNumberGroovy(caseObj)
                    def lineObj = ['lineNum': lineNum, 'lineType': 'case', 'lineString': lineNum]
                    linesToTrackArr.add(lineObj)
                }

                def defStatement = statement.getDefaultStatement()
                if (defStatement != null && !(defStatement instanceof EmptyStatement)) {
                    def lineNum = getLineNumberGroovy(defStatement)
                    def lineObj = ['lineNum': lineNum, 'lineType': 'case', 'lineString': lineNum]
                    linesToTrackArr.add(lineObj)
                }
            }
            if (statement instanceof TryCatchStatement) {
                def tryStatement = statement.getTryStatement()
                runThroughBlockStatementsGroovy(tryStatement, linesToTrackArr)
                def catchStatementList = statement.getCatchStatements()
                for (catchStatement in catchStatementList) {
                    runThroughBlockStatementsGroovy(catchStatement.getCode(), linesToTrackArr)
                }

                def finallyBlock = statement.getFinallyStatement()
                if (finallyBlock != null && !(finallyBlock instanceof EmptyStatement)) {
                    runThroughBlockStatementsGroovy(finallyBlock.getStatements()[0], linesToTrackArr)
                }
            }

            if (statement instanceof BlockStatement) {
                runThroughBlockStatementsGroovy(statement, linesToTrackArr)
            }
        }
    }

    List<Map<String, ?>> getASTJS(String script) {
        def resource = this.getClass().getClassLoader().getResource('node_modules')
        def path = (resource == null) ? "./.." : Paths.get(resource.toURI()).toString();

        Map<String, String> options = new HashMap<>();
        options.put("js.commonjs-require", "true");
        options.put("js.commonjs-require-cwd", path);
        options.put("js.esm-eval-returns-exports", "true")
        options.put("engine.WarnInterpreterOnly", "false")

        Context context = Context.newBuilder("js")
                .allowIO(true)
                .allowExperimentalOptions(true)
                .options(options)
                .build();

        def contextEvalString = """let acorn = require("acorn");
                        export function parseScript(scriptContent){
                            return acorn.parse(scriptContent,{locations:true, ecmaVersion:2022})
                        }
                    """

        Source source = Source.newBuilder("js", contextEvalString, 'test.js').mimeType("application/javascript+module").build()
        Value exports = context.eval(source)
        Value result = exports.invokeMember("parseScript", script)

        Value mainBody = result.getMember('body')
        ArrayList<String> linesToTrackArr = []
        runThroughJSMembers(mainBody, linesToTrackArr)
        return linesToTrackArr;
    }

    static def runThroughJSMembers(Value block, ArrayList<Map> linesToTrackArr) {
        if (block.hasArrayElements()) {
            for (int i = 0; i < block.getArraySize(); i++) {
                checkStatementType(block, linesToTrackArr, i)
            }
        } else {
            checkStatementType(block, linesToTrackArr, -1)
        }
    }

    static def parseJSObjectForFunctions(Value jsObjectVariable) {
        def objectProps = jsObjectVariable.getMember('properties');
        def propArr = []
        if (objectProps.getArraySize() > 0) {
            for (def i = 0; i < objectProps.getArraySize(); ++i) {
                def propValue = objectProps.getArrayElement(i).getMember(['value'])
                def valueType = propValue.getMember('type').toString()
                if (valueType == 'FunctionExpression' || valueType == 'ArrowFunctionExpression') {
                    propArr.add(propValue.getMember('body'))
                }
            }
        }
        return propArr
    }

    static def parseExpressionType(Value expStatement) {
        def nestedExpression = expStatement.getMember('expression')
        def expType = nestedExpression.getMember('type').toString()
        def funcBodyList = []
        if (expType == 'CallExpression') {
            //Parse out arguments
            def argumentList = nestedExpression.getMember('arguments')
            def arraySize = argumentList.getArraySize()
            //Loop through arguments to find arrow functions or anon functions
            for (def i = 0; i < arraySize; ++i) {
                def argument = argumentList.getArrayElement(i)
                def argumentType = argument.getMember('type').toString()
                if (argumentType == 'FunctionExpression' || argumentType == 'ArrowFunctionExpression') {
                    //parse out bodys from these and step through contents
                    def argFuncBody = argument.getMember('body')
                    funcBodyList.add(argFuncBody)
                }
            }
            return funcBodyList
        }
    }

    static def checkStatementType(Value block, ArrayList<Map> linesToTrackArr, int index) {
        def endStatements = ['ExpressionStatement', 'ReturnStatement', 'BreakStatement', 'ThrowStatement', 'ContinueStatement']
        def blockStatements = ['BlockStatement', 'FunctionDeclaration', 'WhileStatement', 'DoWhileStatement', 'ForStatement',
                               'ForInStatement', 'ForOfStatement']
        def statement = (index >= 0) ? block.getArrayElement(index) : block
        def statementType = statement.getMember('type').toString()

        if (statementType == 'ExpressionStatement') {
            def argumentBodyList = parseExpressionType(statement)
            def bodySize = argumentBodyList != null ? argumentBodyList.size() : 0
            for (def i = 0; i < bodySize; ++i) {
                runThroughJSMembers(argumentBodyList[i], linesToTrackArr)
            }
        }
        if (endStatements.contains(statementType)) {
            def lineNum = statement.getMember('loc').getMember('end').getMember('line')
            def lineObj = ['lineNum': lineNum, 'lineType': statementType, 'lineString': lineNum]
            linesToTrackArr.add(lineObj)
        }

        if (statementType == 'VariableDeclaration') {
            def varBody = statement.getMember('declarations').getArrayElement(0).getMember('init')
            if (!varBody.isNull() && varBody.getMember('type').toString() == 'ArrowFunctionExpression') {
                def arrowBody = varBody.getMember('body')
                runThroughJSMembers(arrowBody, linesToTrackArr)
            } else if (!varBody.isNull() && varBody.getMember('type').toString() == 'ObjectExpression') {
                def propFunctions = parseJSObjectForFunctions(varBody)
                for (propFunc in propFunctions) {
                    runThroughJSMembers(propFunc, linesToTrackArr)
                }
            }
            def lineNum = statement.getMember('loc').getMember('end').getMember('line')
            def lineObj = ['lineNum': lineNum, 'lineType': statementType, 'lineString': lineNum]
            linesToTrackArr.add(lineObj)
        }

        if (blockStatements.contains(statementType)) {
            def innerBody = statement.getMember('body')
            runThroughJSMembers(innerBody, linesToTrackArr)
        }

        if (statementType == 'TryStatement') {
            def tryBlockBody = statement.getMember('block')
            runThroughJSMembers(tryBlockBody, linesToTrackArr)
            def handlerBlock = statement.getMember('handler').getMember('body')
            runThroughJSMembers(handlerBlock, linesToTrackArr)
            def finalBlock = statement.getMember('finalizer')
            if (!finalBlock.isNull()) {
                runThroughJSMembers(finalBlock, linesToTrackArr)
            }
        }
        if (statementType == 'IfStatement') {
            def consequent = statement.getMember('consequent')
            def consequentType = consequent.getMember('type').toString()

            if (consequentType == 'ExpressionStatement' || consequentType == 'ReturnStatement') {
                runThroughJSMembers(consequent, linesToTrackArr)
            } else if (consequentType == 'BlockStatement') {
                def consequentBody = consequent.getMember('body')
                runThroughJSMembers(consequentBody, linesToTrackArr)
            }

            def alternate = statement.getMember('alternate')
            if (!alternate.isNull()) {
                def alternateType = alternate.getMember('type').toString()
                if (alternateType == 'BlockStatement') {
                    def alternateBody = alternate.getMember('body')
                    runThroughJSMembers(alternateBody, linesToTrackArr)
                } else {
                    runThroughJSMembers(alternate, linesToTrackArr)
                }
            }
        }

        if (statementType == 'SwitchStatement') {
            def cases = statement.getMember('cases')
            for (int i = 0; i < cases.arraySize; i++) {
                def innerBody = cases.getArrayElement(i).getMember('consequent')
                runThroughJSMembers(innerBody, linesToTrackArr)
            }
        }

    }

    static def injectInstrumentationGroovy(lineObj, sourceCodeArr) {
        switch (lineObj.lineType) {
            case 'expression': sourceCodeArr.add((lineObj.lineNum + scriptCounter), "loggingArray.add('${lineObj.lineNum}')"); ++scriptCounter; break;
            case 'return': sourceCodeArr.add((lineObj.lineNum + scriptCounter - 1), "loggingArray.add('${lineObj.lineNum}')"); ++scriptCounter; break;
            case 'case':
                if (sourceCodeArr[lineObj.lineNum + scriptCounter].contains('break')) {
                    sourceCodeArr[lineObj.lineNum + scriptCounter] = sourceCodeArr[lineObj.lineNum + scriptCounter].split('break').join(';') + ";loggingArray.add('${lineObj.lineNum}');break;"
                } else {
                    sourceCodeArr[lineObj.lineNum + scriptCounter] += ";loggingArray.add('${lineObj.lineNum}');"
                }
                break;
            case 'singleStatementIf':
                def singleLineIndex = lineObj.lineNum + scriptCounter - 1;
                sourceCodeArr[singleLineIndex] = parseSingleLineIf(sourceCodeArr[singleLineIndex], lineObj.lineNum);
                sourceCodeArr.add((lineObj.lineNum + scriptCounter), "loggingArray.add('${lineObj.lastLine}')}");
                ++scriptCounter;
                break;
            default: println('No type'); break;
        }
    }


    static def parseSingleLineIf(ifStatementLine, lineNum) {
        /*this is just to handle the edge case that someone writes a single line if or else
          in a weird way like
            if(true) def foo = "foo"
            else def bar = "bar"
        */
        if (ifStatementLine.trim().contains('if(')) {
            return ifStatementLine.replaceFirst('[)]', '){')
        } else if (ifStatementLine.contains('else')) {
            return ifStatementLine.replaceFirst('[ ]', ' {')
        } else {
            return '{' + ifStatementLine
        }
    }

    static def injectInstrumentationJS(lineObj, sourceCodeArr) {
        def lineNum = lineObj.lineNum.asInt();
        switch (lineObj.lineType) {
            case 'ExpressionStatement':
            case 'VariableDeclaration':
                sourceCodeArr.add((lineNum + scriptCounter), "loggingArray.add('${lineObj.lineString}')");
                ++scriptCounter;
                break;
            case 'ReturnStatement':
            case 'BreakStatement':
            case 'ContinueStatement':
            case 'ThrowStatement':
                sourceCodeArr.add((lineNum + scriptCounter - 1), "loggingArray.add('${lineObj.lineString}')");
                ++scriptCounter;
                break;
            default: println('No type');
                break;
        }
    }

    def static generateCoverageReport(linesToTrack, linesTracked, scriptName) {
        def resource = this.getClassLoader().getResource('template.html')
        String htmlContents = resource.text;
        def tableContents = ""
        for (line in linesToTrack) {
            def isCovered = linesTracked.contains(line.lineNum.toString())
            def className = isCovered ? 'covered' : 'not-covered'
            tableContents += "<tr class='${className}'><td>${line.lineNum}</td><td>${isCovered}</td></tr>\n"
        }
        htmlContents = htmlContents.replace('{coverageReport}', tableContents)
        def timeAndDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())
        htmlContents = htmlContents.replace('{date}', timeAndDate)
        int percent = (linesTracked.size() / linesToTrack.size()) * 100
        def percentClass = ""
        if (percent > 60 && percent < 80) {
            percentClass = 'partial'
        } else if (percent >= 80) {
            percentClass = 'covered'
        } else {
            percentClass = 'not-covered'
        }
        def percentString = "<span class=${percentClass}>${percent.toString()}%</span>"
        htmlContents = htmlContents.replace('{coveragePercent}', percentString)
        Path reportPath = Paths.get("./target/coverage-collection/code-coverage/report_${scriptName}.html")
        if (Files.exists(reportPath)) {
            Path incrementedReportPath = Paths.get("./target/coverage-collection/code-coverage/report_${scriptName}_${fileCounter}.html")
            Files.writeString(incrementedReportPath, htmlContents)
            ++fileCounter
        } else {
            Files.writeString(reportPath, htmlContents)
            fileCounter = 1
        }
        return percent
    }
}