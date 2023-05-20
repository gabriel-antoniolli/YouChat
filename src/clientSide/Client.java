/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author gabri
 */
public class Client {
    
    private static ArrayList<String> allUsers = new ArrayList<>();
    private static HashMap<Integer,String> userMap = new HashMap<>();
    
    public static void setUsers(ArrayList<String> list){
        allUsers = list;
        
    }
    
    public static ArrayList<String> getUsers(){
        return allUsers;
    }
    
    public static void main(String[] args) throws InterruptedException {
        try {
            Socket socket = new Socket("127.0.0.1", 8080);
            System.out.println("Connected to server at 127.0.0.1 on port 8080");
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            Scanner sc = new Scanner(System.in);
            Scanner serverScanner = new Scanner(in).useDelimiter("\n");
            
            Thread readThread = new Thread(new ClientReader(socket,out));
            readThread.start();
            readThread.join(1000);

            out.write("INSERT_NAME\n".getBytes());  
            System.out.println("Please insert your name:\n");
            String name = sc.nextLine();
            String clientName = name;
            name += "\n";
            
            out.write(name.getBytes());
            out.write("JOIN_SERVER\n".getBytes());
            out.flush();
            MenuInteraction menu = new MenuInteraction(out, sc, serverScanner);
            menu.setName(clientName);
            menu.display();
            
            // create a new thread to continuously read messages from the server
            
            
//            while (valid) {
//                
//                System.out.print(">");
//                String message = scanner.nextLine();
//                
//                /**
//                 * This + "\n" is quite important because without it the Scanner.nextLine() in the ClientConnection class 
//                 * would not stop waiting for the message since the way it stops is when he finds the \n delimeter.
//                 * I confess it took me some time to figure this out.
//                 */
//                out.write((message + "\n").getBytes());
//                out.flush();
//                
//                if(message.equals("exit")){
//                    valid = false;
//                }
//            }
//            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
