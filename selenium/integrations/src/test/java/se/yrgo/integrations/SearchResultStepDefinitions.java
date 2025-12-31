package se.yrgo.integrations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import se.yrgo.integrations.pos.SearchPage;

public class SearchResultStepDefinitions {

    private SearchPage searchPage;

    @When("the user searches on a title with text {string}")
    public void the_user_searches_on_a_title_with_text(String input) {
        searchPage = new SearchPage(GeneralTestSetup.getDriver());
        searchPage.searchForTitle(input);
    }


    @Then("the list should contain {string}")
    public void the_list_should_contain(String resultText) {
        List<String> results = searchPage.getResultsTitleSearch();
        boolean contains = results.stream().map(String::toLowerCase).anyMatch(r -> r.contains(resultText.toLowerCase()));
        assertThat(contains).isTrue();
    }
}
