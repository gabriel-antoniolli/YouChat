/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

import static clientSide.Client.getUsers;
import static clientSide.Client.setUsers;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

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
                    getUsers();
                }
                if(message.equals("INVITATION_REQUEST")){
                
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
