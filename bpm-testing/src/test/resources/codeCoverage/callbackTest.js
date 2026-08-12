function testCallback(testFunction){
    let outputVar = ['foo', 'bar', 'foobar'];
    testFunction(outputVar);
}

testCallback((outputVar) => {
    outputVar.forEach((x) => {
        console.log(x);
    }); 
});

var testArrow = () => {
    console.log('hello world in arrow')
}


var testObject = {
    name: 'testObject',
    testFunction : function(){
        let newVar = 100;
        console.log(newVar);
    }
}

testObject.testFunction();