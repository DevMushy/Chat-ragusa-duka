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

        // String temp = br.readLine();
        // ClientList = mapper.readValue(temp, ArrayList.class);

        // if(ClientList.get(0) == null){
        System.out.println("inserisci username: ");
        userName = tastiera.readLine();
        pr.println(userName);
        /*
         * }else{
         * boolean Namecontrol = true;
         * while (Namecontrol) {
         * System.out.println("inserisci username: ");
         * userName = tastiera.readLine();
         * for (int i = 0; i < ClientList.size(); i++) {
         * if (ClientList.get(i).equals(userName)) {
         * System.out.println(ClientList);
         * Namecontrol = false;
         * }
         * }
         * if(!Namecontrol){
         * pr.println(userName);
         * break;
         * }
         * }
         * }
         */

        contr.start();

        String inputString;
        String messageJSON;
        Message messageToSend;

        Boolean loop = true;
        System.out.println("digita /help per vederte la lista dei comandi");
        while (loop) {
            // input da tastiera
            inputString = tastiera.readLine();
            // crea messaggio e serializza
            messageToSend = new Message(userName, inputString);
            messageJSON = mapper.writeValueAsString(messageToSend);
            // invio messaggio
            pr.println(messageJSON);
            //controllo chiusura
            if (inputString.equals("/exit")) {
                loop = false;
            }
        }

        br.close();
        pr.close();
        s.close();
    }
}
