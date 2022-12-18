package com.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientHandler extends Thread {

    private Socket s;
    private PrintWriter pr;
    private BufferedReader br;
    //controllo server
    private boolean loop = true;
    private String reciever = new String();
    private String command = new String();
    private String body = new String();
    private int pos = 0;
    private ArrayList<ClientHandler> listaClient;
    private ArrayList<ClientHandler> listaClientTemp;
    private String clientUserName;

    public ClientHandler(Socket s, ArrayList<ClientHandler> listaClient) {
        this.s = s;
        setName("Chat");
        this.listaClient = listaClient;
        this.listaClientTemp = new ArrayList<>();
    }

    public void run() {
        try {
            // per parlare
            pr = new PrintWriter(s.getOutputStream(), true);

            // per ascoltare
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

            ObjectMapper mapper = new ObjectMapper();

            /*for (int i = 0; i < listaClient.size(); i++) {
                NameClientList.add(listaClient.get(i).clientUserName);
            }

            String SendClientList = mapper.writeValueAsString(NameClientList);
            pr.println(SendClientList);*/

            clientUserName = br.readLine();

            while (loop) {
                //trasforma la stringa in un messaggio
                String tempMessage = br.readLine();
                Message message = mapper.readValue(tempMessage, Message.class);

                //prende il primo carattere presente nel messaggio 
                char control = message.getBody().charAt(0);

                //controlla il carattere preso in precedenza per scegliere l'azione da fare
                if (control == '@') {
                    //recupero il nome del ricevente dal body
                    for (int i = 0; i < 50; i++) {
                        if (message.getBody().charAt(i + 1) == ' ') {
                            pos = i + 1;
                            break;
                        }
                        reciever = reciever + message.getBody().charAt(i + 1);
                    }
                    //recupero il messaggio dal bogy
                    for (int i = pos; i < message.getBody().length(); i++) {

                        body = body + message.getBody().charAt(i);
                    }
                    body = body.replaceAll("^.", "");

                    //setto i parametri
                    message.setReceiver(reciever);
                    message.setType("tx1");
                    message.setBody(body);

                    String MessageToSend = mapper.writeValueAsString(message);

                    //invio
                    for (int i = 0; i < listaClient.size(); i++) {

                        if (listaClient.get(i).clientUserName.equals(message.getReceiver())) {
                            listaClient.get(i).pr.println(MessageToSend);
                        }

                    }

                } else if (control == '/') {
                    //estraggo il comando
                    for (int i = 1; i < message.getBody().length(); i++) {
                        command = command + message.getBody().charAt(i);
                    }

                    message.setReceiver(null);
                    message.setType("tx2");

                    switch (command) {

                        case ("help"):
                            message.setBody(
                                    "Lista Comandi: -1) @[nome] per utilizzare i messaggi privati -2) messaggio senza header = broadcast -3) /exit per chiudere la sessione -4) /help per visualizzare i comandi");
                            break;

                        case ("exit"):
                            int pos = 0;
                            for (int i = 0; i < listaClient.size(); i++) {
                                if(!message.getSender().equals(listaClient.get(i).clientUserName)){
                                    listaClientTemp.add(listaClient.get(i));
                                }
                                else{
                                    pos = i;
                                    i = listaClient.size();
                                }
                            }
                                for (int j = pos; j < listaClient.size(); j++) {
                                    listaClientTemp.add(listaClient.get(j));
                                }
                            message.setBody("close client");
                            
                        break;

                        default:
                            message.setBody(
                                    "Comando digitato non valido, digita /help per vederte la lista dei comandi");
                            break;

                    }
                    //invio
                    String commandToSend = mapper.writeValueAsString(message);
                    for (int i = 0; i < listaClient.size(); i++) {

                        if (listaClient.get(i).clientUserName.equals(message.getSender())) {
                            listaClient.get(i).pr.println(commandToSend);
                        }

                    }
                    //azzero comandi
                    command = "";
                    //controllo per il msg di broadcast che il body inizi con una lettera
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
