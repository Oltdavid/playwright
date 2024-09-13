package page.example;

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
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
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
        try {
            Locator link = page.locator("a", new Page.LocatorOptions().setHasText("A/B Testing"));
            link.click();

            // Várakozás az oldal betöltésére
            page.waitForLoadState(LoadState.NETWORKIDLE);
            page.waitForTimeout(1000);  // További várakozás az oldal stabilizálására

            // Ellenőrizd, hogy az oldal tartalmazza az "A/B Test Control" szöveget
            String expectedText = "A/B Test Control";
            String actualPageContent = page.textContent("body");
            Assert.assertTrue(actualPageContent.contains(expectedText), "Az oldal nem tartalmazza a várt szöveget: " + expectedText);

            // Visszanavigálás a főoldalra
            page.navigate("https://the-internet.herokuapp.com/");
            page.waitForLoadState(LoadState.NETWORKIDLE);
        } catch (Exception e) {
            // Képernyőkép készítése hiba esetén
            ScreenshotUtils.takeScreenshot(page);
            throw e; // Újradobjuk a kivételt a screenshot elkészítése után
        }
    }


    @Test
    public void testAddRemoveElementsLink() {
        testLink("Add/Remove Elements");
    }

    @Test
    public void testBasicAuthLink() {
        // Új böngésző kontextus létrehozása hitelesítési adatokkal
        BrowserContext authContext = browser.newContext(new Browser.NewContextOptions()
                .setHttpCredentials("admin", "admin")); // Felhasználónév és jelszó

        // Új oldal létrehozása a hitelesítési kontextussal
        Page authPage = authContext.newPage();

        // Navigálás a Basic Auth oldalra
        authPage.navigate("https://the-internet.herokuapp.com/basic_auth");

        // Várakozás az oldal betöltésére
        authPage.waitForLoadState(LoadState.NETWORKIDLE);

        // Ellenőrizzük, hogy sikeres volt-e a hitelesítés és megjelent-e a várt szöveg
        String expectedText = "Congratulations! You must have the proper credentials.";
        String actualPageContent = authPage.textContent("body");
        Assert.assertTrue(actualPageContent.contains(expectedText), "Az oldal nem tartalmazza a várt szöveget: " + expectedText);

        // Kontextus bezárása a teszt végén
        authContext.close();
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
        // Új böngésző kontextus létrehozása hitelesítési adatokkal
        BrowserContext authContext = browser.newContext(new Browser.NewContextOptions()
                .setHttpCredentials("admin", "admin")); // Felhasználónév és jelszó

        // Új oldal létrehozása a hitelesítési kontextussal
        Page authPage = authContext.newPage();

        // Navigálás a Digest Authentication oldalra
        authPage.navigate("https://the-internet.herokuapp.com/digest_auth");

        // Várakozás az oldal betöltésére
        authPage.waitForLoadState(LoadState.NETWORKIDLE);

        // Ellenőrizzük, hogy sikeres volt-e a hitelesítés és megjelent-e a várt szöveg
        String expectedText = "Congratulations! You must have the proper credentials.";
        String actualPageContent = authPage.textContent("body");
        Assert.assertTrue(actualPageContent.contains(expectedText), "Az oldal nem tartalmazza a várt szöveget: " + expectedText);

        // Kontextus bezárása a teszt végén
        authContext.close();
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
        // Kattintás a Dynamic Loading linkre
        Locator dynamicLoadingLink = page.locator("a", new Page.LocatorOptions().setHasText("Dynamic Loading"));
        dynamicLoadingLink.click();

        // Várakozás, hogy az oldal teljesen betöltődjön
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // Kattintás az Example 1 linkre
        Locator example1Link = page.locator("//*[@id='content']/div/a[1]");
        example1Link.click();

        // Várakozás a következő oldal teljes betöltéséig
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // Assertáljuk, hogy a "Example 1: Element on page that is hidden" szöveg megjelenik-e
        String expectedText = "Example 1: Element on page that is hidden";
        Assert.assertTrue(page.textContent("body").contains(expectedText), "Az oldal nem tartalmazza a várt szöveget: " + expectedText);
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
        // Kattintás a File Download linkre
        Locator fileDownloadLink = page.locator("a", new Page.LocatorOptions().setHasText("File Download"));
        fileDownloadLink.click();

        // Várakozás, hogy az oldal teljesen betöltődjön
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // Assertáljuk, hogy a "File Downloader" cím megjelenik-e
        String expectedTitle = "File Downloader";
        Assert.assertTrue(page.textContent("body").contains(expectedTitle), "Az oldal nem tartalmazza a várt szöveget: " + expectedTitle);
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
        // Kattintás a Form Authentication linkre
        Locator formAuthLink = page.locator("a", new Page.LocatorOptions().setHasText("Form Authentication"));
        formAuthLink.click();

        // Várakozás, hogy az oldal teljesen betöltődjön
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // Assertáljuk, hogy a "Login Page" cím megjelenik-e
        String expectedTitle = "Login Page";
        Assert.assertTrue(page.textContent("body").contains(expectedTitle), "Az oldal nem tartalmazza a várt szöveget: " + expectedTitle);
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
