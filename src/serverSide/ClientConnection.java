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
import java.util.logging.Level;
import java.util.logging.Logger;
import static serverSide.WebSocketServer.connectedClients;
import static serverSide.WebSocketServer.currentUsers;

/**
 *
 * @author gabri
 */
public class ClientConnection implements Runnable {

    Socket client;
    public String name;

    /**
     *
     */
    public OutputStream out;

    public ClientConnection(Socket client) throws IOException {
        this.client = client;
        this.out = client.getOutputStream();
    }

    @Override
    public void run() {

        try {
            InputStream in = client.getInputStream();
            out = client.getOutputStream();
            Scanner scanner = new Scanner(in).useDelimiter("\\n");
    
    // Insert Name Section        
            out.write("INSERT_NAME\n".getBytes());
            out.flush();
            name = scanner.nextLine();
            
            
    // Join Server Section
            /**
             * Adding the name of the current user to the list of available
             * users.
             */
            connectedClients.add(this);
            out.write("JOIN_SERVER\n".getBytes());
            out.flush();
            System.out.println(name + " connected.");
            
            
            
//    // Listener
//            while (true) {
//                
//                String data = scanner.nextLine();
//                if(data.contains("name:")){
//                    //getting only the name of the string that comes with name:
//                    if(name.contains("name:")) name = name.substring(name.indexOf(":"));
//                    System.out.println("the Server received the name" + name);
//                }
//                if (data.equals("name:")) insertName();
//            }
            
    // Exceptions
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        }
    }
}

//if(userChoice.equals("r")){
//                run();
//            } else {
//                int userID = Integer.parseInt(userChoice);
//                sendRequest(userID - 1);
//                String msg = "An Invitation was sent to " + currentUsers.get(Integer.parseInt(userChoice) - 1) + "\n";
//                out.write(msg.getBytes());
//                out.write("Please wait until the request is accepted or press '0' to go back. \n".getBytes());
//                // Dont forget to fix it
//                out.flush();
//                
//            }