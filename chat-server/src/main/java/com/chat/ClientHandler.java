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
    private PrintWriter pr;
    private BufferedReader br;
    private int id;
    private boolean loop = true;
    private String reciever = new String();
    private String command = new String();
    private String body = new String();
    private int pos = 0;
    private ArrayList<ClientHandler> listaClient;
    private String clientUserName;
    private ArrayList<String> NameClientList;

    public ClientHandler(Socket s, ArrayList<ClientHandler> listaClient) {
        this.s = s;
        id = x++;
        setName("Chat");
        this.listaClient = listaClient;
        NameClientList = new ArrayList<String>();
    }

    public void run() {
        try {
            // per parlare
            pr = new PrintWriter(s.getOutputStream(), true);

            // per ascoltare
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

            ObjectMapper mapper = new ObjectMapper();

            for (int i = 0; i < listaClient.size(); i++) {
                NameClientList.add(listaClient.get(i).clientUserName);
            }

            String SendClientList = mapper.writeValueAsString(NameClientList);
            pr.write(SendClientList);

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
                    for (int i = 1; i < message.getBody().length(); i++) {
                        command = command + message.getBody().charAt(i);
                    }
                    switch (command) {
                        case ("help"):
                            message.setBody(
                                    "Lista Comandi: -1) @[nome] per utilizzare i messaggi privati -2)...");
                            message.setReceiver(null);
                            message.setType("tx2");
                            break;

                        default:
                            message.setBody(
                                    "Comando digitato non valido, digita /help per vederte la lista dei comandi");
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
                    command = "";
                } else if(control >= 'a' && control <= 'z' || control >= 'A' && control <= 'Z'){

                    message.setReceiver("*");
                    message.setType("tx0");
                    message.setBody(message.getBody());

                    String BrdcstMessage = mapper.writeValueAsString(message);

                    for (int i = 0; i < listaClient.size(); i++) {
                        if(!message.getSender().equals(listaClient.get(i).clientUserName)){
                            listaClient.get(i).pr.println(BrdcstMessage);
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
