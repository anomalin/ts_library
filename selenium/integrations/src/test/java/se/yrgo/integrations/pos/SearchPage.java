package se.yrgo.integrations.pos;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import se.yrgo.integrations.GeneralTestSetup;

public class SearchPage {
    WebDriver driver;

    private By titleSearchInput = By.cssSelector("input[placeholder='Title']");
    private By authorSearchInput = By.cssSelector("input[placeholder='Author']");
    private By button = By.cssSelector("input.btn.btn-primary");
    private By table = By.tagName("table");

    public SearchPage(WebDriver driver) {
        this.driver = driver;
    }

    public void searchForTitle(String input) {
        WebDriverWait wait = new WebDriverWait(GeneralTestSetup.getDriver(), Duration.ofSeconds(10));
        WebElement textElem = wait.until(ExpectedConditions.presenceOfElementLocated(titleSearchInput));
        textElem.sendKeys(input);
        WebElement buttonElem = wait.until(ExpectedConditions.presenceOfElementLocated(button));
        buttonElem.click();
    }

      public void searchForAuthor(String input) {
        WebDriverWait wait = new WebDriverWait(GeneralTestSetup.getDriver(), Duration.ofSeconds(10));
        WebElement textElem = wait.until(ExpectedConditions.presenceOfElementLocated(authorSearchInput));
        textElem.sendKeys(input);
        WebElement buttonElem = wait.until(ExpectedConditions.presenceOfElementLocated(button));
        buttonElem.click();
    }

    public List<String> getResultsTitleSearch() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement tableElem = wait.until(ExpectedConditions.presenceOfElementLocated(table));

        List<WebElement> rows = tableElem.findElements(By.tagName("tr"));

        return rows.stream()
                .map(row -> row.findElements(By.tagName("td")))
                .filter(cells -> !cells.isEmpty())
                .map(cells -> cells.get(0).getText().toLowerCase()) 
                .toList();
    }

      public List<String> getResultsAuthorSearch() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement tableElem = wait.until(ExpectedConditions.presenceOfElementLocated(table));

        List<WebElement> rows = tableElem.findElements(By.tagName("tr"));

        return rows.stream()
                .map(row -> row.findElements(By.tagName("td")))
                .filter(cells -> !cells.isEmpty())
                .map(cells -> cells.get(1).getText().toLowerCase()) 
                .toList();
    }

}
