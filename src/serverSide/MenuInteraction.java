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
    
    public MenuInteraction(OutputStream out, Scanner scanner){
        this.out = out;
        this.scanner = scanner;
    }
    
    public void interact() throws IOException{
            out.write("Welcome to YouChat\n".getBytes());
            out.write("Please Insert your name: \n".getBytes());
            out.flush();
            
            name = scanner.nextLine();

            /**
             * Adding the name of the current user to the list of available
             * users.
             */
            currentUsers.add(name);
            out.write("Congratulations, You Have Joined the server. \n".getBytes());
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
            out.write("Please select the ID of the user you want to chat with:\n".getBytes());
            int userChoice = scanner.nextInt();
            String msg = "Opening Chat with " + currentUsers.get(userChoice - 1) + "\n";
            out.write(msg.getBytes());
            out.flush();
    }
    
}
