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
 * @author gabriel Pereira Antoniolli -- 2020352
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
        
        // just to make some nice effect of loading the next action.
        Thread.sleep(1000);

        Scanner internal = new Scanner(System.in);

        int counter = 0;
        /**
         * will iterate over all available users, and add them to the options list
         * if already there nothing happens.
         */
        for (String user : getUsers()) {
            counter++;
            map.put(counter, user);
            
            if(!options.contains(user)){
                options.add(user);
                displayItems.add(counter + "_ " + user);
            }
        }     
        
            /**
             * if there is only one person at the list it means there is no one else available
             * so we tell it to the user, he can then refresh the menu or go back to the main menu
             */
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
                
                /**
                 * if there is more users it will them display all available but the current one,
                 * because there is no point in showing to a user himself.
                 */
            } else if(getUsers().size() > 1){
                
                /**
                 * Display the user alternatives for the current client.
                 */
                for (String curr : displayItems) {
                    if (!curr.contains(name)) {
                        System.out.println(curr);
                    } 
                }
                /**
                 * it again can refresh the page, go back to main menu or select an user id.
                 */
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
                    
                    
                    /**
                     * This is when the user gets into the chat, as it can be seem is a constant loop that allows the client to send
                     * as many messages as he pleases to the other client, when he types 'exit' it them turns valid to false and 
                     * stops the loop
                     * it starts by sending CLIENT_MESSAGE_ command to the server to warn it that it will be sending chat messages.
                     */
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
                    /**
                     * once out of the chat, return to available users menu.
                     */
                    display();
    }
}
