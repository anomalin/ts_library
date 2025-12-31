package se.yrgo.integrations;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.cienvironment.internal.com.eclipsesource.json.JsonObject;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.util.JSONPObject;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AdminLendStepDefinitions {
    private HttpClient client;
    private HttpResponse<String> lendResponse = null;

    @Given("an administrator is logged in")
    public void an_administrator_is_logged_in() throws IOException, InterruptedException {
        // we need a http client that can handle cookies since our
        // login information is handled using cookies
        CookieManager cookieHandler = new CookieManager();
        client = HttpClient.newBuilder().cookieHandler(cookieHandler).build();
        // login data as a json string
        String loginData = "{\"username\":\"test\",\"password\":\"yrgoP4ssword\"}";

        HttpRequest loginReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8888/api/login"))
                .header("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(loginData)).build();
        HttpResponse<String> loginResp;

        loginResp = client.send(loginReq,
                HttpResponse.BodyHandlers.ofString());

        if (loginResp.statusCode() != 200) {
            throw new IllegalStateException("Could not log in");
        }
    }

    @Given("the book with id {int} is available")
    public void the_book_with_id_is_available(Integer bookId) throws IOException, InterruptedException {
        String returnData = Integer.toString(bookId);
        HttpRequest returnReq = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8888/api/admin/loan/return"))
            .header("Content-type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(returnData))
            .build();
        
        HttpResponse<String> returnResp = client.send(returnReq, HttpResponse.BodyHandlers.ofString());
        if (returnResp.statusCode() != 200) {
            throw new IllegalStateException("Could not reset book state: " + returnResp.body());
        }
    }

    @When("the administrator lends book with id {string} to a user with id {string}")
    public void the_administrator_lends_book_with_id_to_a_user_with_id(String bookId, String userId) throws IOException, InterruptedException {
        String lendData = """
                 {"book": %s, "user": %s}
                """.formatted(bookId, userId);

        HttpRequest lendReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8888/api/admin/loan/lend"))
                .header("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(lendData))
                .build();
        
       
        lendResponse = client.send(lendReq, HttpResponse.BodyHandlers.ofString());
    }

    @Then("the book should be noted as lent out to the user")
    public void the_book_should_be_noted_as_lent_out_to_the_user() {

        assertThat(lendResponse.statusCode()).isEqualTo(200);
        assertThat(lendResponse.body()).contains("true");
    }
}
