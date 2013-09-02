package com.project.services.mail;

import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Serializable;

/**
 * @author selim.bensenouci
 */
@Stateless
public class Mailer implements Serializable {

    private Session mailSession;

    public void sendMsg(String email, String subject, String body)
            throws MessagingException, NamingException {
        // Properties props = new Properties();
        InitialContext ictx = new InitialContext();
        Session mailSession = (Session) ictx.lookup("java:/Mail");
// Session mailSessoin = Session.getDefaultInstance(props);
        //     String username = (String) props.get("mail.smtps.user");
        //   String password = (String) props.get("mail.smtps.password");

        MimeMessage message = new MimeMessage(mailSession);
        message.setSubject(subject);

        message.setRecipients(javax.mail.Message.RecipientType.TO,
                javax.mail.internet.InternetAddress.parse(email, false));
        message.setText(body);
        message.saveChanges();

        Transport transport = mailSession.getTransport("smtp");
        try {
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            System.out.print("Message Sent");
        }
        finally {
            transport.close();
        }
    }
}