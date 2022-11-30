package com.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App 
{
    static String userName;
    public static void main( String[] args ) throws Exception
    {
        Socket s = new Socket("localhost", 3000);
        ChatControlThread contr = new ChatControlThread(s);
        ObjectMapper mapper = new ObjectMapper();
        contr.start();

        // per parlare
        PrintWriter pr = new PrintWriter(s.getOutputStream(), true);
        // per ascoltare
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        // per la tastiera
        BufferedReader tastiera = new BufferedReader(new InputStreamReader(System.in));
        
        String inputString;
        String messageJSON;
        Message messageToSend;

        Boolean loop = true;
        pr.println("Eccomi");

        System.out.println(br.readLine()); // ricevo: Ciao come ti chiami?
        pr.println(tastiera.readLine()); // leggo da tastiera il nome e lo invio
        System.out.println(br.readLine()); // ricevo: Salve {nome} sei l'utente connesso numero {x} + i comandi disponibili

        while(loop) {
            inputString = tastiera.readLine();
            messageToSend = new Message(userName, null, null, inputString);
            messageJSON = mapper.writeValueAsString(messageToSend);
            pr.println(messageJSON);
        };
        br.close();
        pr.close();
        s.close();
    }
}
