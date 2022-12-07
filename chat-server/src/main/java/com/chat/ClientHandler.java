package com.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientHandler extends Thread {

    private Socket s;
    private static int x = 1;
    PrintWriter pr;
    BufferedReader br;
    private int id;
    private boolean loop = true;
    String reciever = new String();
    String command = new String();
    String body = new String();
    int pos = 0;
    ArrayList<ClientHandler> listaClient;
    String clientUserName;

    public ClientHandler(Socket s, ArrayList<ClientHandler> listaClient) {
        this.s = s;
        id = x++;
        setName("Chat");
        this.listaClient = listaClient;
    }

    public void run() {
        try {
            // per parlare
            pr = new PrintWriter(s.getOutputStream(), true);

            // per ascoltare
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

            ObjectMapper mapper = new ObjectMapper();
            clientUserName = br.readLine();

            while (loop) {
                String tempMessage = br.readLine();
                Message message = mapper.readValue(tempMessage, Message.class);

                char control = message.getBody().charAt(0);

                if (control == '@') {
                    for (int i = 0; i < 50; i++) {
                        if (message.getBody().charAt(i + 1) == ' ') {
                            pos = i + 1;
                            break;
                        }
                        reciever = reciever + message.getBody().charAt(i + 1);
                    }
                    for (int i = pos; i < message.getBody().length(); i++) {

                        body = body + message.getBody().charAt(i);
                    }
                    body = body.replaceAll("^.", "");

                    message.setReceiver(reciever);
                    message.setType("tx1");
                    message.setBody(body);

                    String MessageToSend = mapper.writeValueAsString(message);

                    for (int i = 0; i < listaClient.size(); i++) {

                        if (listaClient.get(i).clientUserName.equals(message.getReceiver())) {
                            listaClient.get(i).pr.println(MessageToSend);
                        }

                    }

                } else if (control == '/') {
                    for (int i = 0; i < 50; i++) {
                        if (message.getBody().charAt(i + 1) == ' ') {
                            break;
                        }
                        command = command + message.getBody().charAt(i + 1);
                    }

                    switch(command){
                        case ("help"):
                        message.setBody("comando di help");
                        message.setReceiver(null);
                        message.setType("tx2");
                        break;

                    }

                    String commandToSend = mapper.writeValueAsString(message);
                    for (int i = 0; i < listaClient.size(); i++) {

                        if (listaClient.get(i).clientUserName.equals(message.getSender())) {
                            listaClient.get(i).pr.println(commandToSend);
                        }

                    }
                }
            }
            s.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
