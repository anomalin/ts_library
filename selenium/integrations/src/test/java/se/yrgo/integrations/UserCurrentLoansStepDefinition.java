package se.yrgo.integrations;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.assertj.core.api.Assertions.assertThat;

public class UserCurrentLoansStepDefinition {
    private HttpClient client;
    private HttpResponse<String> bookResp = null;

    @Given("a user is logged in")
    public void a_user_is_logged_in() throws IOException, InterruptedException {
        // we need a http client that can handle cookies since our
        // login information is handled using cookies
        CookieManager cookieHandler = new CookieManager();
        client = HttpClient.newBuilder().cookieHandler(cookieHandler).build();
        // login data as a json string
        String loginData = "{\"username\":\"test2\",\"password\":\"yrgoP4ssword\"}";

        HttpRequest loginReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8888/api/login"))
                .header("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(loginData)).build();
        HttpResponse<String> loginResp = client.send(loginReq,
                HttpResponse.BodyHandlers.ofString());
        if (loginResp.statusCode() != 200) {
            throw new IllegalStateException("Could not log in");
        }
    }

    @When("the user navigates to My Books")
    public void the_user_navigates_to_my_books() throws IOException, InterruptedException {
        HttpRequest lendReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8888/api/user/loans"))
                .build();
        bookResp = client.send(lendReq,
                HttpResponse.BodyHandlers.ofString());
        // here bookResp.body() is a JSON string of all the books the user has on loan
    }

    @Then("the users current loans are displayed")
    public void the_users_current_loans_should_be_displayed() {
        assertThat(bookResp.body()).contains("Astrid Lindgren");
    }
}
