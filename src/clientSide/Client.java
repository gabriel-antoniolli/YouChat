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
import java.util.Scanner;

/**
 *
 * @author gabri
 */
public class Client {
    
    public static void main(String[] args) throws InterruptedException {
        try {
            Socket socket = new Socket("127.0.0.1", 8080);
            System.out.println("Connected to server at 127.0.0.1 on port 8080");
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            Scanner scanner = new Scanner(System.in);
            boolean valid = true;
            
            // create a new thread to continuously read messages from the server
            Thread readThread = new Thread(new ClientReader(socket));
            readThread.start();
            readThread.join(1000);
            
            while (valid) {
                System.out.print("> ");
                String message = scanner.nextLine();
                
                /**
                 * This + "\n" is quite important because without it the Scanner.nextLine() in the ClientConnection class 
                 * would not stop waiting for the message since the way it stops is when he finds the \n delimeter.
                 * I confess it took me some time to figure this out.
                 */
                out.write((message + "\n").getBytes());
                out.flush();
                
                if(message.equals("exit")){
                    valid = false;
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
