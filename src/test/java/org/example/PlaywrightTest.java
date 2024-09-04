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



    @Test
    public void testNavigationAndScreenshot() {
        System.out.println("Starting testNavigationAndScreenshot.");
        try {
            // Navigáció az oldalra
            page.navigate("https://www.softwaretestinghelp.com/");

            // Explicit várakozás egy adott elem megjelenésére, pl. h1 elem
            page.waitForSelector("h1");

            // Assert a lap címére
            String pageTitle = page.title();
            System.out.println("This is the title: " + pageTitle);
            Assert.assertTrue(pageTitle.contains("Software Testing Help - FREE IT Courses and Business Software Reviews"), "Page title does not contain expected text.");

            // Képernyőkép készítése az oldal betöltése után
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("example.png")));
            System.out.println("Test passed: Navigation and screenshot completed.");

        } catch (Exception e) {
            handleTestFailure("failed_example1.png", e);
        }
    }

    @Test
    public void testNavigationAndScreenshot1() {
        System.out.println("Starting testNavigationAndScreenshot1.");
        try {
            // Navigáció az oldalra
            page.navigate("https://www.softwaretestinghelp.com/");

            // Explicit várakozás egy adott elem megjelenésére
            page.waitForSelector("h1");

            // Assert a lap címére
            String pageTitle = page.title();
            Assert.assertTrue(pageTitle.contains("Software Testing Help - FREE IT Courses and Business Software ReviewsSoftware Testing Help - FREE IT Courses and Business Software Reviews"), "Page title does not contain expected text.");

            // Képernyőkép készítése az oldal betöltése után
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("example2.png")));
            System.out.println("Test passed: Navigation and screenshot completed.");

        } catch (Exception e) {
            handleTestFailure("failed_example2.png", e);
        }
    }

    @Test
    public void testFailedScenario() {
        System.out.println("Starting testFailedScenario.");
        try {
            // Navigáció egy nem létező oldalra
            page.navigate("https://example.com/non-existent-page");

            // Explicit várakozás egy adott elem megjelenésére, pl. h1 elem
            page.waitForSelector("h1");

            // Képernyőkép készítése az oldal betöltése után
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("nonexistent.png")));
            System.out.println("Test passed: Navigation and screenshot completed.");

            // Ez az assert hiba miatt fog elbukni
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
