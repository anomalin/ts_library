package se.yrgo.integrations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import se.yrgo.integrations.pos.SearchPage;

public class AuthorSearchResultStepDefinitions {
    private SearchPage searchPage;

    @When("the user searches on an author with text {string}")
    public void the_user_searches_on_an_author_with_text(String input) {
        searchPage = new SearchPage(GeneralTestSetup.getDriver());
        searchPage.searchForAuthor(input);
    }

    @Then("the list should contain author {string}")
    public void the_list_should_contain_author(String resultText) {
        List<String> results = searchPage.getResultsAuthorSearch();        
        boolean contains = results.stream().map(String::toLowerCase).anyMatch(r -> r.contains(resultText.toLowerCase()));
        assertThat(contains).isTrue();
    }
}
