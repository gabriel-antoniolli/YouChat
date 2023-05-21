/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

import java.awt.Color;
import java.util.Scanner;
import javafx.scene.layout.Border;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author gabriel Pereira Antoniolli -- 2020352
 * 
 * not really much to discuss here to be honest, most of it is a template i got online.
 * https://codereview.stackexchange.com/questions/25461/simple-chat-room-swing-gui
 * I just had to do some small adjustments.
 * 
 */
public class Chat extends JFrame {
    private JFrame frame;
    private JTextArea chatArea;
    private JScrollPane scroll;
    private Border border;
    
    public Chat(){
        frame = new JFrame("YouChat");
        chatArea = new JTextArea(20,50);
        scroll = new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        chatArea.setSize(540, 400);
        chatArea.setLocation(30,5);
        frame.setResizable(false);
        frame.setSize(600, 600);
        frame.add(scroll);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        chatArea.append("Chats: \n");
    
    }
    
    public void appendMessage(String msg){
        chatArea.append(msg);
        chatArea.append("\n");
    }
    public void exitChat(){
        Thread.currentThread().interrupt();
        
    }
}
