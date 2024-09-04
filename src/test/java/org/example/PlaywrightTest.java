package org.example;

import com.microsoft.playwright.*;

import io.qameta.allure.Attachment;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PlaywrightTest {

    private Playwright playwright;
    private Browser browser;
    private Page page;

    @Parameters({"browserName"})
    @BeforeClass
    public void setUp(String browserName) {
        System.out.println("Setting up Playwright for browser: " + browserName);
        playwright = Playwright.create();

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(false);

        switch (browserName.toLowerCase()) {
            case "chromium":
                browser = playwright.chromium().launch(options);
                break;
            case "firefox":
                browser = playwright.firefox().launch(options);
                break;
            case "webkit":
                browser = playwright.webkit().launch(options);
                break;
            case "edge":
                // Microsoft Edge Chromiumként van implementálva Playwright-ban
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("msedge").setHeadless(false));
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }

        page = browser.newPage();
    }


    @Test
    public void testElementPresence() {
        System.out.println("Starting testElementPresence.");
        try {
            // Navigáció az oldalra
            page.navigate("https://www.softwaretestinghelp.com/");

            // Explicit várakozás a megadott XPath-szal rendelkező elem megjelenésére
            page.waitForSelector("//*[@id=\"masthead\"]/div/div[2]/h1/a");

            // Ellenőrizd, hogy az elem megjelenik az oldalon
            boolean isElementVisible = page.isVisible("//*[@id=\"masthead\"]/div/div[2]/h1/a");
            Assert.assertTrue(isElementVisible, "The element with the specified XPath is not visible.");

            // Képernyőkép készítése az elem megjelenése után
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("element_present.png")));
            System.out.println("Test passed: Element presence and screenshot completed.");

        } catch (Exception e) {
            handleTestFailure("failed_element_presence.png", e);
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
