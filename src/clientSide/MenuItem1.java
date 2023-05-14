/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

import java.io.IOException;
import java.io.OutputStream;
import static serverSide.WebSocketServer.currentUsers;

/**
 *
 * @author gabri
 */
public class MenuItem1 {
    
    private String name;
    private OutputStream out;
    
    public MenuItem1(String name, OutputStream out){
        this.name = name;
        this.out = out;
    }

    public void display() throws IOException {
        //String msgToServer = name + "> \n";
        //out.write(msgToServer.getBytes());
        int count = 0;
        System.out.println("Please select the ID of the user you want to chat with:");
            for (String user : currentUsers) {
                count++;
                String msg = "";
                if (user == name && currentUsers.size() == 1) {
                    System.out.println("No Users Available yet!");
                    System.out.println("Please insert 'r' to refresh the list");

                } else {
                    if (user != name) {
                        msg = count + "_ " + user;

                        System.out.println(msg);
                    }
                }
            }
    }
}
