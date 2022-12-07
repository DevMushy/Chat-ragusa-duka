package com.chat;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
        boolean cond = true;
        ArrayList<ClientHandler> listaClient = new ArrayList();

        ServerSocket ss = new ServerSocket(3000);
        System.out.println("Server in ascolto sulla porta 3000");
        while(cond){
            Socket s = ss.accept();
            ClientHandler c = new ClientHandler(s,listaClient);
            listaClient.add(c);
            c.start();
        }
        ss.close();
    }

}
