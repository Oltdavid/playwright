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
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("msedge").setHeadless(false));
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }

        page = browser.newPage();
    }

    @Test
    public void testElementPresenceValid() {
        System.out.println("Starting testElementPresenceValid.");
        try {
            // Navigáció az oldalra
            page.navigate("https://www.softwaretestinghelp.com/");

            // Várakozás a meglévő elemre
            page.waitForSelector("//*[@id='masthead']/div/div[2]/h1/a");

            // Ellenőrizd, hogy az elem megjelenik az oldalon
            boolean isElementVisible = page.isVisible("//*[@id='masthead']/div/div[2]/h1/a");
            Assert.assertTrue(isElementVisible, "The element with the specified XPath is not visible.");

            System.out.println("Test passed: Element presence confirmed.");

        } catch (Exception e) {
            handleTestFailure("failed_element_presence_valid.png", e);
        }
    }

    @Test
    public void testElementPresenceInvalid() {
        System.out.println("Starting testElementPresenceInvalid.");
        try {
            // Navigáció az oldalra
            page.navigate("https://www.softwaretestinghelp.com/");

            // Szándékosan hibás XPath egy elemhez, amely nem létezik
            page.waitForSelector("//*[@id='non-existent-element']", new Page.WaitForSelectorOptions().setTimeout(5000));

            // Ellenőrizd, hogy az elem megjelenik az oldalon (ez a sor soha nem fut le a hibás XPath miatt)
            boolean isElementVisible = page.isVisible("//*[@id='non-existent-element']");
            Assert.assertTrue(isElementVisible, "The element with the specified XPath is not visible.");

        } catch (Exception e) {
            handleTestFailure("failed_element_presence_invalid.png", e);
        }
    }

    @Test
    public void testTitleCheck() {
        System.out.println("Starting testTitleCheck.");
        try {
            page.navigate("https://www.softwaretestinghelp.com/");
            String title = page.title();
            Assert.assertTrue(title.contains("Software Testing Help"), "Title does not match expected.");
            System.out.println("Test passed: Title check passed.");
        } catch (Exception e) {
            handleTestFailure("failed_title_check.png", e);
        }
    }

    @Test
    public void testNavigationToInvalidURL() {
        System.out.println("Starting testNavigationToInvalidURL.");
        try {
            page.navigate("https://www.invalid-url-example.com/");
            Assert.fail("Navigation to invalid URL should have failed.");
        } catch (Exception e) {
            handleTestFailure("failed_navigation_to_invalid_url.png", e);
        }
    }

    @Test
    public void testPageContainsText() {
        System.out.println("Starting testPageContainsText.");
        try {
            page.navigate("https://www.softwaretestinghelp.com/");
            boolean textExists = page.locator("text=Software Testing Help").isVisible();
            Assert.assertTrue(textExists, "Expected text not found on page.");
            System.out.println("Test passed: Page contains the expected text.");
        } catch (Exception e) {
            handleTestFailure("failed_page_contains_text.png", e);
        }
    }

    @Test
    public void testElementClick() {
        System.out.println("Starting testElementClick.");
        try {
            page.navigate("https://www.softwaretestinghelp.com/");
            page.click("//*[@id='menu-item-2824']/a");
            Assert.assertTrue(page.url().contains("tutorials"), "URL does not contain expected path after click.");
            System.out.println("Test passed: Element click and URL validation passed.");
        } catch (Exception e) {
            handleTestFailure("failed_element_click.png", e);
        }
    }

    @Test
    public void testInvalidSelector() {
        System.out.println("Starting testInvalidSelector.");
        try {
            page.navigate("https://www.softwaretestinghelp.com/");
            page.locator("##invalidSelector").click();
            Assert.fail("Invalid selector should cause an exception.");
        } catch (Exception e) {
            handleTestFailure("failed_invalid_selector.png", e);
        }
    }

    @Test
    public void testFormSubmit() {
        System.out.println("Starting testFormSubmit.");
        try {
            page.navigate("https://www.softwaretestinghelp.com/");
            page.fill("input[name='s']", "automation testing");
            page.press("input[name='s']", "Enter");
            Assert.assertTrue(page.url().contains("s=automation+testing"), "Form submit failed or incorrect search query.");
            System.out.println("Test passed: Form submit and search result URL validation passed.");
        } catch (Exception e) {
            handleTestFailure("failed_form_submit.png", e);
        }
    }

    @Test
    public void testBrokenImage() {
        System.out.println("Starting testBrokenImage.");
        try {
            page.navigate("https://www.softwaretestinghelp.com/");
            boolean imageIsBroken = !page.locator("img[src='/path/to/nonexistent/image.png']").isVisible();
            Assert.assertTrue(imageIsBroken, "Broken image should not be visible.");
            System.out.println("Test passed: Broken image not visible as expected.");
        } catch (Exception e) {
            handleTestFailure("failed_broken_image.png", e);
        }
    }

    @Test
    public void testElementCount() {
        System.out.println("Starting testElementCount.");
        try {
            page.navigate("https://www.softwaretestinghelp.com/");
            int count = page.locator("a").count();
            Assert.assertTrue(count > 10, "There should be more than 10 links on the page.");
            System.out.println("Test passed: Element count check passed.");
        } catch (Exception e) {
            handleTestFailure("failed_element_count.png", e);
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
