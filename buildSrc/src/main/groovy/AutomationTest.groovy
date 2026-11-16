import org.gradle.api.tasks.testing.Test

abstract class AutomationTest extends Test {
    AutomationTest() {
        def testSourceSet = project.sourceSets.test
        testClassesDirs = testSourceSet.output.classesDirs
        classpath = testSourceSet.runtimeClasspath
        useJUnitPlatform()
    }

    void suite(String suiteClassName) {
        scanForTestClasses = false
        include suiteClassName.replace('.', '/') + '.class'
    }
}