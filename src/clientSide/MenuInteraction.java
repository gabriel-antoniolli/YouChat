/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import static serverSide.WebSocketServer.connectedClients;
import static serverSide.WebSocketServer.currentUsers;

/**
 *
 * @author gabri
 */
public class MenuInteraction  {

    public String name;
    private OutputStream out;
    Scanner sc;
    private HashMap<String,ArrayList<String>> chatHistory;

    public MenuInteraction(OutputStream out, Scanner sc, HashMap<String,ArrayList<String>> chatHistory) {
        this.sc = sc;
        this.out = out;
        this.chatHistory = chatHistory;
    }
    
    public synchronized void setName(String name){
        this.name = name;
    }

    public synchronized void display() {
    
        try{
            System.out.println("Main Menu\n");
            System.out.println("Please Select an Option: [numbers only]");
            System.out.println("1_ Check Available People");
            System.out.println("2_ Chat History");
            System.out.println("3_ Exit");

            int decision = sc.nextInt();
            if(decision == 1){
               out.write("DECISION_1\n".getBytes());
               
               MenuItem1 item1 =  new MenuItem1(out,name,sc, chatHistory);
               item1.display();
            } else if(decision == 2){
                //chat history menu?
                ChatHistory history = new ChatHistory(chatHistory,sc,out,name);
                history.displayOptions();
            
            } else if(decision == 3) {
                System.out.println("Thanks for using our app!");
                System.exit(0);
            } else {
                System.out.println("Command not acknowledge try again");
                display();
            }
            
        }catch(Exception e){
                
            String msg = e.getMessage() + "\n";
            try {
                out.write(msg.getBytes());
            } catch (IOException ex) {
                Logger.getLogger(MenuInteraction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  
}
