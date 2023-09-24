package com.detect;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ChromeRunnable implements Runnable {

    private String url;
    private String domain;
    private String token;

    private final ReentrantLock lockLightning = new ReentrantLock();
    private final ReentrantLock lockTurkLightning = new ReentrantLock();
    private final ReentrantLock lockXxxtremeLightning = new ReentrantLock();
    private final ReentrantLock lockImmersive = new ReentrantLock();


    public ChromeRunnable(String url, String domain, String token) {
        this.url = url;
        this.domain = domain;
        this.token = token;
    }

    @Override
    public void run() {

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();
            page.navigate(url);
            page.waitForLoadState(LoadState.NETWORKIDLE);
            com.microsoft.playwright.options.Cookie cookie = new com.microsoft.playwright.options.Cookie("dinamo_session", token);
            cookie.setUrl(domain);
            page.context().addCookies(Arrays.asList(cookie));
            page.reload();
            page.waitForTimeout(3000);

            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "123456");
            connection.setAutoCommit(false);

            while (true) {


                if (url.contains("47844030144")) {
                    lockXxxtremeLightning.lock();
                } else if (url.contains("40892081196")) {
                    lockTurkLightning.lock();
                } else if (url.contains("8345030379")) {
                    lockLightning.lock();
                } else if (url.contains("8510079294")) {
                    lockImmersive.lock();
                } else {
                    throw new IllegalArgumentException();
                }

                //long baslangicZamani = System.currentTimeMillis();

                try {
                    page.waitForTimeout(3000);

                    Frame frameParent = page.frames().stream().filter(p -> p.url() != null && p.url().contains("evolutionlivecasino")).findFirst().get();

                    boolean isTimeout = checkTimeout(frameParent);
                    if (isTimeout) {
                        page.reload(new Page.ReloadOptions().setTimeout(80000));
                        page.waitForTimeout(3000);
                        frameParent = page.frames().stream().filter(p -> p.url() != null && p.url().contains("evolutionlivecasino")).findFirst().get();
                        System.out.println("Ekran Yenilendi");
                    }

                    String html = check(frameParent);

                    List<Integer> comingRecentNumber = new ArrayList<>();
                    List<Integer> comingAllNumber = new ArrayList<>();

                    Document document = Jsoup.parse(html, "UTF-8");

                    Elements recentNumbers = document.getElementsByClass("numbers--2435c").get(0).getElementsByTag("span");
                    for (Element element : recentNumbers) {
                        if (!element.text().contains("x")) {
                            comingRecentNumber.add(Integer.valueOf(element.text()));
                            comingAllNumber.add(Integer.valueOf(element.text()));
                        }
                    }

                    try {
                        Elements otherNumbers = document.getElementsByClass("numbers--2435c").get(1).getElementsByTag("span");
                        for (Element element : otherNumbers) {
                            if (!element.text().contains("x")) {
                                comingAllNumber.add(Integer.valueOf(element.text()));
                            }
                        }
                    } catch (Exception e) {

                    }

                    dbSaveNumbers(comingAllNumber, url, connection);

                } catch (Exception e) {
                    System.err.println("Parse hatası: " + e.getMessage());
                    e.printStackTrace();
                    page.reload();
                    page.waitForTimeout(5000);
                } finally {
                    if (url.contains("47844030144")) {
                        lockXxxtremeLightning.unlock();
                    } else if (url.contains("40892081196")) {
                        lockTurkLightning.unlock();
                    } else if (url.contains("8345030379")) {
                        lockLightning.unlock();
                    } else if (url.contains("8510079294")) {
                        lockImmersive.unlock();
                    }
                }


                /*long bitisZamani = System.currentTimeMillis();

                long gecenSure = bitisZamani - baslangicZamani;
                double saniye = (double) gecenSure / 1000;
                System.out.println("Kod " + saniye + " saniye sürdü : " + gecenSure);
                */

            }
        } catch (Exception e) {
            System.err.println("Playwright hatası: " + e.getMessage());
        }

    }

    public void dbSaveNumbers(List<Integer> allNumbers, String saveUrl, Connection connection) {
        try {

            String table;
            if (saveUrl.contains("47844030144")) {
                table = "live_xxxtreme_lightning";
            } else if (saveUrl.contains("40892081196")) {
                table = "live_turk_lightning";
            } else if (saveUrl.contains("8345030379")) {
                table = "live_lightning";
            } else if (url.contains("8510079294")) {
                table = "live_immersive";
            } else {
                throw new IllegalArgumentException();
            }


            String sql = "TRUNCATE TABLE dinamobet." + table;

            //String sql = "DELETE FROM dinamobet." + table;

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
            }

            sql = "INSERT INTO dinamobet." + table + " (d_id, d_number) VALUES (?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (Integer veri : allNumbers) {
                    preparedStatement.setString(1, UUID.randomUUID().toString());
                    preparedStatement.setInt(2, veri);
                    // preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                    preparedStatement.executeUpdate();
                }
            }
            connection.commit();

        } catch (SQLException e) {
            System.err.println("Bağlantı hatası: " + e.getMessage());
        } finally {

        }
    }

    public String check(Frame frameParent) {
        List<Frame> frameList = frameParent.childFrames().stream().filter(p -> p.url() != null && p.url().contains("evolutionlivecasino")).collect(Collectors.toList());
        for (Frame frame : frameList) {
            String body = frame.innerHTML("body");
            if (body.contains("numbers--2435c")) {
                return body;
            }
        }
        return null;
    }

    public boolean checkTimeout(Frame frameParent) {
        List<Frame> frameList = frameParent.childFrames().stream().filter(p -> p.url() != null && p.url().contains("evolutionlivecasino")).collect(Collectors.toList());
        for (Frame frame : frameList) {
            String body = frame.innerHTML("body");
            if (body.contains("OTURUMUN SÜRESİ DOLDU") || body.contains("Oturumunuz sonlandı") || body.contains("MASA KAPALI")) {
                return true;
            }
        }
        return false;
    }

}
