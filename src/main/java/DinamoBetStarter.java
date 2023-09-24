public class DinamoBetStarter {

    public static void main(String[] args) {
        System.out.println("Test");

        String domain = "https://www.dinamobet745.com";

        DinamoBetToken chromeToken = new DinamoBetToken(domain, domain + "/tr/game/everymatrix/live/play/47844030144#/");
        String token = chromeToken.run();

        System.out.println("Token : " + token);

    }
}
