import groovy.json.JsonBuilder;

def listener = { e -> 
    println("clicked on $e")

    println('second print')
}

listener('Test closure')

def testMethod(closureMethod){
    closureMethod(55)
}

testMethod({e ->  println("incoming variable is $e")})

def name = 'TestVar'

def nameMap = (0..6).collectEntries { index -> 
    [index, name[index]]
}

def builder = new groovy.json.JsonBuilder();


builder {
    jsonVar1 "CARDINAL"
    jsonVar2 "REF_DOCUMENTS.PUBLISHED"
}
println('ending')