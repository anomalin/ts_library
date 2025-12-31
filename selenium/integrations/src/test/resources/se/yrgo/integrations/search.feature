Feature: Searching for books
    As a user I want to be able to search for available books so I know what I can
    loan

Scenario: Getting to the search page
    Given the user is on the start page
    When the user navigates to the book search
    Then they can see the search form

Scenario Outline: Searching for book title 
    Given the user is on the search page
    When the user searches on a title with text "<title>"
    Then the list should contain "<title>"

    Examples:
        | title                         |
        | Sinful Revenge                |
        | Här kommer Pippi Långstrump   |
        | T-SQL Fundamentals            |

 
Scenario: Searching for an author
    Given the user is on the search page
    When the user searches on an author with text "Dean Koontz"
    Then the list should contain author "Dean Koontz"      