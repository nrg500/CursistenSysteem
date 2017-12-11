Feature: I can view a list of course instances
  Scenario: client views the list of course instances
    Given the database contains three course instances
    Given the client has an open browser window
    When the client navigates to the list course instances page
    Then the client sees all the course instances
