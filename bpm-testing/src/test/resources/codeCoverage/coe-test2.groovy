

import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;
import static org.finos.fluxnova.spin.Spin.JSON;

def refDocumentsPublishedEventEndpoint = "";
def refDocumentsPublishedEventPayload = "";

def publishDocumentsRequest = execution.getVariable("publishDocumentsRequest");
def connectResponse = execution.getVariable("connectResponse");

def jsonSlurper = new JsonSlurper();
def connResponse = jsonSlurper.parseText(connectResponse);

if (connResponse.workitemNumber != null) {
    execution.setVariable("workItemNumber", connResponse.workitemNumber);
}
def configMap = execution.getVariable('contextMap')
def refDocumentsPublishedEventHost = configMap.get("cardinal.beneHub.api.host");
refDocumentsPublishedEventEndpoint = refDocumentsPublishedEventHost + "/api/bh/v1/business-events";

def builder = new groovy.json.JsonBuilder();

builder {
    lifeEvent "CARDINAL"
    eventName "REF_DOCUMENTS.PUBLISHED"
    payload  {
        userID   publishDocumentsRequest.userID
        accountOwnerID publishDocumentsRequest.accountOwnerID
        role publishDocumentsRequest.role
        userFirstName publishDocumentsRequest.userFirstName
        userLastName publishDocumentsRequest.userLastName
        userEmailAddress publishDocumentsRequest.userEmailAddress
        relationSSN publishDocumentsRequest.relationSSN
        documentsUploaded  publishDocumentsRequest.documentsUploaded
        publishTo  publishDocumentsRequest.publishTo
        transactionTrackingID publishDocumentsRequest.transactionTrackingID

    }
}
refDocumentsPublishedEventPayload = JSON(builder.toString());

execution.setVariable("refDocumentsPublishedEventEndpoint", refDocumentsPublishedEventEndpoint);
execution.setVariable("refDocumentsPublishedEventPayload", refDocumentsPublishedEventPayload);
execution.setVariable("eventName", "refDocumentsPublishedEvent")