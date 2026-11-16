package local.tests.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.platform.suite.api.ConfigurationParameter;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ConfigurationParameter(key = "browser", value = "chrome")
public @interface UseLocalSeleniumChrome {
}