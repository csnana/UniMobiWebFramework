package local.tests.support;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public abstract class AbstractSeleniumTest {

    @RegisterExtension
    protected final LocalSeleniumDriverProvider driverProvider =
            new LocalSeleniumDriverProvider();

    protected static class LocalSeleniumDriverProvider
            implements BeforeEachCallback, AfterEachCallback {

        private final Map<String, Object> capabilities = new LinkedHashMap<>();
        private ExtensionContext context;
        private WebDriver driver;

        public void setCapability(String name, Object value) {
            if (driver != null) {
                throw new IllegalStateException("Set capabilities before calling getDriver()");
            }
            if (value == null) {
                capabilities.remove(name);
            } else {
                capabilities.put(name, value);
            }
        }

        public WebDriver getDriver() {
            if (driver != null) {
                return driver;
            }
            if (context == null) {
                throw new IllegalStateException("No active test context");
            }

            CloudProperties properties = new CloudProperties();
            ChromeOptions options = new ChromeOptions();

            // Common Chrome local stability flags
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-notifications");
            options.addArguments("--incognito");
            options.addArguments("--disable-features=PasswordLeakDetection,PasswordManagerOnboarding");
            options.addArguments("--disable-blink-features=AutomationControlled");

            // Hide password save / breach popups
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            prefs.put("profile.default_content_setting_values.notifications", 2);
            prefs.put("profile.default_content_setting_values.popups", 2);
            options.setExperimentalOption("prefs", prefs);

            capabilities.forEach(options::setCapability);

            try {
                driver = new RemoteWebDriver(
                        new URL(properties.required("selenium.url")),
                        options
                );
            } catch (MalformedURLException e) {
                throw new IllegalStateException("Invalid selenium.url in cloud.properties", e);
            }

            return driver;
        }

        @Override
        public void beforeEach(ExtensionContext context) {
            this.context = context;
        }

        @Override
        public void afterEach(ExtensionContext context) {
            try {
                if (driver != null) {
                    driver.quit();
                }
            } finally {
                driver = null;
                capabilities.clear();
                this.context = null;
            }
        }
    }
}