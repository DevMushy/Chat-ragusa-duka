package com.chat;
import java.net.ServerSocket;
import java.net.Socket;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
        boolean cond = true;

        ServerSocket ss = new ServerSocket(3000);
        System.out.println("Server in ascolto sulla porta 3000");
        while(cond){
            Socket s = ss.accept();
            ClientHandler c = new ClientHandler(s);
            c.start();
        }
        ss.close();
    }

}
