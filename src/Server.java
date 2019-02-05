import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server implements Runnable {

    private Thread thread;
    private ServerSocket serverSocket;

    Server() {
        thread = new Thread(this);
        thread.setName("Server Thread");
        thread.setPriority(Thread.MAX_PRIORITY);
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(5006);
            while (!thread.isInterrupted()) {
                new Handler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            /*System.out.println(e.getMessage());
            Main.appendToPane(Main.logsText, "[-] Error in Starting server\n", Color.red);*/
        }
    }

    void stopServer() {
        thread.interrupt();
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("ERORR");
        }

    }

    boolean startServer() {
        thread.start();
        return thread.isAlive();
    }

}