
import java.io.IOException;
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
public class Counter implements Runnable {
   static final int MAX_LEN = 30;
   private InetAddress group;
   private int port;

  public Counter(InetAddress group, int port) {  
    this.group = group ;
    this.port = port;    
  }

  public void run() {
    int CounterYES,CounterNO,CounterBLANK;
    CounterYES=0;
    CounterNO=0;
    CounterBLANK=0;

    String vote = null;
    
    label:try {
        MulticastSocket socket = new MulticastSocket(port);
        socket.joinGroup(group);
        while (!"END".equals(vote)) {
            byte[] data = new byte[MAX_LEN];
            DatagramPacket packet = new DatagramPacket(data, data.length, group, port);
            socket.receive(packet);
            vote = new String(packet.getData(), 0, packet.getLength());
            if("YES".equals(vote)){
                CounterYES++;
            }
            if("NO".equals(vote)){
                CounterNO++;
            }
            if("BLANK".equals(vote)){
                CounterBLANK++;
            }
            if("END".equals(vote)){
                break label;
            }
        } //end while
    }
    catch (IOException exception) {
    }
        
    System.out.println("GLOBAL RESULTS");
    System.out.println("YES: " + CounterYES);
    System.out.println("NO " + CounterNO);
    System.out.println("BLANK " + CounterBLANK);
    } // end run
}
