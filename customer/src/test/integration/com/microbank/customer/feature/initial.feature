@Initial
Feature: Tests basic customer information flow from account creation to deletion.

  Scenario: A user registers for an account.
    Given a user's registration information
    When they hit the customer registration endpoint
    Then they receive their customer information back
    And a status code of 201 is received

  Scenario: A user requests their information.
    Given a username for a registered user
    When a user hits the customer information endpoint
    Then they receive their customer information back
    And a status code of 200 is received

  Scenario: