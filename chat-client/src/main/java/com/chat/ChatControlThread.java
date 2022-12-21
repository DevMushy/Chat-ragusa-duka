package com.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;

public class ChatControlThread extends Thread{
    Socket s;
    PrintWriter pr;
    BufferedReader br;
    Message parsedMessage;
    ObjectMapper mapper;
    static ArrayList<Message> cronologia;

    public ChatControlThread(Socket s) {
        cronologia = new ArrayList<>();
        try {
            this.s = s;
            
            // per parlare
            pr = new PrintWriter(s.getOutputStream(), true);
            // per ascoltare
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));

            mapper = new ObjectMapper();
        
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        String ChatJSONString;
        Boolean chatOn  = true;

        while(chatOn) {
            try {
                //riceve il messaggio
                ChatJSONString = br.readLine();
                //lo trasforma in un oggetto message
                parsedMessage = mapper.readValue(ChatJSONString, Message.class);
                //aggiunta del messaggio alla cronologia
                //aggiornamento visivo della chat lato client
                //controllo per la chiusura
                if(parsedMessage.getBody().equals("close client")){
                    chatOn = false;
                    App.loop = false;
                    ClientFrame.exit = true;
                } 
                cronologia.add(parsedMessage);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
}