
import java.io.*;
import java.net.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kevin
 */
public class VotingHUD {
    public static void main(String[] args) throws IOException {
        if (args.length !=2){
            System.out.println("Two command-line arguments are expected.");
            System.out.println("Multi-cast address and port");
        }
        else {

            int CounterYES,CounterNO,CounterBLANK;
            CounterYES=0;
            CounterNO=0;
            CounterBLANK=0;

            Boolean END = false;

            InetAddress group = InetAddress.getByName(args[0]);
            int port = Integer.parseInt(args[1]);

            MulticastSocket socket = null;
            byte[] data = null;

            Thread CounterThread = new Thread(new Counter(group, port));
            CounterThread.start();

            String vote = "NONE";

            while(CounterThread.isAlive() && !"END".equals(vote)){
                vote = "NONE";

                while(!"YES".equals(vote) && !"NO".equals(vote) && !"BLANK".equals(vote) && !"END".equals(vote)){
                    System.out.println("Introduce your vote: YES, NO or BLANK");

                    InputStreamReader is = new InputStreamReader(System.in);
                    BufferedReader br = new BufferedReader(is);
                    vote = br.readLine();

                    if(CounterThread.isAlive()){
                        if("YES".equals(vote)){
                            CounterYES++;
                        }
                        else if("NO".equals(vote)){
                            CounterNO++;
                        }
                        else if("BLANK".equals(vote)){
                            CounterBLANK++;
                        }
                        else if("END".equals(vote)){
                            END = true;
                        }
                        else{
                        System.out.println("Invalid format, try again");
                        }
                    }
                    else{
                        System.out.println("Votation already ended.");
                        END = true;
                    }
                }

                try {
                    String characters = vote;
                    data = characters.getBytes();
                    DatagramPacket packet = new DatagramPacket(data, data.length, group, port);

                    socket = new MulticastSocket(port);
                    socket.setTimeToLive(1); 
                    socket.send(packet);
                    socket.close();
                }
                catch (Exception se) {
                    se.printStackTrace( );
                } // end catch 
            }
            
            while(CounterThread.isAlive()){
            }
                System.out.println("VOTES IN THIS MACHINE");
                System.out.println("YES: " + CounterYES);
                System.out.println("NO " + CounterNO);
                System.out.println("BLANK " + CounterBLANK); 
        }
    } // end main
}
