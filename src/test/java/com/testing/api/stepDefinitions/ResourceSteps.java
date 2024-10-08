package com.testing.api.stepDefinitions;

import com.testing.api.models.Resource;
import com.testing.api.requests.ResourceRequest;
import com.testing.api.utils.ResourceUtil;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;

/**
 * The type Resource steps.
 */
public class ResourceSteps {
    private static final Logger logger = LogManager.getLogger(ResourceSteps.class);

    private final ResourceRequest resourceRequest = new ResourceRequest();

    private Response response;
    private Resource resource;

    /**
     * There is at least 10 active resources.
     *
     * @param num the num
     */
    @When("there is at least {int} active resources")
    public void thereIsAtLeastActiveResources(int num) {
        response = resourceRequest.getResources();
        List<Resource> resources = resourceRequest.getResourcesEntity(response);
        resources.forEach(logger::info);

        long activeResourceCount = resources.stream()
                .filter(Resource::isActive)
                .count();
        if (activeResourceCount < num) {
            for (int i = 0; i < (num - activeResourceCount); i++) {
                Resource randomResource = ResourceUtil.createRandomResource();
                Response response = resourceRequest.createResource(randomResource);
                logger.info(response.statusCode());
                Assert.assertEquals(201, response.statusCode());
            }
        }
    }

    /**
     * Send a put request to update the last resource.
     *
     * @param requestBody the request body
     */
    @When("I send a PUT request to update the last resource")
    public void iSendAPUTRequestToUpdateTheLastResource(String requestBody) {
        response = resourceRequest.getResources();
        List<Resource> resources = resourceRequest.getResourcesEntity(response);
        int activeResourceCount = (int) resources.stream()
                .filter(Resource::isActive)
                .count();

        String id = resources.get(resources.size() - 1).getId();
        logger.info("Last resource ID: {}", id);

        requestBody = String.format(requestBody, id);
        logger.info("Request Body: {}", requestBody);

        resource = resourceRequest.getResourceEntity(requestBody);
        response = resourceRequest.updateResource(resource, id);


    }

    /**
     * Send a delete request to delete resources with IDs from {int} to {int}.
     *
     * @param startId the start id
     * @param endId   the end id
     */
    @Then("I send a DELETE request to delete resources with IDs from {int} to {int}")
    public void iSendADELETERequestToDeleteResourcesWithIDsFromIntToInt(int startId, int endId) {
        for (int resourceId = startId; resourceId <= endId; resourceId++) {
            response = resourceRequest.deleteResource(String.valueOf(resourceId));
            logger.info("Deleted resource with ID " + resourceId + ": " + response.getStatusCode());
            Assert.assertEquals(200, response.statusCode());
        }
    }

    /**
     * Send a put request to update all active resources.
     */
    @Then("I send a PUT request to update all active resources")
    public void iSendAPUTRequestToUpdateAllActiveResources() {
        response = resourceRequest.getResources();
        List<Resource> resources = resourceRequest.getResourcesEntity(response);

        List<Resource> activeResources = resources.stream()
                .filter(Resource::isActive)
                .toList();

        for (Resource resource : activeResources) {
            resource.setActive(false);
            Response updateResponse = resourceRequest.updateResource(resource, resource.getId());

            logger.info("Updated resource: {}", resource.getId());

            Assert.assertEquals(200, updateResponse.statusCode());

            String path = "schemas/resourceSchema.json";
            Assert.assertTrue(resourceRequest.validateSchema(updateResponse, path));
            logger.info("Successfully Validated schema from Resource object");
        }
    }

    /**
     * The response should have a status code of {int}.
     *
     * @param statusCode the status code
     */
    @Then("the resource response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
    }

    /**
     * Validates the response with resource json schema.
     */
    @Then("validates the response with resource JSON schema")
    public void validatesTheResponseWithResourceJSONSchema() {
        String path = "schemas/resourceSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Resource object");
    }
}
