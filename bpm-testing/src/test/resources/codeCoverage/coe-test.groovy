

import java.text.SimpleDateFormat
import groovy.json.JsonSlurper

def date = new Date()
def sdf2 = new SimpleDateFormat("yyyy-MM-dd")
def nbDays = execution.getVariable('nbDays')
sdf2.setTimeZone(TimeZone.getTimeZone("UTC"))
def today = sdf2.format(date);
String weekend = "yes";
def sdf3 = new SimpleDateFormat("EEEE")
sdf3.setTimeZone(TimeZone.getTimeZone("UTC"))
day = sdf3.format(date);

if (day!="Saturday" && day!="Sunday")
{
    weekend = "no";
}


def jsonSlurperTest = new JsonSlurper()
def object1 = jsonSlurperTest.parseText(nbDays);
def matchedPattern = object1.find{k-> k.date == today}
if(matchedPattern == null && weekend =="no")
{
    execution.setVariable ("isBusinessDate", "yes")
}
else
{
    execution.setVariable ("isBusinessDate", "no")
}