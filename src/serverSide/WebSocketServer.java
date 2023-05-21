/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide;

/**
 * @author gabriel Pereira Antoniolli -- 2020352
 */

import serverSide.ClientConnection;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WebSocketServer {
  
    //Note that the next Two variable come from the concurrent library of java.util.
    
    //This Variable is responsible for storing the name of the current clients once they input it.
    // it might not seem that important but the name is used a lot of times throughout the code.
    public static ConcurrentLinkedQueue<String> currentUsers = new ConcurrentLinkedQueue<>();
    
    // This Map is responsible to map the Name of the user with the connection(Socket) that he/she is using.
    public static ConcurrentHashMap<String,Socket> connectedClients = new ConcurrentHashMap<>();;


    public static void main(String[] args) throws IOException {
        // initialize server.
        ServerSocket server = new ServerSocket(8080);
        try {
            System.out.println("Server has started on 127.0.0.1:8080.\r\nWaiting for a connection…");
            
            // Will always be running awaiting for new clients to join.
            while (true) {
                Socket client = server.accept();
                System.out.println("A client connected.");
                
                /**
                 * Every new Client executes a new ClientConnection thread
                 * which is basically a constant loop listening to whatever messages that client sends.
                 */
                Thread newClient = new Thread(new ClientConnection(client));
                newClient.start();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    
    /**
     * These methods avoid race conditions on the currentUser List.
     */
    public static void addUser(String name){
        synchronized(currentUsers){
            currentUsers.add(name);
            System.out.println(name);
        }
    }
    public static ArrayList<String> getCurrentUsers(){
        synchronized(currentUsers){
            return new ArrayList<String>(currentUsers);
        }
    }
    
}
