package com.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class App {
    static String userName;
    public static Boolean loop;
    public static PrintWriter pr;
    static BufferedReader br;
    public static ClientFrame frm;
    
    public static void main(String[] args) throws Exception {
        Socket s = new Socket("localhost", 3000);
        
        // per parlare
        pr = new PrintWriter(s.getOutputStream(), true);
        // per ascoltare
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        
        ChatControlThread contr = new ChatControlThread(s);
        ClientFrame frm = new ClientFrame();
        
        contr.start();
        loop = true;
        while(loop) {
            
        }

        br.close();
        pr.close();
        s.close();
    } 
}
