


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Session;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;



public class Main2 {
   public static void main(String[] args) throws JMSException, IOException, NamingException {
        // variables that we need for our chat room
            Connection con = new Connection();
        Connection.TopicConnect tc= con.new TopicConnect("MyTopic", "ConnectionFactory", "Slum");
       try {
 if (args.length!=3)
 System.out.println("Factory, Topic, or username missing");
 // args[0]=topicFactory; args[1]=topicName; args[2]=username
 // Read from command line
 BufferedReader commandLine = new
 java.io.BufferedReader(new InputStreamReader(System.in));
 // Loop until the word "exit" is typed
 while(true) {
 String s = commandLine.readLine();
 if (s.equalsIgnoreCase("exit")){
 tc.CloseConnect();
 System.exit(0);
 } else
 tc.writeMessage(s);
 }
 } catch (Exception e) { e.printStackTrace(); }
       
     
    }

   
    
}
        