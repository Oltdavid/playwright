package org.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import io.qameta.allure.Attachment;

public class HerokuAppTest {
    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
    }

    public class ScreenshotUtils {

        @Attachment(value = "Screenshot on Failure", type = "image/png")
        public static byte[] takeScreenshot(Page page) {
            return page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        }
    }

    @Test
    public void testAllLinksOnPage() {
        System.out.println("Starting testAllLinksOnPage.");
        try {
            // Navigáció a főoldalra
            page.navigate("https://the-internet.herokuapp.com/");

            // Várakozás, hogy az oldal betöltődjön
            page.waitForLoadState(LoadState.NETWORKIDLE);

            // Az összes link begyűjtése a főoldalról
            Locator links = page.locator("#content ul li a");

            // Minden linkre kattintás
            for (int i = 0; i < links.count(); i++) {
                Locator link = links.nth(i);
                String linkText = link.innerText();
                System.out.println("Kattintás a linkre: " + linkText);

                // Kattintás a linkre és várakozás az oldal betöltődésére
                link.click();
                page.waitForLoadState(LoadState.NETWORKIDLE);

                // Ellenőrzés, hogy az oldal betöltődött
                Assert.assertTrue(page.isVisible("body"), "Az oldal nem töltődött be megfelelően: " + linkText);

                // Visszanavigálás a főoldalra
                page.navigate("https://the-internet.herokuapp.com/");
                page.waitForLoadState(LoadState.NETWORKIDLE);
            }
        } catch (Exception e) {
            handleTestFailure("failed_test_all_links.png", e);
        }
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

    private void handleTestFailure(String screenshotPath, Exception e) {
        System.out.println("Teszt sikertelen: " + e.getMessage());
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));
        Assert.fail("Teszt meghiúsult: " + e.getMessage());
    }
}
