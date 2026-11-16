package local.tests.utils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class AppiumOSSEriBankUtils {

    private static final Duration WAIT = Duration.ofSeconds(10);
    private static final String COMPANY = "company";
    private static final String ID_PREFIX = "com.experitest.ExperiBank:id/";

    private AppiumOSSEriBankUtils() {
    }

    // XPath values
    public static final String ANDROID_USER_XPATH = "//*[@id='usernameTextField']";
    public static final String ANDROID_PASS_XPATH = "//*[@id='passwordTextField']";
    public static final String ANDROID_LOGIN_XPATH = "//*[@text='Login']";
    public static final String IOS_USER_XPATH = "//*[@label='usernameTextField']";
    public static final String IOS_PASS_XPATH = "//*[@label='passwordTextField']";
    public static final String IOS_LOGIN_XPATH = "//*[@label='Login']";

    // iOS locator values
    public static final String IOS_USER = "usernameTextField";
    public static final String IOS_PASS = "passwordTextField";
    public static final String IOS_LOGIN = "loginButton";
    public static final String IOS_MAKE = "makePaymentButton";
    public static final String IOS_PHONE = "phoneTextField";
    public static final String IOS_NAME = "nameTextField";
    public static final String IOS_AMOUNT = "amountTextField";
    public static final String IOS_COUNTRY = "countryTextField";
    public static final String IOS_SEND = "sendPaymentButton";
    public static final String IOS_YES = "Yes";
    public static final String IOS_ADV = "Advanced Actions";
    public static final String IOS_QR = "QR Code Scanner";
    public static final String IOS_START = "Start !";
    public static final String IOS_ALLOW = "ALLOW";
    public static final String IOS_OK = "OK";
    public static final String IOS_LOGOUT = "Logout";

    // Android locator values
    public static final String ANDROID_USER = ID_PREFIX + "usernameTextField";
    public static final String ANDROID_PASS = ID_PREFIX + "passwordTextField";
    public static final String ANDROID_LOGIN = ID_PREFIX + "loginButton";
    public static final String ANDROID_MAKE = ID_PREFIX + "makePaymentButton";
    public static final String ANDROID_PHONE = ID_PREFIX + "phoneTextField";
    public static final String ANDROID_NAME = ID_PREFIX + "nameTextField";
    public static final String ANDROID_AMOUNT = ID_PREFIX + "amountTextField";
    public static final String ANDROID_COUNTRY = ID_PREFIX + "countryTextField";
    public static final String ANDROID_SEND = ID_PREFIX + "sendPaymentButton";
    public static final String ANDROID_YES = "android:id/button1";
    public static final String ANDROID_BALANCE = "//*[contains(@text,'$')]";

    public static void performLoginIOS(AppiumDriver driver) {
        sendKeys(driver, AppiumBy.accessibilityId(IOS_USER), COMPANY);
        sendKeys(driver, AppiumBy.accessibilityId(IOS_PASS), COMPANY);
        click(driver, AppiumBy.accessibilityId(IOS_LOGIN));
    }

    public static void disableSavePasswordPopupIOS(AppiumDriver driver) {
        clickIfPresent(driver, AppiumBy.xpath("//*[@label='Not Now']"));
    }

    public static void performLoginAndDisablePopupIOS(AppiumDriver driver) {
        performLoginIOS(driver);
        disableSavePasswordPopupIOS(driver);
    }

    public static void makePaymentAfterLoginIOS(AppiumDriver driver) {
        click(driver, AppiumBy.accessibilityId(IOS_MAKE));
        sendKeys(driver, AppiumBy.accessibilityId(IOS_PHONE), "0501234567");
        sendKeys(driver, AppiumBy.accessibilityId(IOS_NAME), "ExperiTester");
        sendKeys(driver, AppiumBy.accessibilityId(IOS_AMOUNT), "50");
        sendKeys(driver, AppiumBy.accessibilityId(IOS_COUNTRY), "India");
    }

    public static void logoutAndClearUsernameField(AppiumDriver driver) {
        click(driver, AppiumBy.accessibilityId(IOS_LOGOUT));
        waitFor(driver).until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.accessibilityId(IOS_USER))).clear();
    }

    public static void goToQRAfterLoginAndStartScanningIOS(AppiumDriver driver) {
        click(driver, AppiumBy.accessibilityId(IOS_ADV));
        click(driver, AppiumBy.accessibilityId(IOS_QR));
        click(driver, AppiumBy.accessibilityId(IOS_START));
    }

    public static void allowCameraIOS(AppiumDriver driver) {
        if (!clickIfPresent(driver, AppiumBy.accessibilityId(IOS_ALLOW))) {
            clickIfPresent(driver, AppiumBy.accessibilityId(IOS_OK));
        }
    }

    public static void performLoginAndroid(AppiumDriver driver) {
        sendKeys(driver, AppiumBy.id(ANDROID_USER), COMPANY);
        sendKeys(driver, AppiumBy.id(ANDROID_PASS), COMPANY);
        click(driver, AppiumBy.id(ANDROID_LOGIN));
    }

    public static void makePaymentAfterLoginAndroid(AppiumDriver driver) {
        click(driver, AppiumBy.id(ANDROID_MAKE));
        sendKeys(driver, AppiumBy.id(ANDROID_PHONE), "0508422511");
        sendKeys(driver, AppiumBy.id(ANDROID_NAME), "testuser");
        sendKeys(driver, AppiumBy.id(ANDROID_AMOUNT), "50");
        sendKeys(driver, AppiumBy.id(ANDROID_COUNTRY), "india");
        click(driver, AppiumBy.id(ANDROID_SEND));
        clickYesOnAndroidSystemPopup(driver);
    }

    public static void clickYesOnAndroidSystemPopup(AppiumDriver driver) {
        click(driver, AppiumBy.id(ANDROID_YES));
    }

    public static String getBalanceAndroid(AppiumDriver driver) {
        return waitFor(driver)
                .until(ExpectedConditions.visibilityOfElementLocated(
                        AppiumBy.xpath(ANDROID_BALANCE)))
                .getText();
    }

    public static String getBalanceIOS(AppiumDriver driver) {
        return waitFor(driver)
                .until(ExpectedConditions.visibilityOfElementLocated(
                        AppiumBy.xpath("//*[contains(@name,'$')]")))
                .getText();
    }

    public static int convertEribankBalanceToCents(String balance) {
        String[] parts = balance.split("\\s");
        return Integer.parseInt(parts[parts.length - 1].replaceAll("[$,.]", ""));
    }

    public static WebElement getUsernameElement(AppiumDriver driver) {
        String platform = String.valueOf(
                driver.getCapabilities().getCapability("platformName"));

        By locator = platform.equalsIgnoreCase("iOS")
                ? AppiumBy.accessibilityId(IOS_USER)
                : AppiumBy.id(ANDROID_USER);

        return waitFor(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static String getUserNameXPath(String platform) {
        return platform.equalsIgnoreCase("iOS") ? IOS_USER_XPATH : ANDROID_USER_XPATH;
    }

    public static String getPasswordXPath(String platform) {
        return platform.equalsIgnoreCase("iOS") ? IOS_PASS_XPATH : ANDROID_PASS_XPATH;
    }

    public static String getLoginXPath(String platform) {
        return platform.equalsIgnoreCase("iOS") ? IOS_LOGIN_XPATH : ANDROID_LOGIN_XPATH;
    }

    private static WebDriverWait waitFor(AppiumDriver driver) {
        return new WebDriverWait(driver, WAIT);
    }

    private static void click(AppiumDriver driver, By locator) {
        waitFor(driver).until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    private static void sendKeys(AppiumDriver driver, By locator, String value) {
        WebElement element = waitFor(driver)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(value);
    }

    private static boolean clickIfPresent(AppiumDriver driver, By locator) {
        List<WebElement> elements = driver.findElements(locator);
        if (elements.isEmpty()) {
            return false;
        }
        waitFor(driver).until(ExpectedConditions.elementToBeClickable(locator)).click();
        return true;
    }
}