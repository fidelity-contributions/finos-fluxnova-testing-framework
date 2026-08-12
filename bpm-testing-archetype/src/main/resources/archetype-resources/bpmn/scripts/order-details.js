var menuDetails = {}

var firstName = execution.getVariable("firstName");
var menu = execution.getVariable("menu");

if (firstName === "John") {
    menuDetails['name'] = "John Doe";
    menuDetails['address'] = "123 Main St, Springfield";
} else if (firstName === "Sam") {
    menuDetails['name'] = "Sam Doe";
    menuDetails['address'] = "456 Elm St, Shelbyville";
}

menuDetails['restaurant'] = execution.getVariable("restaurant");
menuDetails['website'] = execution.getVariable("websiteUrl");
menuDetails.menu = menu;

execution.setVariable("orderDetails", S(JSON.stringify(menuDetails)));