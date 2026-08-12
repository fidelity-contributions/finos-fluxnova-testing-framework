package codeCoverage

// DoWhileStatement
DoWhileStatement();

def DoWhileStatement(){
  def cars = ["BMW", "Mercedes Benz", "Volvo"]
  def x = 0
  do {
    println(cars[x])
    x++
  } while(cars.size() > x)
}

// ForStatement
ForStatement();

def ForStatement(){
  def cars = ["BMW", "Mercedez Benz", "Volvo", "Toyota", "Honda"]
  for(int i = 0; i < cars.size(); i++) {
    println("The car is: " +cars[i])
  }
}

// WhileStatement
WhileStatement();

def WhileStatement(){
  def cars = ["BMW", "Mercedez Benz", "Volvo", "Toyota", "Honda"]
  int i=execution.getVariable('indexCounter')
  while(i < cars.size()) {
    println("The car is: " +cars[i])
    i++
  }
}
