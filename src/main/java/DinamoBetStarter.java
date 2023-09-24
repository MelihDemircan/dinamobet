public class DinamoBetStarter {

    public static void main(String[] args) {
        System.out.println("Test");

        String domain = "https://www.dinamobet745.com";

        DinamoBetToken chromeToken = new DinamoBetToken(domain, domain + "/tr/game/everymatrix/live/play/47844030144#/");
        String token = chromeToken.run();

        System.out.println("Token : " + token);

        Thread thread = new Thread(new DinamobetRulet(domain + "/tr/game/everymatrix/live/play/47844030144#/", domain, token));

        Thread thread1 = new Thread(new DinamobetRulet(domain + "/tr/game/everymatrix/live/play/40892081196#/", domain, token));

        Thread thread2 = new Thread(new DinamobetRulet(domain + "/tr/game/everymatrix/live/play/8345030379#/", domain, token));

        Thread thread3 = new Thread(new DinamobetRulet(domain + "/tr/game/everymatrix/live/play/8510079294#/", domain, token));

        thread.start();
        thread1.start();
        thread2.start();
        thread3.start();


    }
}
