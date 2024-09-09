package org.example;

import com.microsoft.playwright.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.nio.file.Paths;

public class WizzAirLoginTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @BeforeClass
    public void setup() {
        // Playwright és böngésző indítása fej nélküli módban
        playwright = Playwright.create();

        // Böngésző teljes képernyős mód beállítása és helymeghatározás blokkolása
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false) // A fej nélküli módot letiltjuk
                .setArgs(java.util.Arrays.asList("--start-maximized"))); // Teljes képernyős mód

        // Új böngésző kontextus létrehozása maximális nézettel és helymeghatározási kérés letiltása
        context = browser.newContext(new Browser.NewContextOptions()
                .setPermissions(java.util.Arrays.asList("geolocation")));  // Blokkoljuk a geolocation engedélykérést

        page = context.newPage();
    }

    @Test
    public void handleCookiePopupAndLogin() {
        try {
            // Navigálás az oldalra
            page.navigate("https://wizzair.com/en-gb/information-and-services/compliments-and-complaints");

            // Cookie pop-up lekezelése
            page.waitForSelector("#onetrust-accept-btn-handler");
            page.click("#onetrust-accept-btn-handler");

            // Kattints a login gombra
            page.waitForSelector("button:has-text('LOG IN')").click();

            // Email mező megvárása és kitöltése
            page.waitForSelector("input[type='email']").fill("oltdavid@gmail.com");

            // Jelszó mező megvárása és kitöltése
            page.waitForSelector("input[type='password']").fill("Gabber2023");

            // Kétszer megnyomjuk a 'Tab' billentyűt a checkboxhoz navigáláshoz
            page.keyboard().press("Tab");
            page.keyboard().press("Tab");

            // 'Space' billentyű lenyomása a checkbox kijelöléséhez
            page.keyboard().press("Space");

            // Az 'Enter' billentyű megnyomása a bejelentkezéshez
            page.keyboard().press("Enter");

            // Várakozás a bejelentkezés után, hogy biztosan betöltsön minden adat
            page.waitForTimeout(5000); // Várakozás 5 másodpercig

            // Az oldal teljes szövegének lekérése
            String pageContent = page.locator("body").innerText();

            // Logoljuk a tartalmat, hogy lássuk, mi van betöltve
            System.out.println("Page content: " + pageContent);

            // Görgetés az account number elemhez, hogy látható legyen
            page.locator("#account-number").scrollIntoViewIfNeeded();

            // Adatok assertálása külön
            assert pageContent.contains("3880655792") : "Expected '3880655792' was not found!";


            System.out.println("All assertions passed. All expected data is present on the page.");
        } catch (Exception e) {
            // Képernyőkép készítése hiba esetén
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshot.png")));
            throw e;
        }
    }

    @Test
    public void assertAccountNumber() {
        try {
            // Navigálás az oldalra
            page.navigate("https://wizzair.com/en-gb/information-and-services/compliments-and-complaints");

            // Várakozás a cookie pop-up megjelenésére és kezelése
            page.waitForSelector("#onetrust-accept-btn-handler", new Page.WaitForSelectorOptions().setTimeout(30000)); // 30 másodpercig várunk
            page.click("#onetrust-accept-btn-handler");

            // Kattints a login gombra
            page.waitForSelector("button:has-text('LOG IN')").click();

            // Email mező megvárása és kitöltése
            page.waitForSelector("input[type='email']").fill("x-David.Oltvanyi@wizzair.com");

            // Jelszó mező megvárása és kitöltése
            page.waitForSelector("input[type='password']").fill("Qwert1234");

            // Kétszer megnyomjuk a 'Tab' billentyűt a checkboxhoz navigáláshoz
            page.keyboard().press("Tab");
            page.keyboard().press("Tab");

            // 'Space' billentyű lenyomása a checkbox kijelöléséhez
            page.keyboard().press("Space");

            // Az 'Enter' billentyű megnyomása a bejelentkezéshez
            page.keyboard().press("Enter");



            // Görgetés az account number elemhez, hogy látható legyen
            page.locator("//*[@id=\"account-number\"]").scrollIntoViewIfNeeded();

            // Várakozás a bejelentkezés után, hogy minden adat biztosan betöltődjön
            page.waitForTimeout(10000); // 10 másodperc várakozás

            // Várakozás az 'account-number' elem megjelenésére 15 másodpercig
            page.waitForSelector("//*[@id=\"account-number\"]", new Page.WaitForSelectorOptions().setTimeout(15000));

            // Az account number értékének lekérése
            String accountNumber = page.locator("//*[@id=\"account-number\"]").innerText();

            // Assertálás, hogy az account number értéke megegyezik-e a várt értékkel
            assert accountNumber.equals("4080761301") : "Expected account number '4080761301', but got: " + accountNumber;

            System.out.println("Assertion passed: Account number is correct: " + accountNumber);
        } catch (Exception e) {
            // Képernyőkép készítése hiba esetén
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshot.png")));
            throw e;
        }
    }







    @AfterClass
    public void tearDown() {
        // Böngésző és Playwright leállítása
        context.close();
        playwright.close();
    }
}
