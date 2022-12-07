package com.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {
    static String userName;

    public static void main(String[] args) throws Exception {
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

        System.out.println("nome: ");
        userName = tastiera.readLine();
        pr.println(userName);
        String inputString;
        String messageJSON;
        Message messageToSend;

        Boolean loop = true;

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
