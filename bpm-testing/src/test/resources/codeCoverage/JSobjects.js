let jsObject = {
    name: 'jsobject',
    status: 'object',
    testFunc: function(){
        let newVar = 100;
        console.log(`Number is ${newVar}`);
    },
    testFunc2: () =>{
        let arrowVar = 'JS Objact test function';
        console.log(arrowVar);
    }
}

jsObject.name = 'Test Objecy';

jsObject.status = 'in use';

jsObject.testFunc();

jsObject.testFunc2();