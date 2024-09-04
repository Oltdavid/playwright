package org.example;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PlaywrightTest {

    private Playwright playwright;
    private Browser browser;
    private Page page;

    @BeforeClass
    public void setUp() {
        System.out.println("Setting up Playwright.");
        playwright = Playwright.create();
        browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
    }

    @Test
    public void testNavigationAndScreenshot() {
        System.out.println("Starting testNavigationAndScreenshot.");
        try {
            page.navigate("https://mvnrepository.com/artifact/io.qameta.allure/allure-maven/2.8");
            System.out.println("Test passed: Navigation and screenshot completed.");

            // Assert that the page title contains a specific string to simulate a "passed" test
            String pageTitle = page.title();
            Assert.assertTrue(pageTitle.contains("Allure Maven"), "Page title does not contain expected text.");

        } catch (Exception e) {
            handleTestFailure("failed_example1.png", e);
        }
    }

    @Test
    public void testNavigationAndScreenshot1() {
        System.out.println("Starting testNavigationAndScreenshot1.");
        try {
            page.navigate("https://mvnrepository.com/artifact/io.qameta.allure/allure-maven/2.8");
            System.out.println("Test passed: Navigation and screenshot completed.");

            // Assert that the page title contains a specific string to simulate a "passed" test
            String pageTitle = page.title();
            Assert.assertTrue(pageTitle.contains("Allure Maven"), "Page title does not contain expected text.");

        } catch (Exception e) {
            handleTestFailure("failed_example2.png", e);
        }
    }

    @Test
    public void testFailedScenario() {
        System.out.println("Starting testFailedScenario.");
        try {
            page.navigate("https://example.com/non-existent-page");
            System.out.println("Test passed: Navigation and screenshot completed.");

            // This assert is designed to fail to demonstrate a "failed" test case
            Assert.assertTrue(page.title().contains("Non-Existent Title"), "Page title unexpectedly matches.");

        } catch (Exception e) {
            handleTestFailure("failed_nonexistent.png", e);
        }
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Tearing down Playwright.");
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    private void handleTestFailure(String fileName, Exception e) {
        System.err.println("Test failed: " + e.getMessage());
        byte[] screenshot = takeScreenshot(fileName);
        if (screenshot != null) {
            saveScreenshotAttachment(screenshot);
        }
        Assert.fail("Test failed due to an exception: " + e.getMessage());
    }

    private byte[] takeScreenshot(String fileName) {
        try {
            Path path = Paths.get(fileName);
            page.screenshot(new Page.ScreenshotOptions().setPath(path));
            System.out.println("Screenshot saved: " + path.toAbsolutePath());
            return Files.readAllBytes(path);
        } catch (Exception e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
            return null;
        }
    }

    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] saveScreenshotAttachment(byte[] screenshot) {
        return screenshot;
    }
}
