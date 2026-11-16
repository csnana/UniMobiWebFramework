package local.tests.suites.android;

import local.tests.android.AndroidBasicFlowTest;
import local.tests.support.UseAvailableAndroidDevice;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        AndroidBasicFlowTest.class,
})
@UseAvailableAndroidDevice
public class AndroidRunner01 {
}
