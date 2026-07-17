// AssertStatement
AssertStatement(15);

function AssertStatement(input) {
  console.assert(4 * (2 + 3) - 5 == input, "Test Failed, Expected 15");
}

// SwitchStatement, CaseStatement and BreakStatement
let indexCounter = execution.getVariable('indexCounter')
for (let i = 1; i <= indexCounter; i++) {
  SwitchAndBreakStatement(i);
}

function SwitchAndBreakStatement(dayNumber) {
  let day;
  let defaultMsg;
  switch (dayNumber) {
    case 1:
      day="Its Monday";
      break;
    case 2:
      day="Its Tuesday";
      break;
    case 3:
      day="Its Wednesday"
      break;
    case 4:
      day="Its Thursday";
      break;
    case 5:
      day="Its Friday";
      break;
    case 6:
      day="Its Saturday";
      break;
    case 7:
      day="Its Sunday";
      break;
    default:
      defaultMsg="Not a valid day number";
      break;
  }
  console.log((dayNumber>=1 && dayNumber<=7) ? day : defaultMsg);
}

// TryCatchStatement and CatchStatement
TryCatchStatement();

function TryCatchStatement() {
  try {
    adddlert("Welcome guest!");
  } catch (err) {
    console.log(err.toString());
  } finally {
    console.log("The final block");
  }
  console.log("Let's move on after the exception");
}

// ContinueStatement
ContinueStatement(5);

function ContinueStatement(input) {
  let i = 0;
  while (i < 10) {
    if (i === input) {
      i++;
      continue;
    }
    console.log(i);
    i++;
  }
}

// EmptyStatement
EmptyStatement();

function EmptyStatement(){
  for (let i = 0; i < 10; i++);
}

// ExpressionStatement
ExpressionStatement();

function ExpressionStatement(){
  let x=5;
  console.log("The value of x is "+x);
}

// IfStatement
IfStatement();

function IfStatement() {
  let x=10;

  if (x > 0) {
    console.log("Number is positive")
  } else if (x < 0) {
    console.log("Number is negative");
  } else {
    console.log("Number is 0");
  }
}

OneLineIfStatement();
//
function OneLineIfStatement()  {
  let x = 10;
  if(x > 0) console.log("Number is positive"); else console.log("Number is negative");
}

try{
  OneLineIfStatementThrows();
} catch(e) {
  console.error(e.toString())
}
//
function OneLineIfStatementThrows()  {
  let x = 10;
  if(x > 0) throw new Error
}

OneLineIfStatementBreak()

function OneLineIfStatementBreak() {
  for(let i = 0; i < 10; i++) {
    if(i > 5) break
  }
}

// ReturnStatement
console.log(ReturnStatement(6,5));

function ReturnStatement(x,y) {
  let a = x + y;
  return a;
}

// SynchronizedStatement
DoSomethingSynchronized();

async function DoSomethingSynchronized() {
  console.log('Start DoSomethingSynchronized');
  await ResolveAfterSeconds();
  console.log('End DoSomethingSynchronized');
}

function ResolveAfterSeconds() {
  console.log('Start ResolveAfterSeconds');
  new Promise((resolve) => {
    setTimeout(() => {
      resolve('Waiting!!!');
    }, 2000);
  });
  console.log('End ResolveAfterSeconds');
}

// ThrowStatement
try {
  ThrowStatement(17);
} catch (e) {
  console.error(e.toString())
}

function ThrowStatement(age) {
  if (age < 18) {
    throw new Error("Access denied - Under 18's not allowed.");
  } else {
    console.log("Welcome to the show.");
  }
}