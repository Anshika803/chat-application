package com.example.chat_app;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import static java.nio.file.Files.newBufferedReader;

class Server extends JFrame {

     ServerSocket server;
     Socket socket;

     BufferedReader br;
     PrintWriter out;

    private JLabel heading=new JLabel("Server area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto", Font.PLAIN,20);

    //    constructor
     public Server()  {
         try {
             server = new ServerSocket(7777);
             System.out.println("server is ready to accept connection");
             System.out.println("waiting....");
             socket = server.accept();

             br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             out = new PrintWriter(socket.getOutputStream());

             createGUI();
             handleEvents();
             startReading();
//             startWriting();

         } catch (Exception e) {
             e.printStackTrace();
         }
     }
    private void createGUI(){
//        gui code;

        this.setTitle("Server Messager[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


//        coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);


//         frame ka layout set krenge
        this.setLayout(new BorderLayout());

//        adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jscrollpane=new JScrollPane(messageArea);
        this.add(jscrollpane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);

    }

    private void handleEvents(){
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
//               System.out.println("key released"+e.getKeyCode());
                if (e.getKeyCode() == 10) {
                    String messagetosend = messageInput.getText();
                    messageArea.append("Me :"+messagetosend+"\n");
                    out.println(messagetosend);
                    out.flush();
                    messageInput.setText("");
                }
            }
        });
    }


    public void startReading() {
//              thread-read krke deta rhega
             Runnable r1 = () -> {

                 System.out.println("reader started...");

                 try {
                 while (true) {
                         String msg = br.readLine();
                         if(msg.equals("exit")) {
                             System.out.println("Client terminated the chat");
                             JOptionPane.showMessageDialog(this,"client terminated the chat");
                             messageInput.setEnabled(false);
                             socket.close();
                             break;
                         }
//                         System.out.println("Client :" + msg);
                     messageArea.append("Server:" + msg+"\n");
                 }
                 } catch (Exception e) {
//                     e.printStackTrace();
                     System.out.println("connection is closed....");
                 }
             };
             new Thread(r1).start();
         }

         public void startWriting() {
//thread-data user se lega and then send krke client tak

             Runnable r2 = () -> {
                 System.out.println("writer started...");

                 try {
                 while( !socket.isClosed()) {

                         BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                         String content = br1.readLine();
                         out.println(content);
                         out.flush();

                     if(content.equals("exit")){
                         socket.close();
                         break;
                     }
                 }
                 } catch (Exception e) {
//                     e.printStackTrace();
                     System.out.println("connection is closed....");
                 }
             };
             new Thread(r2).start();
         }

         public static void main(String args[]){
         System.out.println("this is server...server is going to start");
         new Server(); // calling constructor

     }
     }

