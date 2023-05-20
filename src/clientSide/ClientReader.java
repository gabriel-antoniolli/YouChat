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
   
    public ClientReader(Socket socket, OutputStream out) {
        this.socket = socket;
        this.out = out;
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
                    System.out.println("PREPARING CHAT...");
                    Thread.sleep(2000);
                    boolean valid = true;
                     System.out.println("Press 'ENTER' two times to enter the Chat");
                    while(valid){
                        out.write("CLIENT_MESSAGE\n".getBytes());
                        out.flush();
                        System.out.print(from + "> ");
                        String msg = scanner.nextLine();
                        msg += "\n";
                        out.write(msg.getBytes());
                        out.flush();
                    }


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException ex) {
           Logger.getLogger(ClientReader.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
    
}
