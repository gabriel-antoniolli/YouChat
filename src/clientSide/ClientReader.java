/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author gabri
 */
public class ClientReader implements Runnable {

   private Socket socket;

    public ClientReader(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        
        try {
            
            InputStream in = socket.getInputStream();
            Scanner scanner = new Scanner(in).useDelimiter("\n");
            while (true) {
                String message = scanner.nextLine();
                System.out.println("Server: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
