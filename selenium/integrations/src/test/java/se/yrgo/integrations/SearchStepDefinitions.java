package se.yrgo.integrations;

import static org.assertj.core.api.Assertions.assertThat;
import io.cucumber.java.en.*;
import se.yrgo.integrations.pos.StartPage;

public class SearchStepDefinitions {
    private StartPage startPage;
    
    @When("the user navigates to the book search")
    public void the_user_navigates_to_the_book_search() {
        startPage = new StartPage(GeneralTestSetup.getDriver());
        startPage.navigateToSearch();

    }

    @Then("they can see the search form")
    public void they_can_see_the_search_form() {
        boolean result = startPage.isSearchFormVisible();
        assertThat(result).isTrue();
    }
}
