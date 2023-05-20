/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

import static clientSide.Client.getUsers;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author gabri
 */
public class MenuItem1 {

    private String name;
    private OutputStream out;
    private ArrayList<String> users;

    public MenuItem1(OutputStream out, String name) {
        this.out = out;
        this.name = name;

    }

    public void display() throws IOException, InterruptedException {

        int count = 0;
        System.out.println("Please select the ID of the user you want to chat with or insert 'r' to refresh the list:");
        /**
         * 1_ Send Request to server to get a list of users 2_ Server reads
         * request and send back the list of users separated by comma 3_ split
         * users in comma and add them to menu display.
         */
        Thread.sleep(1000);

        Scanner internal = new Scanner(System.in);
        System.out.println(getUsers().size());
        

            if (getUsers().size() == 1) {
                System.out.println("No Users Available yet!\n");
                System.out.println("Please insert 'r' to refresh the list");

                if (internal.nextLine().equals("r")) {
                    //availableUsers.remove(user);
                    out.write("DECISION_1\n".getBytes());
                    display();
                } else {
                    System.out.println("Command not recognized, leaving application");
                    System.exit(0);
                }

            } else {
                for (String user : getUsers()) {
                    String msg = "";
                    
                    if(!user.equals(name)){
                        msg = count + "_ " + user;
                        System.out.println(msg);
                    }
                }
            
                String decision = internal.nextLine();
                if (decision.equals("r")) {
                    out.write("DECISION_1\n".getBytes());
                    display();
                } else {
                    if(decision.length() < 2 && decision.matches("[0-9]+")){

                    }
                }
            
            
            
            }
    //                 else {
    //                    System.out.println("Command not recognized, leaving application");
    //                    System.exit(0);
    //                }

            
        
    }
}
