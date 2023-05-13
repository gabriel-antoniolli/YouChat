/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;
import static serverSide.WebSocketServer.currentUsers;

/**
 *
 * @author gabri
 */
public class MenuInteraction {
    
    OutputStream out;
    public String name;
    Scanner scanner;
    
    public MenuInteraction(OutputStream out, Scanner scanner, String name){
        this.out = out;
        this.scanner = scanner;
        this.name = name;
    }
    
    public void interact() throws IOException{
            
            out.write("Here is a list of available users.\n".getBytes());
            int count = 0;
            for (String user : currentUsers) {
                count++;
                String msg = "";
                if (user == name) {
                    msg = count + "_ " + user + "(Yourself) \n";
                } else {
                    msg = count + "_ " + user + "\n";
                }
                out.write(msg.getBytes());
            }
            out.flush();
            out.write("Please select the ID of the user you want to chat with or insert 'r' to refresh:\n".getBytes());
            String userChoice = scanner.next();
            if(userChoice.equals("r")){
                interact();
            } else {
                String msg = "Opening Chat with " + currentUsers.get(Integer.parseInt(userChoice) - 1) + "\n";
                out.write(msg.getBytes());
                out.flush();
            }
    }
    
}
