/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide;

import serverSide.ClientConnection;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class WebSocketServer {
  

    public static ArrayList<String> currentUsers = new ArrayList<>();
    public static ArrayList<ClientConnection> connectedClients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
         ServerSocket server = new ServerSocket(8080);
    try {
        System.out.println("Server has started on 127.0.0.1:8080.\r\nWaiting for a connection…");
        while (true) {
            Socket client = server.accept();
            System.out.println("A client connected.");
            ClientConnection clientConnection = new ClientConnection(client);
            Thread newClient = new Thread(new ClientConnection(client));
            newClient.start();
            
        }
    } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
    }
    }
    
}
