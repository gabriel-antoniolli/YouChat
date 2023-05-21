/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author gabri
 */
public class ChatHistory {
    
    private HashMap<String,ArrayList<String>> chatHistory;
    
    public ChatHistory(HashMap<String,ArrayList<String>> chatHistory){
        this.chatHistory = chatHistory;
    }
    
    public void displayOptions(){
    
        if(chatHistory.size() == 0){
            System.out.println("This user has not had a chat with another user yet! please have a chat and come back here later!");
        } else {
            System.out.println("Select");
        }
    }
    
}
