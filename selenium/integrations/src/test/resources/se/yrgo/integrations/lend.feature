Feature: Lending books
    As a user or admin I want to be able to lend a book and see my loans

Scenario: Lending a book to a user
    Given an administrator is logged in
    And the book with id 5 is available
    When the administrator lends book with id "5" to a user with id "2"
    Then the book should be noted as lent out to the user

Scenario: A user can se their loans
    Given a user is logged in
    When the user navigates to My Books 
    Then the users current loans are displayed