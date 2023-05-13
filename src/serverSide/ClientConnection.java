/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author gabri
 */
public class ClientConnection implements Runnable {
    
    Socket client;
    public String name;
    
    public ClientConnection(Socket client){
        this.client = client;
    }
    
    @Override
    public void run() {
       
        try {
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();
            out.write("Welcome to YouChat\n".getBytes());
            out.write("Please Insert your name: \n".getBytes());
            out.flush();
            Scanner scanner = new Scanner(in).useDelimiter("\\n");
            name = scanner.nextLine();
            out.write("Congratulations, You Have Joined the server. \n".getBytes());
            out.write("Here is a list of available users.\n".getBytes());
            out.flush();
            
            // Use the name to identify the client in future messages
            System.out.println(name + " connected.");
            while (true) {
                
                String data = scanner.nextLine();
                System.out.println(name + "> " + data);
                // handle incoming message from client
            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        }
    }
}
