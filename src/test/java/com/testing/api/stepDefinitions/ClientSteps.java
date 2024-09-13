package com.testing.api.stepDefinitions;

import com.testing.api.models.Client;
import com.testing.api.requests.ClientRequest;
import com.testing.api.utils.ClientUtil;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class ClientSteps {
    private static final Logger logger = LogManager.getLogger(ClientSteps.class);

    private final ClientRequest clientRequest = new ClientRequest();

    private Response response;
    private Client   client;

    @Given("there are registered clients in the system")
    public void thereAreRegisteredClientsInTheSystem() {
        response = clientRequest.getClients();
        logger.info(response.jsonPath()
                            .prettify());
        Assert.assertEquals(200, response.statusCode());

        List<Client> clientList = clientRequest.getClientsEntity(response);
        if (clientList.isEmpty()) {
            response = clientRequest.createDefaultClient();
            logger.info(response.statusCode());
            Assert.assertEquals(201, response.statusCode());
        }
    }

    @Given("I have a client with the following details:")
    public void iHaveAClientWithTheFollowingDetails(DataTable clientData) {
        Map<String, String> clientDataMap = clientData.asMaps()
                                                      .get(0);
        client = Client.builder()
                       .name(clientDataMap.get("Name"))
                       .lastName(clientDataMap.get("LastName"))
                       .country(clientDataMap.get("Country"))
                       .city(clientDataMap.get("City"))
                       .email(clientDataMap.get("Email"))
                       .phone(clientDataMap.get("Phone"))
                       .build();
        logger.info("Client mapped: {}", client);
    }

    @Given("I send a POST request to create {int} random clients")
    public void iSendAPOSTRequestToCreateRandomClients(int numberClients) {
        for (int i = 0; i < numberClients; i++) {
            Client randomClient = ClientUtil.createRandomClient();
            Response response = clientRequest.createClient(randomClient);
            logger.info(response.statusCode());
            Assert.assertEquals(201, response.statusCode());
        }
    }

    @Given("I send a POST request to create a client with name Laura")
    public void iSendAPOSTRequestToCreateAClientWithNameLaura() {
        response = clientRequest.createDefaultClient();
        logger.info(response.statusCode());
        Assert.assertEquals(201, response.statusCode());
    }

    @When("I retrieve the details of the client with ID {string}")
    public void sendGETRequest(String clientId) {
        response = clientRequest.getClient(clientId);
        logger.info(response.jsonPath()
                            .prettify());
        logger.info("The status code is: {}", response.statusCode());
    }

    @When("I send a GET request to view all the clients")
    public void iSendAGETRequestToViewAllTheClient() {
        response = clientRequest.getClients();
    }

    @When("I send a POST request to create a client")
    public void iSendAPOSTRequestToCreateAClient() {
        response = clientRequest.createClient(client);
    }

    @When("I send a DELETE request to delete the client with ID {string}")
    public void iSendADELETERequestToDeleteTheClientWithID(String clientId) {
        response = clientRequest.deleteClient(clientId);
    }

    @When("I send a DELETE request to delete clients with IDs from {int} to {int}")
    public void iSendADELETERequestToDeleteClientsWithIDsFromTo(int startId, int endId) {
        for (int clientId = startId; clientId <= endId; clientId++) {
            response = clientRequest.deleteClient(String.valueOf(clientId));
            logger.info("Deleted client with ID " + clientId + ": " + response.getStatusCode());
            Assert.assertEquals(200, response.statusCode());
        }
    }

    @When("I send a PUT request to update the client with ID {string}")
    public void iSendAPUTRequestToUpdateTheClientWithID(String clientId, String requestBody) {
        client = clientRequest.getClientEntity(requestBody);
        response = clientRequest.updateClient(client, clientId);
        Client posclient = clientRequest.getClientEntity(response);
        Assert.assertEquals(posclient.getName(), client.getName());
        Assert.assertEquals(posclient.getLastName(), client.getLastName());
        Assert.assertEquals(posclient.getCountry(), client.getCountry());
        Assert.assertEquals(posclient.getCity(), client.getCity());
        Assert.assertEquals(posclient.getEmail(), client.getEmail());
        Assert.assertEquals(posclient.getPhone(), client.getPhone());
    }

    @When("I send a PUT request to update the client")
    public void iSendAPUTRequestToUpdateTheClientWithName(String requestBody) {
        client = clientRequest.getClientEntity(requestBody);
        response = clientRequest.updateClient(client, client.getId());
        logger.info(response.statusCode());
        Assert.assertEquals(200, response.statusCode());
    }


    @When("I send a GET request to view client with ID {string}")
    public void iSendAGETRequestToViewClientWithID(String clientId) {
        response = clientRequest.getClient(clientId);
        logger.info("GET request sent for client with ID " + clientId + ": " + response.getStatusCode());
    }


    @Then("the response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
    }

    @Then("the response should have the following details:")
    public void theResponseShouldHaveTheFollowingDetails(DataTable expectedData) {
        client = clientRequest.getClientEntity(response);
        Map<String, String> expectedDataMap = expectedData.asMaps()
                                                          .get(0);

        Assert.assertEquals(expectedDataMap.get("Name"), client.getName());
        Assert.assertEquals(expectedDataMap.get("LastName"), client.getLastName());
        Assert.assertEquals(expectedDataMap.get("Country"), client.getCountry());
        Assert.assertEquals(expectedDataMap.get("City"), client.getCity());
        Assert.assertEquals(expectedDataMap.get("Email"), client.getEmail());
        Assert.assertEquals(expectedDataMap.get("Phone"), client.getPhone());
        Assert.assertEquals(expectedDataMap.get("Id"), client.getId());
    }

    @Then("the response should include the details of the created client")
    public void theResponseShouldIncludeTheDetailsOfTheCreatedClient() {
        Client new_client = clientRequest.getClientEntity(response);
        new_client.setId(null);
        Assert.assertEquals(client, new_client);
    }

    @Then("validates the response with client JSON schema")
    public void userValidatesResponseWithClientJSONSchema() {
        String path = "schemas/clientSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client object");
    }

    @Then("validates the response with client list JSON schema")
    public void userValidatesResponseWithClientListJSONSchema() {
        String path = "schemas/clientListSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client List object");
    }

}
