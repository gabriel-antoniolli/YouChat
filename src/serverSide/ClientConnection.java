/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide;

import clientSide.Chat;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static serverSide.WebSocketServer.addUser;
import static serverSide.WebSocketServer.connectedClients;
import static serverSide.WebSocketServer.currentUsers;

/**
 *
 * @author gabri
 */
public class ClientConnection implements Runnable {

    Socket client;
    public String name;
    OutputStream targetClientOut;
    InputStream targetClientIn;
    Scanner targetClientReader;
    Chat chat;

    /**
     *
     */
    public OutputStream out;

    public ClientConnection(Socket client) throws IOException {
        this.client = client;
        this.out = client.getOutputStream();
    }

    @Override
    public void run() {
        try {
            InputStream in = client.getInputStream();
            out = client.getOutputStream();
            Scanner scanner = new Scanner(in).useDelimiter("\\n");
    
            while(true){
                String userRequest = scanner.nextLine();
                
                if(userRequest.equals("INSERT_NAME")){
                    String temp = name = scanner.nextLine();
                    addUser(temp);
                    connectedClients.put(name, client);
                    System.out.println("User added to the server: " + name);
                    System.out.println("Amount of users connected is:" + currentUsers.size());
                }
                if(userRequest.equals("JOIN_SERVER")){
                    System.out.println(name + " has joined the server");
                }
                if(userRequest.equals("DECISION_1")){
                    
                    String userList = String.join(",", currentUsers);
                    userList += "\n";
                        
                        
                    String clientMessage = "CURRENT_USERS\n";
                    out.write(clientMessage.getBytes());
                    out.write(userList.getBytes());
                    out.flush();
                }
                if(userRequest.equals("CHAT_REQUEST")){
                    String from = scanner.nextLine();
                    String to = scanner.nextLine();
                   
                    targetClientOut = connectedClients.get(to).getOutputStream();
                    targetClientIn = connectedClients.get(to).getInputStream();
                    targetClientReader = new Scanner(targetClientIn).useDelimiter("\\n");
                    
                    out.write("PREPARE_CHAT\n".getBytes());
                    String fromDetails = from + "\n";
                    String toDetails = to + "\n";
                    out.write(fromDetails.getBytes());
                    out.flush();
                    targetClientOut.write("PREPARE_CHAT\n".getBytes());
                    targetClientOut.write(toDetails.getBytes());
                    targetClientOut.flush();
                    chat = new Chat();
                }
                if(userRequest.equals("CLIENT_MESSAGE")){
                    
                    String clientMessage = scanner.nextLine();
                    System.out.println("heyy I am getting it: " + clientMessage);
                    chat.appendMessage(clientMessage);
                }
        }
            

    // Exceptions
        
        } catch (IOException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

