package local.tests.browser;

import local.tests.support.AbstractSeleniumTest;
import local.tests.support.UseLocalSeleniumChrome;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import static local.tests.utils.SeleniumEriBankUtils.*;

@UseLocalSeleniumChrome
public class ChromeBasicFlowTest extends AbstractSeleniumTest {

    @Test
    void openEribankWebsite() {
        driverProvider.setCapability("browserName", "chrome");
        WebDriver driver = driverProvider.getDriver();
        driver.get("https://demo-bank.ct.digital.ai");
        waitForWebPageLoadWithCustomPolling(driver, 2000);
        loginWeb(driver);
        makePaymentWeb(driver);
    }


}