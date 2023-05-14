/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import static serverSide.WebSocketServer.currentUsers;

/**
 *
 * @author gabri
 */
public class ClientReader implements Runnable {

   private Socket socket;
   private OutputStream out;
   public String name;
   
    public ClientReader(Socket socket, OutputStream out) {
        this.socket = socket;
        this.out = out;
    }

    @Override
    public void run() {
        
        try {
            
            InputStream in = socket.getInputStream();
            Scanner scanner = new Scanner(in).useDelimiter("\n");
            Scanner internal = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                System.out.println(message + " CURRENT MESSAGE"); // Can delete this later.
                
                if(message.equals("INSERT_NAME")){
                    System.out.println("Please insert your name:");
                    name = internal.nextLine();
                    name = name + "\n";
                    out.write(name.getBytes());
                    out.flush();
                    currentUsers.add(name);
                    
                }
                if(message.equals("JOIN_SERVER")){
                    System.out.println(currentUsers.size() + " HEERRREEEE");
                    Thread menu = new Thread(new MenuInteraction(out,name));
                    menu.start();
                    String msg = name + "> " + "Menu has been displayed\n";
                    out.write(msg.getBytes());
                
                
                }
                if(message.equals("INVITATION_REQUEST")){
                    
                    System.out.println(message + "1");
                    String decision = scanner.next();
                    System.out.println("HERE IS THE DECISION TEST: " + decision);
                    
                    System.out.println(decision);
                    if(decision == "y"){
                        System.out.println("it doesnt get here");
                        //do something
                        
                        out.write("Requested Accepted\n".getBytes());
                        out.flush();
                        System.out.println("decision");
                    } else if(decision == "n") {
                        // dont do something
                    } else {
                        //deny also;
                    }
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
