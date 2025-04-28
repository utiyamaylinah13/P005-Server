package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class server extends Thread {
    private ServerSocket serverSocket;
    private HashMap<String, PrintWriter> clients;
    private boolean running;
    private String serverIp;
    private int port;

    public server(String serverIp, int port) {
        this.serverIp = serverIp;
        this.port = port;
        this.clients = new HashMap<>();
        this.running = true;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port, 50, InetAddress.getByName(serverIp));
            System.out.println("Server started on " + serverIp + ":" + port);

            while (running) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            if (running) {
                e.printStackTrace();
            }
        }
    }

    // Stop the server
    public void stopServer() {
        try {
            running = false;
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("Server stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Handle login or registration
                out.println("Enter username: ");
                clientName = in.readLine();
                out.println("Welcome, " + clientName);

                synchronized (clients) {
                    clients.put(clientName, out);
                }

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("exit")) {
                        break;
                    }

                    // Broadcast to all clients
                    synchronized (clients) {
                        for (PrintWriter writer : clients.values()) {
                            writer.println(clientName + ": " + message);
                        }
                    }
                }

                // Remove client when disconnected
                synchronized (clients) {
                    clients.remove(clientName);
                }
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
