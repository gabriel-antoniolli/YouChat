/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static serverSide.WebSocketServer.currentUsers;

/**
 *
 * @author gabri
 */
public class ClientConnection implements Runnable {

    Socket client;
    public String name;

    public ClientConnection(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

        try {
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();
            Scanner scanner = new Scanner(in).useDelimiter("\\n");
            MenuInteraction interaction = new MenuInteraction(out, scanner);
            interaction.interact();
            name = interaction.name;
            
            // Use the name to identify the client in future messages
            System.out.println(name + " connected.");
            while (true) {

                String data = scanner.nextLine();
                System.out.println(name + "> " + data);
                // handle incoming message from client
            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        }
    }
}
