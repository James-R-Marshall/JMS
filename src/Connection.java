

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author James
 */
public class Connection {
    
     protected InitialContext ctx;
       
     
     // This is for initializing our context to an initial state. 
     protected void InitCtx(){
    	 try{
    		 ctx = new InitialContext();
    	 	}
    	 	catch(Exception e){System.out.println(e);} 
 		}
     
     
     
     public class QueueConnect extends Connection  {
         
        protected QueueConnectionFactory f;
        protected QueueConnection con;
        protected QueueSession ses;
        protected Queue t;
        protected QueueSender sender;
        protected QueueReceiver reciever;
        
    // returns our connection factory in case we want it, and we also create the factory
    private QueueConnectionFactory CreateQConnection( String conFactory)
    {
        try{
        InitCtx();
        f = (QueueConnectionFactory)ctx.lookup(conFactory);       
        }
        catch (Exception e){System.out.println(e);}
        return f;
    }
    
    // we actually create the connection and set it to our connection variable
    private void setQConnection(){
        try{
        con = f.createQueueConnection();
    }
    catch(Exception e){System.out.println(e);} 
    }
    
    // create the session off of the stored connection variable, need to call setQconnection first
    public void startSession(boolean transacted ,int acknolegementMode )
    {
        try{
        ses = con.createQueueSession(transacted, acknolegementMode);
        } catch (Exception e){System.out.println(e);} 
        }
    
    public void startSession()
    {
        try{
        ses = con.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (Exception e){System.out.println(e);} 
        }
     
     // this is for looking up the queue in the server, pass the queue name and it will return the queue
    public Queue GetQueue(String QueueName)
     {
         try{
         t=(Queue)ctx.lookup(QueueName);
         }  catch(Exception e){System.out.println(e);} 
         
         return t;
        }
     
    public Queue GetQueue()
     {
         return t;
     }
     
        
    // you are creating a sender from your session object so that you may send messages to your queue
    protected void CreateSender(){
        try{
            System.out.println(t.toString());
        sender = ses.createSender(t);
                } catch(JMSException e){System.out.println(e);}
    } 
    
    
    // this makes it so the receiver object is created so it will receive messages from your queue. 
    protected void CreateReciever() throws JMSException
    {
    reciever = ses.createReceiver(t);
    }
    
    public QueueSender getSender(){
        return sender;
    }
    
    
    // this marks the Text message specific section of our code
    public TextMessage msg;
    public MyListener listener;

    
    // this is for our deconstructor so it will close the connection.     
        @Override
    protected void finalize() throws JMSException, Throwable
    {
            try {
                con.close();
            } finally {
                super.finalize();
            }
    }
        
        
    // a standard getter so that we can get the receiver if we want it outside the class
    public QueueReceiver GetReciever()
   {
       return reciever;
   }
    
    // this creates the Text Message receiver 
    public MyListener CreateTextMsgReciever(String conFactory, String QueueName) throws JMSException, InterruptedException
    {
        InitCtx();
        CreateQConnection(conFactory);
        setQConnection();
        con.start();
        startSession();
        GetQueue(QueueName);
        CreateReciever();
        listener = new MyListener();
        
       reciever.setMessageListener(this.listener);
        
        return listener;        
    }
    
        // this was originally part of a class that was called TextMsg, but I forgot that java does not support multi inheritance and it would of been 2 layers down causing null exceptions. 
    // this method created the sender for our text message app. 
    public void CreateTextMsgSender(String conFactory, String QueueName, boolean transacted, int ackMode) throws JMSException
    {
        InitCtx();
        CreateQConnection(conFactory);
         setQConnection();
          startSession(transacted, ackMode);
         GetQueue(QueueName);
                 CreateSender();
       
        
        msg = ses.createTextMessage(); 
    }
    
    // this was originally part of a class that was called TextMsg, but I forgot that java does not support multi inheritance and it would of been 2 layers down causing null exceptions. 
    // this method created the sender for our text message app. 
    public void CreateTextMsgSender(String conFactory, String QueueName) throws JMSException
    {
        InitCtx();
        CreateQConnection(conFactory);
        
        setQConnection();
        startSession();
        GetQueue(QueueName);      
        CreateSender();
        msg = ses.createTextMessage(); 
    }
     }
    public class TopicConnect extends Connection{
         
        TopicSession pubSession;
        TopicPublisher publisher;
        TopicConnection connection;
        String username = "slum";
        Topic topic;
        String TpcName = "MSG";
        MyListener listener = new MyListener();
        TopicSession subSession;
        TopicSubscriber subscriber;

    public TopicConnect(String TpcName, String TpcFactory, String username) throws NamingException, JMSException {
            this.username = username;
            InitCtx();
            GetTopicConnection(TpcFactory);
            CreatePubSession();
            CreateSubSession();
            LookUpTopic(TpcName);
            CreatePublisher();
            CreateSubscriber();
            CreateDefaultChatListener();
            StartConnection();
            
        }
        
           @Override
    protected void finalize() throws JMSException, Throwable
    {
            try {
                CloseConnect();
            } finally {
                super.finalize();
            }
    }
        
    private void LookUpTopic(String topic) throws NamingException{
            this.topic = (Topic)ctx.lookup(topic);
        }
    public void writeMessage(String text) throws JMSException
         {
             TextMessage message = pubSession.createTextMessage();
             message.setText(text);
             publisher.publish(message);
         }
         
    private void GetTopicConnection(String Topic) throws NamingException, JMSException
        {
            TopicConnectionFactory conFactory = (TopicConnectionFactory)ctx.lookup(Topic);
            connection = conFactory.createTopicConnection();
        }
        
    private void CreatePubSession() throws JMSException{
            pubSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        }
    
    private void CreatePubSession(boolean transacted, int ACK) throws JMSException{
            pubSession = connection.createTopicSession(transacted, ACK);
        }
    
    private void CreateSubSession() throws JMSException{
            subSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        }
    
    private void CreateSubSession(boolean transacted, int ACK) throws JMSException{
            subSession = connection.createTopicSession(transacted, ACK);
        }
        
    private void CreatePublisher() throws JMSException
         {
             publisher = pubSession.createPublisher(topic);
         }
         
    private void CreateSubscriber() throws JMSException
         {
             subscriber = subSession.createSubscriber(topic, null, true);
         }
                   
    public void CreateDefaultChatListener() throws JMSException{
               subscriber.setMessageListener(listener);
           }
           
    public void CreateCustomListener(MessageListener l) throws JMSException{
                subscriber.setMessageListener(l);
           }
           
    public void StartConnection() throws JMSException
           {
               connection.start();
           }
    public void StopConnect() throws JMSException{
               connection.stop();
           }
    public void CloseConnect() throws JMSException{
               connection.close();
           }
         
     }

}
