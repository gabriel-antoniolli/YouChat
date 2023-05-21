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
 * @author gabriel Pereira Antoniolli -- 2020352
 */
public class Client {
    
    // Name of the clients
    public static ArrayList<String> users = new ArrayList<>();
    // indexes of the users to be chosen in the check available users Menu.
    private static HashMap<Integer,String> userMap = new HashMap<>();
    // store the conversations that a specific user had.
    private static HashMap<String,ArrayList<String>> chatHistory = new HashMap<>();
    
    
    public static void setUsers(ArrayList<String> list){
        users = list;
    }
    
    public static ArrayList<String> getUsers(){
        return users;
    }
    
    public static void main(String[] args) throws InterruptedException {
        try {
            
            Socket socket = new Socket("127.0.0.1", 8080);
            System.out.println("Connected to server at 127.0.0.1 on port 8080");
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            Scanner sc = new Scanner(System.in);
            Scanner serverScanner = new Scanner(in).useDelimiter("\n");
            
            /**
             * Thread responsible for listening all the communication coming from the server.
             * the use of threads here is mandatory since the program cannot execute its task and
             * keep listening for new messages in another way.
             */
            Thread readThread = new Thread(new ClientReader(socket,out, chatHistory,sc));
            readThread.start();
            readThread.join(1000);

            // Start the program by giving the client name to the server
            out.write("INSERT_NAME\n".getBytes());  
            System.out.println("Please insert your name:\n");
            String name = sc.nextLine();
            String clientName = name;
            // again, it needs the \n because of the Scanner.nextLine().
            name += "\n";
            out.write(name.getBytes());
            
            //telling the server it is joing the application (which it already have it is just a formality.)
            out.write("JOIN_SERVER\n".getBytes());
            out.flush();
            
            //initializes the menu for the client
            MenuInteraction menu = new MenuInteraction(out, sc, chatHistory);
            // gives current client name 
            menu.setName(clientName);
            // display options.
            menu.display();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
