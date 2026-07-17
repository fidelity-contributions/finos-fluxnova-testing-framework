package codeCoverage

// AssertStatement
AssertStatement(15);

def AssertStatement(int input) {
  println(input)
  assert 4 * ( 2 + 3 ) - 5 == input : "Test Failed, Expected 15"
}

// SwitchStatement, CaseStatement and BreakStatement
for(int i in 1..8){
  SwitchAndBreakStatement(i);
}

def SwitchAndBreakStatement(int day) {
  switch(day) {
    case 1:
      println("Its Monday");
      break;
    case 2:
      println("Its Tuesday");
      break;
    case 3:
      println("Its Wednesday")
      break;
    case 4:
      println("Its Thursday");
      break;
    case 5:
      println("Its Friday");
      break;
    case 6:
      println("Its Saturday");
      break;
    case 7:
      println("Its Sunday");
      break;
    default:
      println("Not a valid day number");
      break;
  }
}

// TryCatchStatement and CatchStatement
TryCatchStatement();

def TryCatchStatement() {
  try {
    int[] arr = new int[3];
    def indexCounter = execution.getVariable('indexCounter')
    arr[indexCounter] = 5;
    println(arr)
  }catch(ArrayIndexOutOfBoundsException ex) {
    println(ex.toString());
    println(ex.getMessage());
    println(ex.getStackTrace());
  } catch(Exception ex) {
    println("Catching the exception");
  }finally {
    println("The final block");
  }

  println("Let's move on after the exception");
}

// ContinueStatement
ContinueStatement(5);

def ContinueStatement(int input) {
  int i = 0;
  while (i < 10) {
    if (i == input) {
      i++;
      continue;
    }
    println(i);
    i++;
  }
}

// EmptyStatement
EmptyStatement();

def EmptyStatement(){
  for(int i = 0; i < 10; i++);
}

// ExpressionStatement
ExpressionStatement();

def ExpressionStatement(){
  int x = 10;
  println("The value of x is "+x);
}

// IfStatement
IfStatement();

def IfStatement(){
  int x =10;

  if(x > 0) {
    println("Number is positive")
  } else if(x < 0){
    println("Number is negative")
  } else {
    println("Number is 0")
  }
}

// ReturnStatement
println(ReturnStatement(5,6));

def ReturnStatement(int x, int y){
  int a = x + y;
  return a;
}

// SynchronizedStatement
def synchronized increment(int count) {
  count++;
  return count;
}
println("Return Synchronized Count: "+ increment(1))


// ThrowStatement
try {
  ThrowStatement(17);
} catch(ArithmeticException exception){
  println("Inside Catch with ArithmeticException: "+exception)
}

def ThrowStatement(int age) throws ArithmeticException {
  if (age < 18) {
    throw new ArithmeticException("Access denied - Under 18's not allowed.");
  }
  else {
    println("Welcome to the show")
  }
}

//If statement without codeblock
def codeBlockCondition = execution.getVariable('codeBlockCondition')
if(codeBlockCondition)
  codeBlockCondition = false

if(codeBlockCondition)
  println('Code block condition :' + codeBlockCondition)
else
  println('Code block condition : ' + codeBlockCondition)

println('test')