package org.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.qameta.allure.Attachment;

public class HerokuAppLinkTests {
    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        page.navigate("https://the-internet.herokuapp.com/");
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    public class ScreenshotUtils {

        @Attachment(value = "Screenshot on Failure", type = "image/png")
        public static byte[] takeScreenshot(Page page) {
            return page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        }
    }

    @Test
    public void testABTestingLink() {
        testLink("A/B Testing");
    }

    @Test
    public void testAddRemoveElementsLink() {
        testLink("Add/Remove Elements");
    }

    @Test
    public void testBasicAuthLink() {
        testLink("Basic Auth");
    }

    @Test
    public void testBrokenImagesLink() {
        testLink("Broken Images");
    }

    @Test
    public void testChallengingDomLink() {
        testLink("Challenging DOM");
    }

    @Test
    public void testCheckboxesLink() {
        testLink("Checkboxes");
    }

    @Test
    public void testContextMenuLink() {
        testLink("Context Menu");
    }

    @Test
    public void testDigestAuthenticationLink() {
        testLink("Digest Authentication");
    }

    @Test
    public void testDisappearingElementsLink() {
        testLink("Disappearing Elements");
    }

    @Test
    public void testDragAndDropLink() {
        testLink("Drag and Drop");
    }

    @Test
    public void testDropdownLink() {
        testLink("Dropdown");
    }

    @Test
    public void testDynamicContentLink() {
        testLink("Dynamic Content");
    }

    @Test
    public void testDynamicControlsLink() {
        testLink("Dynamic Controls");
    }

    @Test
    public void testDynamicLoadingLink() {
        testLink("Dynamic Loading");
    }

    @Test
    public void testEntryAdLink() {
        testLink("Entry Ad");
    }

    @Test
    public void testExitIntentLink() {
        testLink("Exit Intent");
    }

    @Test
    public void testFileDownloadLink() {
        testLink("File Download");
    }

    @Test
    public void testFileUploadLink() {
        testLink("File Upload");
    }

    @Test
    public void testFloatingMenuLink() {
        testLink("Floating Menu");
    }

    @Test
    public void testForgotPasswordLink() {
        testLink("Forgot Password");
    }

    @Test
    public void testFormAuthenticationLink() {
        testLink("Form Authentication");
    }

    @Test
    public void testFramesLink() {
        testLink("Frames");
    }

    @Test
    public void testGeolocationLink() {
        testLink("Geolocation");
    }

    @Test
    public void testHorizontalSliderLink() {
        testLink("Horizontal Slider");
    }

    @Test
    public void testHoversLink() {
        testLink("Hovers");
    }

    @Test
    public void testInfiniteScrollLink() {
        testLink("Infinite Scroll");
    }

    @Test
    public void testInputsLink() {
        testLink("Inputs");
    }

    @Test
    public void testJQueryUIMenusLink() {
        testLink("JQuery UI Menus");
    }

    @Test
    public void testJavaScriptAlertsLink() {
        testLink("JavaScript Alerts");
    }

    @Test
    public void testJavaScriptOnloadEventErrorLink() {
        testLink("JavaScript onload event error");
    }

    @Test
    public void testKeyPressesLink() {
        testLink("Key Presses");
    }

    @Test
    public void testLargeDeepDOMLink() {
        testLink("Large & Deep DOM");
    }

    @Test
    public void testMultipleWindowsLink() {
        testLink("Multiple Windows");
    }

    @Test
    public void testNestedFramesLink() {
        testLink("Nested Frames");
    }

    // Helper method to test each link
    private void testLink(String linkText) {
        Locator link = page.locator("a", new Page.LocatorOptions().setHasText(linkText));
        link.click();
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // Assert that the page contains the text of the clicked link
        Assert.assertTrue(page.textContent("body").contains(linkText), "Az oldal nem tartalmazza a várt szöveget: " + linkText);

        // Visszanavigálás a főoldalra
        page.navigate("https://the-internet.herokuapp.com/");
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    @AfterMethod
    public void tearDown() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}
