Feature: the version can be retrieved
  Scenario: client makes call to GET /version
    When the client calls /version
    Then the client receives status code of 200
    And the client receives server version 1.0

  Scenario: client opens a webbrowser with the homepage
    When The client opens the browser on the homepage
    Then The version number is displayed on the page

