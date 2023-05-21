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
            InputStream in = client.getInputStream();
            out = client.getOutputStream();
            Scanner scanner = new Scanner(in).useDelimiter("\\n");

            while (true) {
                String userRequest = scanner.nextLine();

                if (userRequest.equals("INSERT_NAME")) {
                    String temp = name = scanner.nextLine();
                    addUser(temp);
                    connectedClients.put(name, client);
                    System.out.println("User added to the server: " + name);
                    System.out.println("Amount of users connected is:" + currentUsers.size());
                }
                if (userRequest.equals("JOIN_SERVER")) {
                    System.out.println(name + " has joined the server");
                }
                if (userRequest.equals("DECISION_1")) {

                    String userList = String.join(",", currentUsers);
                    userList += "\n";

                    String clientMessage = "CURRENT_USERS\n";
                    out.write(clientMessage.getBytes());
                    out.write(userList.getBytes());
                    out.flush();
                }
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
//                    targetClientOut.write("GET_DETAILS\n".getBytes());
//                    targetClientOut.flush();
                }
                if(userRequest.contains("FROM_")){
                    if(from.equals("")){
                        
                        from = userRequest.substring(userRequest.indexOf("M_") + 2);
                        if( connectedClients.get(from).getOutputStream() != null){
                            target = connectedClients.get(from).getOutputStream();
                        }
                    }
                }
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
                if(userRequest.equals("EXIT_CHAT")){
                    if(from.equals(name)){
                        from = to;
                    }
                    String msg = from + "\n";
                    System.out.println("FROM: " + from);
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
