package com.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class App {
    static String userName;

    public static void main(String[] args) throws Exception {
        ArrayList<String> ClientList;
        Socket s = new Socket("localhost", 3000);
        // per parlare
        PrintWriter pr = new PrintWriter(s.getOutputStream(), true);
        // per ascoltare
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        // per la tastiera
        BufferedReader tastiera = new BufferedReader(new InputStreamReader(System.in));
        ChatControlThread contr = new ChatControlThread(s);
        ObjectMapper mapper = new ObjectMapper();
        contr.start();

        String temp = br.readLine();
        ClientList = mapper.readValue(temp, ArrayList.class);

        if(ClientList.isEmpty()){
            System.out.println("inserisci username: ");
            pr.println(userName);
        }else{
            boolean Namecontrol = true;
            while (Namecontrol) {    
                System.out.println("inserisci username: ");
                userName = tastiera.readLine();
                for (int i = 0; i < ClientList.size(); i++) {
                    if (ClientList.get(i).equals(userName)) {
                        
                    }
                }
            }
        }

        String inputString;
        String messageJSON;
        Message messageToSend;

        Boolean loop = true;
        System.out.println("digita /help per vederte la lista dei comandi");
        while (loop) {
            inputString = tastiera.readLine();
            messageToSend = new Message(userName, inputString);
            messageJSON = mapper.writeValueAsString(messageToSend);
            pr.println(messageJSON);
        }
        ;
        br.close();
        pr.close();
        s.close();
    }
}
