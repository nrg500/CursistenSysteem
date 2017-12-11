Feature: a textfile with course instances can be imported
  Scenario: client uploads a correct textfile through the browser
    Given the client is on the import file page
    When the client selects a correct file
    And the client clicks submit
    Then the client will receive a message that the import succeeded
  Scenario: client uploads an incorrect textfile through the browser
    Given the client is on the import file page
    When the client selects an incorrect file
    And the client clicks submit
    Then the client will receive a message that specifies which line number went wrong