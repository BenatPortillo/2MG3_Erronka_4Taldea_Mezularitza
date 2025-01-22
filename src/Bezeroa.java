import java.io.*;
import java.net.Socket;

public class Bezeroa {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public Bezeroa(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void cerrar() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null && !socket.isClosed()) socket.close();
    }

    public boolean konektatutaDago() {
        return socket != null && !socket.isClosed();
    }

    public Socket getSocket() {
        return socket;
    }

    public void enviarMensaje(String mensaje) throws IOException {
        if (out != null) {
            out.println(mensaje);
            out.flush();
        } else {
            throw new IOException("Flujo de salida no disponible para el cliente.");
        }
    }
}

