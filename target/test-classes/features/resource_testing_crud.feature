@active
Feature: Resource testing CRUD

  @smoke
  Scenario:  Get the list of active resources
    Given there is at least 5 active resources
    Then I send a PUT request to update all active resources
    And I send a DELETE request to delete resources with IDs from 1 to 5

  @smoke
  Scenario: Update the last created resource
    Given there is at least 15 active resources
    When I send a PUT request to update the last resource
    """
    {
      "name": "Awesome Tuna",
      "trademark": "M",
      "stock": 1234,
      "price": 5.6,
      "description": "Nulla non eros euismod, eleifend nibh a, efficitur arcu. Nam dapibus erat a turpis blandit tincidunt. In est massa, tristique in magna quis, dignissim ornare nisi",
      "tags": "food",
      "active": false,
      "id":%s
    }
    """
    Then the resource response should have a status code of 200
    And validates the response with resource JSON schema
    And I send a DELETE request to delete resources with IDs from 1 to 15
