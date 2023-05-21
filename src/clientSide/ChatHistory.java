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

/**
 *
 * @author gabri
 */
public class ChatHistory {
    
    private HashMap<String,ArrayList<String>> chatHistory;
    private Scanner sc;
    private OutputStream out;
    private String name;
    
    public ChatHistory(HashMap<String,ArrayList<String>> chatHistory, Scanner sc, OutputStream out, String name){
        this.chatHistory = chatHistory;
        this.sc = sc;
        this.out = out;
        this.name = name;
    }
    
    public void displayOptions(){
    
        /**
         * warn user that has no chat history yet, and offers options to go back, refresh or exit application.
         */
        if(chatHistory.size() == 0){
            System.out.println("This user has not had a chat with another user yet! please have a chat and come back here later!");
            System.out.println("Press 'r' to refresh, 'b' to go back to Main Menu or type 'x' to exit");
            String decision= sc.nextLine();
            if(decision.equals("b")){
                
                MenuInteraction menu = new MenuInteraction(out,sc,chatHistory);
                menu.setName(name);
                menu.display();
            } else if(decision.equals('r')){
                
                displayOptions();
            } else{
                System.exit(0);
            }
            
            /**
             * if there is chat history, all the user has to do is to type the name which is the key to open the conversation.
             * Again the user has always the option to go back to main menu
             */
        } else {
            System.out.println("Select type the name of the person of the conversation you want to review");
            System.out.println("Options:\n");
            
            for(String name : chatHistory.keySet()){
                System.out.println("_ " + name);
            }
            System.out.println("\nOr press 'b' to go back to main menu");
            try{
                String decision = sc.nextLine();
                if(decision.equals("b")){
                   MenuInteraction menu = new MenuInteraction(out,sc,chatHistory);
                   menu.setName(name);
                   menu.display();
                }else{
                    // if user selected a key, meaning decision is not false.
                    if(chatHistory.get(decision).isEmpty() == false){

                        /**
                         * displaying the chat messages to the user of the related key, the user can then leave by typing any key of the keyboard.
                         */
                        chatHistory.get(decision).forEach(el -> System.out.println(el));
                        System.out.println("\n Press any key to leave");
                        String decision2 = sc.nextLine();
                        if(decision2.matches("[a-zA-Z0-9]+") || decision2.isEmpty()){
                            displayOptions();
                        } else {
                            System.out.println("command not found");
                            System.exit(0);
                        }
                    } else {
                        System.out.println("sorry conversation not found, try again");
                        displayOptions();
                    }
                }
            }catch(Exception ex){
                System.out.println("Command not acknowledge, please try again");
                displayOptions();
            }
        }
    }
    
}
