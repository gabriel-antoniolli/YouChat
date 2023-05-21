/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

import static clientSide.Client.getUsers;
import static clientSide.Client.setUsers;
import static clientSide.Client.users;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabri
 */
public class ClientReader implements Runnable {

   private Socket socket;
   private OutputStream out;
   public String name;
   private ArrayList<String> allUsers = new ArrayList<>();
   Chat chat;
   private HashMap<String,ArrayList<String>> chatHistory;
   Scanner sc;
   private ArrayList<String> messageHistory;
   
    public ClientReader(Socket socket, OutputStream out, HashMap<String,ArrayList<String>> chatHistory, Scanner sc) {
        this.messageHistory = new ArrayList<String>();
        this.socket = socket;
        this.out = out;
        this.chatHistory = chatHistory;
        this.sc = sc;
    }
    

    @Override
    public void run() {
        
        try {
            InputStream in = socket.getInputStream();
            Scanner scanner = new Scanner(in).useDelimiter("\n");
            Scanner internal = new Scanner(System.in);
            
            while (true) {
                String message = scanner.nextLine();
                if(message.equals("CURRENT_USERS")){
                    String usersList = scanner.nextLine();
                   
                    //Server Solution of Naming System
                    if(usersList.contains(",")){
                        String[] users = usersList.split(",");
                        
                        for(int i = 0; i < users.length; i++){
                            if(!allUsers.contains(users[i])){
                                allUsers.add(users[i]);
                            }
                        }
                    } else {
                            if(!allUsers.contains(usersList)){
                                allUsers.add(usersList);
                            }
                    }
                    setUsers(allUsers);
                }
                if(message.equals("PREPARE_CHAT")){
                   String from = scanner.nextLine();
                   try{
                    chat = new Chat();
                   }catch(NullPointerException e){
                       System.out.println("Opening Chat");
                   }
                   System.out.println("PREPARING CHAT...");
                   System.out.println("Press 'ENTER' to enter the Chat");
                   String msg = "FROM_" + from + "\n";
                   out.write(msg.getBytes());
                   out.flush();
                   //Resets the array for a new chat
                   messageHistory = new ArrayList();

                }
                if(message.equals("GET_DETAILS")){

                    String from = scanner.nextLine();
                    from += "\n";
                    out.write(from.getBytes());
                    out.flush();
                }
                if(message.equals("CLIENT_MESSAGE")){
                   String msg = scanner.nextLine();
                   messageHistory.add(msg);
                   chat.appendMessage(msg);
                   if(msg.contains("'exit'")){
                       chat.exitChat();
                       out.write("EXIT_CHAT\n".getBytes());
                       out.flush();
                   }
                }
                if(message.equals("SAVE_CHAT")){
                    String from = scanner.nextLine();
                    chatHistory.put(from,messageHistory);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
