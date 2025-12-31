package se.yrgo.integrations;

import io.cucumber.java.en.Given;
import se.yrgo.integrations.pos.SearchPage;

public class GeneralStepDefinitions {
    private SearchPage searchPage;

    @Given("the user is on the start page")
    public void the_user_is_on_the_start_page() {
        GeneralTestSetup.getDriver().get("http://frontend");
        if (!"The Library".equals(GeneralTestSetup.getDriver().getTitle())) {
            throw new IllegalStateException("Not on the start page");
        }
    }

    @Given("the user is on the search page")
    public void the_user_is_on_the_search_page() {
        GeneralTestSetup.getDriver().get("http://frontend/search");
        searchPage = new SearchPage(GeneralTestSetup.getDriver());
    }
}
