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
 *  @author gabriel Pereira Antoniolli -- 2020352
 */
public class ClientConnection implements Runnable {

    Socket client;
    public String name;
    Chat chat;
    String from = "";
    public OutputStream target = null;
    String to = "";

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
            //stream of inputs coming from the client 
            InputStream in = client.getInputStream();
            
            //stream of inputs towards the client
            out = client.getOutputStream();
            
            //scanner reading whatever message comes from the client InputStream.
            Scanner scanner = new Scanner(in).useDelimiter("\\n");

            //This will always be true, because we will always want to listen what do user has to send.
            while (true) {
                String userRequest = scanner.nextLine();

                /**
                 * List of commands a client can request to the server in order to use the application
                 */
                // Save his/her name in the app
                if (userRequest.equals("INSERT_NAME")) {
                    String temp = name = scanner.nextLine();
                    addUser(temp);
                    connectedClients.put(name, client);
                    System.out.println("User added to the server: " + name);
                    System.out.println("Amount of users connected is:" + currentUsers.size());
                }
                // just logging in the server console that that user joined the server
                if (userRequest.equals("JOIN_SERVER")) {
                    System.out.println(name + " has joined the server");
                }
                //once the users decide to check available users it sends a String will all users conneceted to the server
                if (userRequest.equals("DECISION_1")) {

                    // this command concatenates all the users in the comma
                    String userList = String.join(",", currentUsers);
                    // note that ALL the messages will have to end with \n 
                    //considering this is where the nextLine() command knows when to stop
                    userList += "\n";

                    String clientMessage = "CURRENT_USERS\n";
                    out.write(clientMessage.getBytes());
                    out.write(userList.getBytes());
                    out.flush();
                }
                /**
                 * Once the user sends the chat request to the server it will get the from and to the chat is going to be
                 * then will create the target object which is the OutputStream to the target Client of the chat
                 * and finally it will also send the command to PREPARE_CHAT for both clients 
                 * (it will be better explained in ClientReader class.)
                 * 
                 */
                if (userRequest.equals("CHAT_REQUEST")) {
                    from = scanner.nextLine();
                    to = scanner.nextLine();
                    target = connectedClients.get(to).getOutputStream();
                    
                    out.write("PREPARE_CHAT\n".getBytes());
                    String fromDetails = from + "\n";
                    String toDetails = to + "\n";
                    out.write(toDetails.getBytes());
                    out.flush();
                    target.write("PREPARE_CHAT\n".getBytes());
                    target.write(fromDetails.getBytes());
                    target.flush();
                }
                /**
                 * This is a workaround that I had to create to assign the from variable to a name again.
                 * the reason I had to do so is because only one client will request that CHAT_REQUEST command from above
                 * that creates the from and to Strings.
                 * since the other client will not have none of those created then we have the create it here.
                 */
                if(userRequest.contains("FROM_")){
                    if(from.equals("")){
                        
                        from = userRequest.substring(userRequest.indexOf("M_") + 2);
                        if( connectedClients.get(from).getOutputStream() != null){
                            target = connectedClients.get(from).getOutputStream();
                        }
                    }
                }
                /**
                 * Every time a new message is sent in the chat from user to another it will be requesting this block of code.
                 * as you can see it sends message both to the client A and B
                 * it also send the CLIENT_MESSAGE command that will be explained in the ClientReader class.
                 */
                if (userRequest.contains("CLIENT_MESSAGE")) {

                    String msg = scanner.nextLine();
                    msg += "\n";
                    
                    int startIndex = userRequest.indexOf("E_") + 2;
                    String tempName = userRequest.substring(startIndex);
                    

                    String senderMessage = name + "> " + msg;
                    out.write("CLIENT_MESSAGE\n".getBytes());
                    out.flush();
                    out.write(senderMessage.getBytes());
                    out.flush();
                    
                    target.write("CLIENT_MESSAGE\n".getBytes());
                    target.flush();
                    target.write(senderMessage.getBytes());
                    target.flush();
                }
                /**
                 * if a client decides to exit the chat he/she will send this command to warn the server.
                 * once the server know they are leaving it then passes the command to save the chat.
                 */
                if(userRequest.equals("EXIT_CHAT")){
                    // this is another workaround because the 'from' in one of the clients was not loading.
                    if(from.equals(name)){
                        from = to;
                    }
                    String msg = from + "\n";
                    out.write("SAVE_CHAT\n".getBytes());
                    out.flush();
                    out.write(msg.getBytes());
                    out.flush();
                    
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
