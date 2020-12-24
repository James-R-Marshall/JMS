/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.jms.JMSException;
import javax.naming.NamingException;

/**
 *
 * @author slumb
 */
public class EventLogic {
    public String Directions;
    public void mainloop() throws NamingException, JMSException
    {
        Connection con = new Connection();
        Connection.TopicConnect tc= con.new TopicConnect("MyTopic", "ConnectionFactory", "Slum");
        eventListener l = new eventListener(this);
        tc.CreateCustomListener(l);
        try {
 
 BufferedReader commandLine = new
 java.io.BufferedReader(new InputStreamReader(System.in));
 // Loop until the word "exit" is typed
 while(true) {
	 System.out.println("checking...");
 if ("exit".equals(Directions)){
 tc.CloseConnect();
 System.exit(0);
 } 
 
 else if ("Hi".equals(Directions)) {
          System.out.println("Hello"); 
          Directions = "Waiting";
     }
 else if (Directions!= "Waiting" && Directions != null) {
	 Thread.sleep(2000);
	 System.out.println("Recieved: " + Directions);
	 Directions = "Waiting";
 }
 else {
 System.out.println("No message");
 Thread.sleep(2000);
 }
 }
 } catch (Exception e) { e.printStackTrace(); }
       
    }
    
}
