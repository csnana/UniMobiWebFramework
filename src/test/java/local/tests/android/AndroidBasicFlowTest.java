package local.tests.android;

import io.appium.java_client.AppiumDriver;
import local.tests.support.AbstractAppiumTest;
import local.tests.support.UseAvailableAndroidDevice;
import local.tests.utils.AppiumOSSEriBankUtils;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;


@UseAvailableAndroidDevice
public class AndroidBasicFlowTest extends AbstractAppiumTest {
    String apkPath = Path.of("src/test/resources/com.experitest.ExperiBank.apk").toAbsolutePath().toString();

    @Test
    public void paymentTest() {
        driverProvider.setCapability("app", apkPath);
        AppiumDriver driver = driverProvider.getDriver();
        AppiumOSSEriBankUtils.performLoginAndroid(driver);
        AppiumOSSEriBankUtils.makePaymentAfterLoginAndroid(driver);
    }
}