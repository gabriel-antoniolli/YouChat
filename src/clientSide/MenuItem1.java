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
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author gabri
 */
public class MenuItem1 {

    private String name;
    private OutputStream out;
    private HashMap<Integer, String> map;
    private ArrayList<String> options;
    private ArrayList<String> displayItems;

    public MenuItem1(OutputStream out, String name) {
        this.displayItems = new ArrayList<>();
        this.map = new HashMap<>();
        this.options = new ArrayList<>();
        this.out = out;
        this.name = name;

    }

    public void display() throws IOException, InterruptedException {

        System.out.println("Please select the ID of the user you want to chat with or insert 'r' to refresh the list:");
        /**
         * 1_ Send Request to server to get a list of users 2_ Server reads
         * request and send back the list of users separated by comma 3_ split
         * users in comma and add them to menu display.
         */
        Thread.sleep(1000);

        Scanner internal = new Scanner(System.in);

        // Checking My Get Users variable content
        //getUsers().iterator().forEachRemaining(el -> System.out.println(el));
        int counter = 0;
        
        for (String user : getUsers()) {
            counter++;
            boolean valid = true;
            map.put(counter, user);
            
            if(!options.contains(user)){
                options.add(user);
                displayItems.add(counter + "_ " + user);
            }
            
        }     
        
            if (getUsers().size() == 1) {
                System.out.println("No Users Available yet!\n");
                System.out.println("Please insert 'r' to refresh the list");

                if (internal.nextLine().equals("r")) {
                    out.write("DECISION_1\n".getBytes());
                    display();

                } else {
                    // Maybe go back to previous menu would be good
                    System.out.println("Command not recognized, leaving application");
                    System.exit(0);
                }
                
            } else if(getUsers().size() > 1){
            
                /**
                 * Display the user alternatives for the current client.
                 */
                for (String curr : displayItems) {
                    if (!curr.contains(name)) {
                        System.out.println(curr);
                    } 
                }
                String decision = internal.nextLine();
                if(decision.equals("r")){
                    out.write("DECISION_1\n".getBytes());
                    display();
                } else if(decision.matches("[0-9]+")){
                    /*
                     * send message to server requesting that user
                     * server has to find a way of stopping all the process of the requested client and open chat
                    
                    */
                    
                    String serverComm = "CHAT_REQUEST\n" ;
                    String from = name + "\n";
                    String to = map.get(Integer.parseInt(decision)) + "\n";

                    out.write(serverComm.getBytes());
                    out.write(from.getBytes());
                    out.write(to.getBytes());
                    out.flush();
                }
                
                
            }

            
        

//            if (getUsers().size() == 1) {
//                
//
//                
//
//            } else {
//                String msg = "";
//                for (String user : getUsers()) {
//                    
//                    options.add(getUsers().size()+"_ " + user );
//                    if(!user.equals(name)){
//                        msg = getUsers().size() + "_ " + user;
//                        System.out.println(msg);
//                    }
//                }
//            
//                String decision = internal.nextLine();
//                if (decision.equals("r")) {
//                    out.write("DECISION_1\n".getBytes());
//                    display();
//                } else {
//                    if(decision.length() < 2 && decision.matches("[0-9]+")){
//
//                    }
//                }
//            
//            
//            
//            }
        //                 else {
        //                    System.out.println("Command not recognized, leaving application");
        //                    System.exit(0);
        //                }
    }
}
