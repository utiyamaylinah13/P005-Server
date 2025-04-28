package server;

import javax.swing.*;

public class ServerApp {
    public static void main(String[] args) {
        // Menampilkan UI Server
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserServer();  // Menampilkan frame UI Server
            }
        });
    }
}
