package local.tests.suites.ios;

import local.tests.android.AndroidBasicFlowTest;
import local.tests.ios.IosBasicFlowTest;
import local.tests.support.UseAvailableAndroidDevice;
import local.tests.support.UseAvailableIosDevice;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        IosBasicFlowTest.class,
})
@UseAvailableIosDevice
public class IosRunner01 {
}
