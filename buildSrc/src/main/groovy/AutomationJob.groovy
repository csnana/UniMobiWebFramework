import groovy.lang.DelegatesTo
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskProvider

abstract class AutomationJob extends DefaultTask {
    private int testTaskCount = 0
    private final List<TaskProvider<AutomationTest>> testTasks = []

    void automationTest(
            @DelegatesTo(value = AutomationTest, strategy = Closure.DELEGATE_FIRST)
            Closure<?> configuration) {
        String childName = "${name}__${testTaskCount++}"
        TaskProvider<AutomationTest> child =
                project.tasks.register(childName, AutomationTest)

        child.configure {
            configuration.delegate = delegate
            configuration.resolveStrategy = Closure.DELEGATE_FIRST
            configuration.call()
        }

        testTasks.add(child)
        dependsOn(child)
    }

    void systemProperty(String propertyName, Object value) {
        testTasks.each { child ->
            child.configure {
                systemProperty(propertyName, value.toString())
            }
        }
    }
}