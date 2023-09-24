package com.detect;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.Cookie;
import com.microsoft.playwright.options.LoadState;

public class ChromeToken {

    private final String url;
    private final String urlRulet;

    public ChromeToken(String url, String urlRulet) {
        this.url = url;
        this.urlRulet = urlRulet;
    }

    public String run() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            Page page = browser.newPage();
            page.navigate(url);
            page.waitForLoadState(LoadState.NETWORKIDLE);

            page.getByRole(AriaRole.LINK,  new Page.GetByRoleOptions().setName("Giriş Yap")).click();
            page.getByPlaceholder("Kullanıcı Adı").click();
            page.getByPlaceholder("Kullanıcı Adı").fill("EXT02D47851");
            page.getByPlaceholder("Kullanıcı Adı").press("Tab");
            page.getByPlaceholder("Şifre").fill("245623");
            page.getByPlaceholder("Şifre").press("Enter");
            page.getByPlaceholder("Telefon (+90534616****)").click();
            page.getByPlaceholder("Telefon (+90534616****)").fill("9066");
            page.getByPlaceholder("Telefon (+90534616****)").press("Enter");

            page.waitForTimeout(3000);
            page.navigate(urlRulet);
            page.waitForTimeout(3000);

            Cookie cookie = page.context().cookies().stream().filter(p -> "dinamo_session".equals(p.name)).findFirst().orElseThrow(() -> new IllegalStateException("Token not found"));
            return cookie.value;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
