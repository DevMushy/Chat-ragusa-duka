package com.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChatControlThread extends Thread{
    Socket s;
    PrintWriter pr;
    BufferedReader br;
    BufferedReader tastiera;
    Message parsedMessage;
    ObjectMapper mapper;

    public ChatControlThread(Socket s) {
        try {
            this.s = s;
            // per parlare
            pr = new PrintWriter(s.getOutputStream(), true);
            // per ascoltare
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            // per la tastiera
            tastiera = new BufferedReader(new InputStreamReader(System.in));

            mapper = new ObjectMapper();
        
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        String ChatJSONString;
        Boolean chatOn  = true;

        while(chatOn) {
            try {
                //riceve il messaggio
                ChatJSONString = br.readLine();
                //lo trasforma in un oggetto message
                parsedMessage = mapper.readValue(ChatJSONString, Message.class);
                //stampa il campo body del messaggio
                System.out.println(parsedMessage.getBody());
                //controllo per la chiusura
                if(parsedMessage.getBody().equals("close client")){
                    chatOn = false;
                }
                
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        };
    }
    
}