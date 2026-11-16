package local.tests.ios;


import local.tests.support.AbstractAppiumTest;
import local.tests.support.UseAvailableIosDevice;
import local.tests.utils.AppiumOSSEriBankUtils;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

@UseAvailableIosDevice
public class IosBasicFlowTest extends AbstractAppiumTest {
    String ipaPath = Path.of("src/test/resources/ExperiBank.ipa").toAbsolutePath().toString();

    @Test
    public void nativeAppBasicTest() {
        driverProvider.setCapability("app", ipaPath);
        io.appium.java_client.AppiumDriver driver = driverProvider.getDriver();
        AppiumOSSEriBankUtils.performLoginIOS(driver);
        AppiumOSSEriBankUtils.makePaymentAfterLoginIOS(driver);
    }
}
