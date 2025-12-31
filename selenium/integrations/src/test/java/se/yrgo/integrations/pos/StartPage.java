package se.yrgo.integrations.pos;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import se.yrgo.integrations.GeneralTestSetup;

public class StartPage {
    private WebDriver driver;
    private By search = By.cssSelector("a.btn.btn-primary");
    private By form = By.tagName("form");

    public StartPage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateToSearch() {
        WebDriverWait wait = new WebDriverWait(GeneralTestSetup.getDriver(), Duration.ofSeconds(10));
        WebElement searchButton = wait.until(ExpectedConditions.presenceOfElementLocated(search));
        searchButton.click();
    }

    public boolean isSearchFormVisible() {
        WebDriverWait wait = new WebDriverWait(GeneralTestSetup.getDriver(), Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(form));
            return true;
        } catch (Exception e) {
            return false;
        }
       
    }
}
