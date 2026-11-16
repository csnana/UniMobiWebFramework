# UniMobiWebFramework

This project is a sample test framework for mobile and web automation.

It is made to run tests for:
- Android apps , iOS apps  and Web browser tests in Chrome

The project uses Java and Gradle. It also uses tools like:
- Appium , Selenium , JUnit 5

This project is a basic example for testing apps and web pages in different environments.

## What the project does

The project runs automated test cases for:
- Android devices, iOS devices and Chrome browser

It can be used for checking if an app works correctly, if a page loads correctly, and if user actions like tapping, typing, and clicking work as expected.

## Project structure

The main files and folders are:

- build.gradle
    - This file tells Gradle how to build the project.
    - It also adds the libraries needed for testing.
    - It sets Java version and test settings.

- settings.gradle
    - This file tells Gradle about the project.

- gradlew and gradlew.bat
    - These are the Gradle wrapper files.
    - They help run Gradle without installing Gradle manually.

- gradle/wrapper/
    - These files are used by the Gradle wrapper.

- buildSrc/
    - This folder contains custom Groovy classes used by Gradle tasks.
    - It helps run the automation suites.

- src/main/java/org/example/Main.java
    - This is a simple Java entry point.
    - It is not the main test logic, but it shows the Java project setup.

- src/test/java/local/tests/
    - This folder contains test classes.
    - It includes tests for Android, iOS, and browser.

- src/test/resources/
    - This folder contains files needed by tests, such as app files and properties files.

## Tools used

This project uses:

- Java 21
- Gradle
- Appium
- Selenium
- JUnit 5

These tools are used to open apps or browsers, interact with elements, and verify results.

## Test types in this project

### Android tests
These tests are for Android devices or Android apps.

### iOS tests
These tests are for iPhone or iPad devices and iOS apps.

### Browser tests
These tests run in Chrome using Selenium.

## How the test flow works

1. The project sets up the test environment.
2. It picks the platform: Android, iOS, or browser.
3. It starts the required tool:
    - Appium for mobile app testing
    - Selenium for browser testing
4. It opens the app or page.
5. It performs actions, such as clicking, typing, or swiping.
6. It checks whether the expected result appears.
7. It reports pass or fail.

## File examples

Some important test files include:

- AndroidBasicFlowTest.java
    - Basic Android test flow

- IoSBasicFlowTest.java
    - Basic iOS test flow

- ChromeBasicFlowTest.java
    - Basic browser test flow

- AndroidRunner01.java
    - Runner for Android test suite

- BrowserRunner01.java
    - Runner for browser test suite

- IoSRunner01.java
    - Runner for iOS test suite

- AbstractAppiumTest.java
    - Base class for Appium-based mobile tests

- AbstractSeleniumTest.java
    - Base class for Selenium-based browser tests

## Build and run

Use Gradle wrapper to run tests uisng below two commands

Example:

```bash
./gradlew test 
./gradlew runAllPlatformAutomation
```

### iOS app install note

This project includes an iOS app file in the test resources folder.

Important: an .ipa file built locally may not install directly on a physical iPhone or iPad without extra setup.

Sometimes the app must be rebuilt and signed correctly for the device. Also, the Appium iOS setup may require the WebDriverAgentRunner and related iOS app components to be built from Xcode.
This is part of the normal iOS setup for physical devices.

So, if an app does not install or launch correctly:rebuild the app in Xcode  make sure the app is signed correctly
build the WebDriverAgentRunner from Xcode  confirm the device is trusted and connected properly

This is common for Appium iOS testing on real devices.