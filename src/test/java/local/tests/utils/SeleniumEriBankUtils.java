package local.tests.utils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public final class SeleniumEriBankUtils {

    public static void loginWeb(WebDriver driver) {
        WebElement username = waitForVisible(driver, By.cssSelector("[data-auto='username'] input"));
        username.clear();
        username.sendKeys("company");
        WebElement password = waitForVisible(driver, By.cssSelector("[data-auto='password'] input"));
        password.clear();
        password.sendKeys("company");
        waitForClickable(driver, By.cssSelector("[data-auto='login']")).click();
        waitForVisible(driver, By.cssSelector("[data-auto='transfer-funds']"));
    }
    public static void makePaymentWeb(WebDriver driver) {
        int pollingIntervalInMils = 2000;
        waitForClickable(driver, By.cssSelector("[data-auto='transfer-funds']")).click();
        waitForVisible(driver, By.cssSelector("input[name='NAME']")).clear();
        waitForVisible(driver, By.cssSelector("input[name='NAME']")).sendKeys("John");
        waitForVisible(driver, By.cssSelector("input[name='PHONE']")).clear();
        waitForVisible(driver, By.cssSelector("input[name='PHONE']")).sendKeys("0541234567");
        waitForVisible(driver, By.cssSelector("input[name='AMOUNT']")).clear();
        waitForVisible(driver, By.cssSelector("input[name='AMOUNT']")).sendKeys("1000");
        waitForClickable(driver, By.cssSelector("[data-auto='country']")).click();
        waitForClickable(driver, By.xpath("//*[text()='United Kingdom']")).click();
        waitForWebPageLoadWithCustomPolling(driver, pollingIntervalInMils);
        waitForClickable(driver, By.cssSelector("[data-auto='transfer-button']")).click();
    }

    public static void waitForWebPageLoadWithCustomPolling(WebDriver driver, int pollingIntervalInMils) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.pollingEvery(Duration.ofMillis(pollingIntervalInMils))
                .until(d -> ((JavascriptExecutor) d)
                        .executeScript("return document.readyState")
                        .equals("complete"));
    }

    private static WebElement waitForVisible(WebDriver driver, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private static WebElement waitForClickable(WebDriver driver, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

}