package main.java.kocev.nenad.serialization;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static int SERVER_PORT = 8080;
    public static String SERVER_ADDRESS = "0.0.0.0";
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        Application application = new Application();
        Socket socket;
        while (true){
            socket = serverSocket.accept();
            ServerWorker serverWorker = new ServerWorker(socket, application);
            Thread thread = new Thread(serverWorker);
            thread.start();
        }
    }
}
