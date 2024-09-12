@active
Feature: Client testing CRUD

  @smoke
  Scenario: Change the phone number of the first Client named Laura
    Given I send a POST request to create 9 random clients
    And I send a POST request to create a client with name Laura
    When I send a PUT request to update the client with ID "10"
    """
    {
      "name": "Laura",
      "lastName": "Munoz",
      "country": "Colombia",
      "city": "Medellin",
      "email": "laura@test.com",
      "phone": "87654321"
    }
    """
    Then the response should have a status code of 200
    And I send a DELETE request to delete clients with IDs from 1 to 10


    @smoke
    Scenario: Update and delete a New Client
      Given I send a POST request to create 1 random clients
      When I send a PUT request to update the client with ID "1"
      """
      {
        "name": "Maria",
        "lastName": "Munoz",
        "country": "Colombia",
        "city": "Medellin",
        "email": "laura@test.com",
        "phone": "300789654443"
      }
      """
      Then the response should have a status code of 200
      And validates the response with client JSON schema
      And I send a DELETE request to delete clients with IDs from 1 to 1
