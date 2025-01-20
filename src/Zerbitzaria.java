import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Zerbitzaria {
    private String host;
    private int puerto;
    private ServerSocket serverSocket;
    private List<Bezeroa> bezeroak; // Lista de clientes conectados
    private List<String> historialMensajes; // Historial de mensajes enviados

    public Zerbitzaria(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
        this.bezeroak = new ArrayList<>();
        this.historialMensajes = new ArrayList<>(); // Inicializamos el historial
    }

    public void hasi() throws IOException {
        this.serverSocket = new ServerSocket(puerto, 50, InetAddress.getByName(host));
        System.out.println("Servidor escuchando en " + host + ":" + puerto);
    }

    public boolean konektatuta() {
        return serverSocket != null && !serverSocket.isClosed();
    }

    public Socket onartuKonexioa() throws IOException {
        return serverSocket.accept(); // Acepta una nueva conexión de cliente
    }

    public void itxi() throws IOException {
        if (serverSocket != null) {
            serverSocket.close(); // Cierra el servidor
        }
    }

    public void gehituBezeroa(Bezeroa bezero) {
        bezeroak.add(bezero);
        System.out.println("Nuevo cliente conectado: " + bezero.getSocket().getInetAddress());

        // Enviar el historial de mensajes al cliente recién conectado
        enviarHistorial(bezero);
    }

    public List<Bezeroa> getBezeroak() {
        return bezeroak; // Devuelve la lista de clientes conectados
    }

    public void agregarMensajeAlHistorial(String mensaje) {
        historialMensajes.add(mensaje); // Almacena el mensaje en el historial
    }

    public void enviarHistorial(Bezeroa bezero) {
        try {
            for (String mensaje : historialMensajes) {
                bezero.enviarMensaje(mensaje); // Envía cada mensaje al cliente
            }
        } catch (IOException e) {
            System.err.println("Error al enviar el historial al cliente: " + e.getMessage());
        }
    }

    public void bidaliMezuaDenei(String mezua, Bezeroa emea) {
        agregarMensajeAlHistorial(mezua); // Almacena el mensaje en el historial

        System.out.println("Enviando mensaje a todos los clientes: " + mezua);
        for (Bezeroa bezero : bezeroak) {
            if (bezero != emea) { // Aseguramos que no se envíe el mensaje al cliente que lo envió
                try {
                    bezero.enviarMensaje(mezua);
                } catch (IOException e) {
                    System.err.println("Error al enviar mensaje al cliente " + bezero.getSocket().getInetAddress() + ": " + e.getMessage());
                }
            }
        }
    }
}
