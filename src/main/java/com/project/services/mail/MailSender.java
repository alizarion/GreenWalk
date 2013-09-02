package com.project.services.mail;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 31/07/12
 * Time: 21:23
 * To change this template use File | Settings | File Templates.
 */
public class MailSender {
    private IMailProvider mailProvider;
    private final static Logger LOG = Logger.getLogger(MailSender.class);

    public void setMailProvider (IMailProvider provider) {
        this.mailProvider = provider;
    }

    // option to set it up during construction
    public MailSender (IMailProvider provider) {
        this.mailProvider = provider;
    }

    public void sendEmail(EmailObject obj) {
        if(mailProvider == null)
            throw new RuntimeException("Need a mail provider to send email.");

        try {
            //Todo change for production
            LOG.info("Email Sent : \n" +
                    "Email Subject : "+ obj.getSubject() +"\n " +
                    "Email HTML Content : "+ obj.getHTML() );
            // mailProvider.send(obj);
        } catch (Exception e) {
            // do something here
        }
    }

}
