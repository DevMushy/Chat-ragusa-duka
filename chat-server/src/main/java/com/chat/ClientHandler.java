package com.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientHandler extends Thread {

    private Socket s;
    private static int x = 1;
    private int id;
    private LocalDate date;
    private LocalTime time;
    private boolean loop = true;
    char[] charArray = new char[50];
    char[] charbody = new char[2000];
    int pos = 0;

    public ClientHandler(Socket s) {
        this.s = s;
        id = x++;
        setName("Chat");
    }

    public void run() {
        try {
            // per parlare
            PrintWriter pr = new PrintWriter(s.getOutputStream(), true);

            // per ascoltare
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

            ObjectMapper mapper = new ObjectMapper();

            while (loop) {
                // da riontrollare
                String tempMessage = br.readLine();
                Message message = mapper.readValue(tempMessage, Message.class);

                char control = message.getBody().charAt(0);

                if (control == '@') {
                    for (int i = 0; i < 50; i++) {
                        if (message.getBody().charAt(i + 1) == ' ') {
                            pos = i + 1;
                            break;
                        }
                        charArray[i] = message.getBody().charAt(i + 1);
                        System.out.println(charArray);
                    }
                    int j = 0;
                    for (int i = pos; i < message.getBody().length(); i++) {

                        charbody[j] = message.getBody().charAt(i);
                        j++;
                    }

                    String test = new String(charbody);
                    test = test.replaceAll("^.", "");

                    
                    message.setReceiver(new String(charArray));
                    message.setType("tx1");
                    message.setBody(test);
                    System.out.println(message.getType());
                    System.out.println(message.getReceiver());
                    System.out.println(message.getBody());

                } else if (control == '/') {
                    for (int i = 0; i < 50; i++) {
                        if (String.valueOf(message.getBody().charAt(i + 1)) == " ") {
                            break;
                        }
                        charArray[i] = message.getBody().charAt(i + 1);
                    }

                }
            }
            s.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
