/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.font.TextMeasurer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


public class eventListener implements MessageListener {

    EventLogic par;

    public  eventListener(EventLogic p) {
        par = p;
    }
    
    @Override
    public void onMessage(Message msg) {
        try {
            par.Directions = ((TextMessage)msg).getText();
        } catch (JMSException ex) {
            Logger.getLogger(eventListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
