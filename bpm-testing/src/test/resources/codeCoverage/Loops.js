// DoWhileStatement
DoWhileStatement();

function DoWhileStatement() {
  let cars = ["BMW", "Mercedes Benz", "Volvo"]
  let x = 0;
  do {
    console.log(cars[x]);
    x++;
  } while (x < cars.length)
}

// ForStatement
ForStatement();

function ForStatement() {
  let cars = ["BMW", "Mercedez Benz", "Volvo", "Toyota", "Honda"]
  for(let i = 0; i < cars.length; i++) {
    console.log("The car is: " +cars[i])
  }
}

// ForInStatement
ForInStatement();

function ForInStatement(){
  let cars = ["BMW", "Mercedez Benz", "Volvo", "Toyota", "Honda"]
  for(let x in cars) {
    console.log("The car is: " +cars[x])
  }
}

// ForOfStatement
ForOfStatement();

function ForOfStatement(){
  let cars = ["BMW", "Mercedez Benz", "Volvo", "Toyota", "Honda"]
  for(let car of cars) {
    console.log("The car is: " +car)
  }
}

// WhileStatement
WhileStatement();

function WhileStatement(){
  let cars = ["BMW", "Mercedez Benz", "Volvo", "Toyota", "Honda"];
  let i = execution.getVariable('indexCounter')
  while(i < cars.length) {
    console.log("The car is: " + cars[i])
    i++
  }
}