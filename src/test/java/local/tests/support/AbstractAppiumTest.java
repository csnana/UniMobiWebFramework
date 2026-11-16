package local.tests.support;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;

public abstract class AbstractAppiumTest {

    @RegisterExtension
    protected final LocalAppiumDriverProvider driverProvider =
            new LocalAppiumDriverProvider();

    protected static class LocalAppiumDriverProvider
            implements BeforeEachCallback, AfterEachCallback {

        private final Map<String, Object> capabilities = new LinkedHashMap<>();
        private ExtensionContext context;
        private AppiumDriver driver;

        public void setCapability(String name, Object value) {
            if (driver != null) {
                throw new IllegalStateException(
                        "Set capabilities before calling getDriver()");
            }
            if (value == null) {
                capabilities.remove(name);
            } else {
                capabilities.put(name, value);
            }
        }

        public AppiumDriver getDriver() {
            if (driver != null) {
                return driver;
            }
            if (context == null) {
                throw new IllegalStateException("No active test context");
            }

            String platform = configuration("device.platform");
            if (platform.isBlank()) {
                Class<?> testClass = context.getRequiredTestClass();
                if (testClass.isAnnotationPresent(UseAvailableAndroidDevice.class)) {
                    platform = "Android";
                } else if (testClass.isAnnotationPresent(UseAvailableIosDevice.class)) {
                    platform = "iOS";
                }
            }
            if (platform.isBlank()) {
                throw new IllegalStateException(
                        "Runner must set device.platform to Android or iOS");
            }

            CloudProperties properties = new CloudProperties();
            DeviceProvider.DeviceTarget target;
            try {
                target = new DeviceProvider().select(
                        platform,
                        configuration("device.serial"),
                        properties.optional("deviceSN"),
                        configuration("device.restricted"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while selecting a device", e);
            } catch (Exception e) {
                throw new IllegalStateException("Could not select an Appium device", e);
            }

            URL appiumUrl;
            try {
                appiumUrl = new URL(properties.required("appium.url"));
            } catch (MalformedURLException e) {
                throw new IllegalStateException("Invalid appium.url in cloud.properties", e);
            }

            if (platform.equalsIgnoreCase("Android")) {
                UiAutomator2Options options = new UiAutomator2Options()
                        .setDeviceName(target.name())
                        .setUdid(target.udid());
                capabilities.forEach(options::setCapability);
                driver = new AndroidDriver(appiumUrl, options);
            } else if (platform.equalsIgnoreCase("iOS")) {
                XCUITestOptions options = new XCUITestOptions()
                        .setDeviceName(target.name())
                        .setUdid(target.udid());
                capabilities.forEach(options::setCapability);
                driver = new IOSDriver(appiumUrl, options);
            } else {
                throw new IllegalArgumentException(
                        "Unsupported device.platform: " + platform);
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

        private String configuration(String key) {
            return context.getConfigurationParameter(key)
                    .orElseGet(() -> System.getProperty(key, ""))
                    .trim();
        }
    }
}