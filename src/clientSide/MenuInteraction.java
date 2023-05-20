/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

import java.io.IOException;
import java.io.OutputStream;
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
    Scanner serverScanner;

    public MenuInteraction(OutputStream out, Scanner sc, Scanner serverScanner) {
        this.sc = sc;
        this.out = out;
        this.serverScanner = serverScanner;
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
               
               MenuItem1 item1 =  new MenuItem1(out,name);
               item1.display();
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
//        try {
//            out.write("Here is a list of available users.\n".getBytes());
//            int count = 0;
//            for (String user : currentUsers) {
//                count++;
//                String msg = "";
//                if (user == name && currentUsers.size() == 1) {
//                    msg = "No Users Available yet!\n";
//                    out.write(msg.getBytes());
//                    out.write("Please insert 'r' to refresh the list\n".getBytes());
//                } else {
//                    if (user != name) {
//                        msg = count + "_ " + user + "\n";
//                        out.write(msg.getBytes());
//                        out.write("Please select the ID of the user you want to chat with\n".getBytes());
//                    }
//                }
//            }
//            out.flush();
//
//        } catch (IOException ex) {
//            Logger.getLogger(MenuInteraction.class.getName()).log(Level.SEVERE, null, ex);
//        }
//private void sendRequest(int userID) throws IOException {
//        ClientConnection targetClient = connectedClients.get(userID);
//        String msg = name + " is inviting you for a chat, would you like to accept? [ y / n ]\n";
//        targetClient.out.write(msg.getBytes());
//        targetClient.out.write("INVITATION_REQUEST\n".getBytes());
//        targetClient.out.flush();
}
