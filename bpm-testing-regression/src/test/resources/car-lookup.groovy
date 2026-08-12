import org.finos.fluxnova.bpm.engine.delegate.BpmnError

def isVintage = execution.getVariable("isVintage");
def carType = isVintage ? "vintage" : "regular";
System.out.println("Order for " + carType + " car");

def car = execution.getVariable("carMakeModel");
System.out.println("Car model:" + car);

if (car == null || car.equals("")) {
    throw new BpmnError("501", "Unable to retrieve car make/model from API call!");
}
