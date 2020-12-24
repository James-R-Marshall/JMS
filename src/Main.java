


import java.io.IOException;
import javax.jms.JMSException;
import javax.naming.NamingException;




public class Main {

    public static void main(String[] args) throws JMSException, IOException, NamingException {
        EventLogic E = new EventLogic();
        E.mainloop();
     
    }
    
}
