import java.io.IOException;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        String host = "192.168.115.153";
        int puerto = 5555;
        Zerbitzaria zerbitzaria = new Zerbitzaria(host, puerto);

        try {
            zerbitzaria.hasi();

            while (zerbitzaria.konektatuta()) {
                Socket bezeroarenSocketa = zerbitzaria.onartuKonexioa();
                Bezeroa bezero = new Bezeroa(bezeroarenSocketa);
                zerbitzaria.gehituBezeroa(bezero);

                Thread konexioa = new Thread(() -> {
                    try {
                        String mezua;
                        while ((mezua = bezero.getIn().readLine()) != null) {
                            System.out.println("Mezua jaso da: " + mezua);
                            zerbitzaria.bidaliMezuaDenei(mezua, bezero);
                        }
                    } catch (IOException e) {
                        System.err.println("Errorea bezeroaren komunikazioan: " + e.getMessage());
                    } finally {
                        try {
                            bezero.cerrar();
                            zerbitzaria.getBezeroak().remove(bezero);
                            System.out.println("Bezeroa deskonektatu da: " + bezero.getSocket().getInetAddress());
                        } catch (IOException e) {
                            System.err.println("Errorea bezeroa ixtean: " + e.getMessage());
                        }
                    }
                });

                konexioa.start();
            }
        } catch (IOException ioe) {
            System.err.println("Errorea zerbitzaria abiarazteko: " + ioe.getMessage());
        } finally {
            try {
                zerbitzaria.itxi();
            } catch (IOException e) {
                System.err.println("Errorea zerbitzaria ixtean: " + e.getMessage());
            }
        }
    }
}

