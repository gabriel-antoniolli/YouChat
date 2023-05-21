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
 * @author gabri
 */
public class Chat extends JFrame {
    private JFrame frame;
    private JTextArea chatArea;
    private JScrollPane scroll;
    private Border border;
    
    public static void main(String[] args){
        new Chat();
    }
    
    public Chat(){
        frame = new JFrame("Product Bot");
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
}
