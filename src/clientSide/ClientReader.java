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
 * @author gabriel Pereira Antoniolli -- 2020352
 * 
 * Perhaps one of the most components of the application
 * this class is listening all the server responses and from here most of the actions of
 * the clients are taken.
 */
public class ClientReader implements Runnable {

    /**
     * Again, it is important to bring all the properties of the user, because when dealing with threads
     * some unexpected behaviour may occur and the client might not get the value it is awaiting for
     */
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
            /**
             * Here two scanners are needed.
             * 1_ for reading messages of the server
             * 2_ for reading inputs of the client user.
             */
            Scanner scanner = new Scanner(in).useDelimiter("\n");
            Scanner internal = new Scanner(System.in);
            
            // as long as the client is running this loop will always be running
            // listening to server commands.
            while (true) {
                String message = scanner.nextLine();
                
                /**
                 * same approach as with the server, once certain command arrives executes
                 * the specific block of code for it
                 */
                
                /**
                 * when command is CURRENT_USERS it will read a string with all available users in the server
                 * as it is a string it has to be processed, we first split it, assign into an array
                 * then loop through the array and add the elements to an ArrayList. this is because I prefer
                 * to work with lists.
                 */
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
                    //Static method coming from the Client class updating the 'users' variable.
                    // in other words, any time a new client is added, it updates that variable.
                    setUsers(allUsers);
                }
                /***
                 * When a client request a chat with another client the server will send this command
                 * PREPARE_CHAT, which starts the chat GUI and notify the server to update the FROM
                 * variable for the other user, as i said in the clientConnection class (this is a workaround)
                 */
                if(message.equals("PREPARE_CHAT")){
                   String from = scanner.nextLine();
                   /**
                    * by the way, because it is 2 chat it opens two chat objects,
                    * which is exactly what should happen, because ideally it will not be
                    * running in only one machine, but the fact that they load at the same time was giving me
                    * a weird error sometimes, so this try-catch is an attempt to prevent it;
                    */
                   try{
                    chat = new Chat();
                    
                   }catch(NullPointerException e){
                       System.out.println("Opening Chat");
                   }
                   System.out.println("PREPARING CHAT...");
                   System.out.println("Press 'ENTER' to enter the Chat or type 'exit' to exit (With Single Quotes)");
                   String msg = "FROM_" + from + "\n";
                   out.write(msg.getBytes());
                   out.flush();
                   //Resets the List for a new chat
                   messageHistory = new ArrayList();

                }
                
                /**
                 * all the chat messages are going to arrive here in this block
                 * first thing we read them, store them in the messageHistory list
                 * append it to the chat, if the message is equals 'exit' the leave the chat
                 * if exits the chat warns server that it is leaving the server then tell to save the chat
                 */
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
                /**
                 * this is the save the chat block of code, it saves in a map with the key being the 
                 * person the client was speaking with and the value is the list of messages.
                 */
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
