package com.detect;

public class ChromeDetection {

    public static void main(String[] args) throws InterruptedException {
        String domain = "https://www.dinamobet745.com";

        ChromeToken chromeToken = new ChromeToken(domain, domain + "/tr/game/everymatrix/live/play/47844030144#/");
        String token = chromeToken.run();

        System.out.println("Token : " + token);

        ChromeRunnable myRunnable = new ChromeRunnable(domain + "/tr/game/everymatrix/live/play/47844030144#/", domain, token);
        Thread thread = new Thread(myRunnable);

        ChromeRunnable myRunnable1 = new ChromeRunnable(domain + "/tr/game/everymatrix/live/play/40892081196#/", domain, token);
        Thread thread1 = new Thread(myRunnable1);

        ChromeRunnable myRunnable2 = new ChromeRunnable(domain + "/tr/game/everymatrix/live/play/8345030379#/", domain, token);
        Thread thread2 = new Thread(myRunnable2);

        ChromeRunnable myRunnable3 = new ChromeRunnable(domain + "/tr/game/everymatrix/live/play/8510079294#/", domain, token);
        Thread thread3 = new Thread(myRunnable3);

        //thread.start();
        //thread1.start();
        //thread2.start();
        //thread3.start();

        //new Thread(new ChromeRecentRunnable()).start();

    }


}
