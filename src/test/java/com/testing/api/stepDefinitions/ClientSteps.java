package com.testing.api.stepDefinitions;

import com.testing.api.models.Client;
import com.testing.api.requests.ClientRequest;
import com.testing.api.utils.ClientUtil;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

/**
 * The type Client steps.
 */
public class ClientSteps {
    private static final Logger logger = LogManager.getLogger(ClientSteps.class);

    private final ClientRequest clientRequest = new ClientRequest();

    private Response response;
    private Client   client;

    /**
     * Send a post request to create random clients.
     *
     * @param numberClients the number clients
     */
    @Given("I send a POST request to create {int} random clients")
    public void iSendAPOSTRequestToCreateRandomClients(int numberClients) {
        for (int i = 0; i < numberClients; i++) {
            Client randomClient = ClientUtil.createRandomClient();
            Response response = clientRequest.createClient(randomClient);
            logger.info(response.statusCode());
            Assert.assertEquals(201, response.statusCode());
        }
    }

    /**
     * Send a post request to create a client with name laura.
     */
    @Given("I send a POST request to create a client with name Laura")
    public void iSendAPOSTRequestToCreateAClientWithNameLaura() {
        response = clientRequest.createDefaultClient();
        logger.info(response.statusCode());
        Assert.assertEquals(201, response.statusCode());
    }


    /**
     * Send a delete request to delete the client with id {int}.
     *
     * @param clientId the client id
     */
    @When("I send a DELETE request to delete the client with ID {string}")
    public void iSendADELETERequestToDeleteTheClientWithID(String clientId) {
        response = clientRequest.deleteClient(clientId);
    }

    /**
     * Send a delete request to delete clients with IDs from {int} to {int}
     *
     * @param startId the start id
     * @param endId   the end id
     */
    @When("I send a DELETE request to delete clients with IDs from {int} to {int}")
    public void iSendADELETERequestToDeleteClientsWithIDsFromTo(int startId, int endId) {
        for (int clientId = startId; clientId <= endId; clientId++) {
            response = clientRequest.deleteClient(String.valueOf(clientId));
            logger.info("Deleted client with ID " + clientId + ": " + response.getStatusCode());
            Assert.assertEquals(200, response.statusCode());
        }
    }

    /**
     * Send a put request to update the client with id.
     *
     * @param clientId    the client id
     * @param requestBody the request body
     */
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


    /**
     * The response should have a status code of {int}.
     *
     * @param statusCode the status code
     */
    @Then("the response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
    }


    /**
     * User validates response with client json schema.
     */
    @Then("validates the response with client JSON schema")
    public void userValidatesResponseWithClientJSONSchema() {
        String path = "schemas/clientSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client object");
    }

    /**
     * User validates response with client list json schema.
     */
    @Then("validates the response with client list JSON schema")
    public void userValidatesResponseWithClientListJSONSchema() {
        String path = "schemas/clientListSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client List object");
    }

}
