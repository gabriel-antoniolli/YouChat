/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package youchat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebSocketServer {
  

  

    public static void main(String[] args) throws IOException {
        
         ServerSocket server = new ServerSocket(8080);
    try {
      System.out.println("Server has started on 127.0.0.1:80.\r\nWaiting for a connection…");
      Socket client = server.accept();
      System.out.println("A client connected.");
      
    } catch(Exception e){
        System.out.println(e);
    }
       
    }
}
