package com.project.services.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 31/07/12
 * Time: 21:26
 * To change this template use File | Settings | File Templates.
 */

public class SMTPEmailProvider implements IMailProvider {




    public void send(EmailObject obj) throws NamingException {
        // use SMTP to send email using passed-in config

        // Properties props = new Properties();
        InitialContext ictx = new InitialContext();
        Session mailSession = (Session) ictx.lookup("java:/Mail");
// Session mailSessoin = Session.getDefaultInstance(props);
        //     String username = (String) props.get("mail.smtps.user");
        //   String password = (String) props.get("mail.smtps.password");

        final MimeMessage msg = new MimeMessage(mailSession);
        try {
            msg.setSubject(obj.getSubject());

            final Multipart mp = new MimeMultipart("alternative");

            final MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(obj.getText(), "text/plain");
            mp.addBodyPart(textPart);

            final MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(obj.getHTML(), "text/html");
            mp.addBodyPart(htmlPart);

            msg.setContent(mp );

            msg.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(obj.getToAddr()));

            Transport transport = mailSession.getTransport("smtp");

            transport.connect();
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        finally {

        }
    }
}