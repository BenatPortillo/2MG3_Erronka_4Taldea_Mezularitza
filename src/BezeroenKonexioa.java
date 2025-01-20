import java.io.IOException;

public class BezeroenKonexioa extends Thread {

    private final Bezeroa bezero;
    private final Zerbitzaria zerbitzaria;

    public BezeroenKonexioa(Bezeroa bezero, Zerbitzaria zerbitzaria) {
        this.bezero = bezero;
        this.zerbitzaria = zerbitzaria;
    }

    @Override
    public void run() {
        bezero.getOut().println("Kaixo, ongi etorri gure mezularitza zerbitzura!");
        try {
            String mezua;
            while (bezero.konektatutaDago() && (mezua = bezero.getIn().readLine()) != null) {
                System.out.println("Mensaje recibido del cliente: " + mezua); // Depuración para ver el mensaje recibido
                // Enviar el mensaje a todos los clientes
                zerbitzaria.bidaliMezuaDenei(mezua, bezero);
            }
        } catch (IOException ioe) {
            System.err.println("Errorea bezeroarekin komunikatzean: " + ioe.getMessage());
        } finally {
            try {
                bezero.cerrar(); // Cerramos la conexión
            } catch (IOException e) {
                System.err.println("Errorea bezeroa ixtean: " + e.getMessage());
            }
        }
    }

}
