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
    private Scanner sc;
    private HashMap<String,ArrayList<String>> chatHistory;
    boolean valid = true;

    public MenuItem1(OutputStream out, String name, Scanner sc, HashMap<String,ArrayList<String>> chatHistory) {
        this.displayItems = new ArrayList<>();
        this.map = new HashMap<>();
        this.options = new ArrayList<>();
        this.out = out;
        this.name = name;
        this.sc = sc;
        this.chatHistory = chatHistory;
        this.valid = true;

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

        int counter = 0;
        
        for (String user : getUsers()) {
            counter++;
            map.put(counter, user);
            
            if(!options.contains(user)){
                options.add(user);
                displayItems.add(counter + "_ " + user);
            }
        }     
        
            if (getUsers().size() == 1) {
                System.out.println("No Users Available yet!\n");
                System.out.println("Please type 'r' to refresh the list or 'b' to go back to main menu");
                String decision = internal.nextLine();
                if(!decision.trim().isEmpty()){
                    if (decision.equals("r")) {
                        out.write("DECISION_1\n".getBytes());
                        display();

                    }else if(decision.equals("b")){
                        MenuInteraction menu = new MenuInteraction(out,sc,chatHistory);
                        menu.setName(name);
                        menu.display();
                    
                    } else {
                        // Maybe go back to previous menu would be good
                        System.out.println("Command not recognized, leaving application");
                        System.exit(0);
                    }
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
                System.out.println("Or type 'b' to go back to Main Menu");
                String decision = internal.nextLine();
                if(!decision.trim().isEmpty()){
                    if(decision.equals("r")){
                        out.write("DECISION_1\n".getBytes());
                        display();
                    } else if(decision.equals("b")){
                        MenuInteraction menu = new MenuInteraction(out,sc,chatHistory);
                        menu.setName(name);
                        menu.display();
                    
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
                    } else {
                        System.out.println("command invalid please try again");
                        display();
                    }
                }
            }
                    
                    Thread.sleep(2000);
                    
                    
                    
                    while(valid){
                        
                        System.out.print(name + "> ");
                        String msg = internal.nextLine();
                        if(msg.equals("'exit'")){
                            valid = false;
                        }
                        String serverMessage = "CLIENT_MESSAGE_" + name +"\n";
                        out.write(serverMessage.getBytes());
                        out.flush();
                        msg += "\n";
                        out.write(msg.getBytes());
                        out.flush();
                        
                    }
                    display();
    }
}
