Feature: UserController feature
  Background: create a user
    Given user details

  Scenario: verify if user resources can be added
    When creating a user
    Then user must be created

  Scenario: verify the user is able to create with no marks
    When creating a user with no marks
    Then user can be created with zero marks

  Scenario: verify the user is able to create with no name
    When creating a user with no name
    Then Name is required error message thrown

  Scenario: verify the user is able to create with no address
    When creating a user with no address
    Then Address is required error message thrown

  Scenario: verify if a user name can be updated
    When updating a user name
    Then user name is updated

  Scenario:Verify that the user is deleted
    When creating a user
    Then user got deleted

  Scenario: verify if a user address can be updated
    When updating a user address
    Then user address is updated

  Scenario: verify if a user marks can be updated
    When updating a user marks
    Then user marks is updated

  Scenario: verify the users list is displayed
    When creating a user
    Then user list displayed

  Scenario: verify that the particular user in the list is displayed
    When creating a user
    Then user with particular id must be displayed

  Scenario: verify that the invalid id user is displayed
    When creating a user
    Then Blank page must be displayed


















