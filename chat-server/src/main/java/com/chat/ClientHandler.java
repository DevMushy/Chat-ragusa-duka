package com.chat;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientHandler extends Thread{

    private Socket s;
    private static int x = 1;
    private int id;
    private LocalDate date;
    private LocalTime time;
    private boolean loop = true;
    
    public ClientHandler(Socket s){
        this.s = s;
        id = x++;
        setName("Chat");
    }

    public void run(){
        try {
            // per parlare
            PrintWriter pr = new PrintWriter(s.getOutputStream(), true);
              
            // per ascoltare
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

            ObjectMapper mapper = new ObjectMapper();
            
            
            while(loop){
                //da riontrollare
                String tempMessage = br.readLine();
                Message message = mapper.readValue(tempMessage, Message.class);

                char control = message.getBody().charAt(0);

                if(control == '/'){
                    char[] charArray = new char[50];
                    for (int i = 0; i < 50; i++) {
                        if(String.valueOf(message.charAt(i + 1)) == " ") {
                            break;
                        }
                        charArray[i] = message.charAt(i + 1);
                    }

                }else if(control == '!'){


                }
                



            }
            s.close();
        
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
            
    }
    
}
