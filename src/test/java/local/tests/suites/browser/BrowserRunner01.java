package local.tests.suites.browser;

import local.tests.browser.ChromeBasicFlowTest;
import local.tests.support.UseLocalSeleniumChrome;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        ChromeBasicFlowTest.class
})
@UseLocalSeleniumChrome
public class BrowserRunner01 {
}