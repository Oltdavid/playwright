package page.example;

import com.microsoft.playwright.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import page.WizzAirHomePage;
import page.WizzAirLoginPage;

public class WizzAirLoginTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;
    WizzAirHomePage wizzAirHomePage;
    WizzAirLoginPage wizzAirLoginPage;

    @BeforeClass
    public void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        page = context.newPage();
        wizzAirHomePage = new WizzAirHomePage(page);
        wizzAirLoginPage = new WizzAirLoginPage(page);
    }

    @Test
    public void loginTest() {
        // Navigate to WizzAir site
        page.navigate("https://wizzair.com/en-gb/information-and-services/compliments-and-complaints");

        // Accept cookies and login
        wizzAirHomePage.acceptCookies();
        wizzAirHomePage.clickLoginButton();
        wizzAirLoginPage.fillEmail("test@example.com");
        wizzAirLoginPage.fillPassword("password123");

        // Submit login form (e.g., pressing Enter)
        page.keyboard().press("Enter");
    }

    @AfterClass
    public void tearDown() {
        context.close();
        browser.close();
        playwright.close();
    }
}
