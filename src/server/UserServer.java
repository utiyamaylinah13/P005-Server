package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class UserServer extends JFrame {
    private JTextArea textArea;
    private JButton startButton;
    private JButton stopButton;
    private JLabel statusLabel;
    private server server;

    public UserServer() {
        setTitle("Server Configuration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        statusLabel = new JLabel("Server not started");
        panel.add(statusLabel, BorderLayout.NORTH);

        startButton = new JButton("Start Server");
        stopButton = new JButton("Stop Server");
        stopButton.setEnabled(false);  // Disable Stop button initially

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel, BorderLayout.SOUTH);

        // Action for the start server button
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });

        // Action for the stop server button
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        });

        setVisible(true);
    }

    private void startServer() {
        String serverIp = "127.0.0.1"; // IP server statis
        int port = 8080; // Menggunakan port 8080 yang umum digunakan

        textArea.append("Starting server on IP: " + serverIp + " and Port: " + port + "\n");

        // Start the server thread
        server = new server(serverIp, port);
        server.start();

        statusLabel.setText("Server is running...");
        startButton.setEnabled(false);  // Disable Start button after start
        stopButton.setEnabled(true);    // Enable Stop button after start
    }

    private void stopServer() {
        server.stopServer();  // Stop the server
        statusLabel.setText("Server stopped");
        startButton.setEnabled(true);  // Enable Start button again
        stopButton.setEnabled(false); // Disable Stop button after stop
    }
}
