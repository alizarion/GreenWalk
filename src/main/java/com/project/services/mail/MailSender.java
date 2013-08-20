package com.project.services.mail;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 31/07/12
 * Time: 21:23
 * To change this template use File | Settings | File Templates.
 */
public class MailSender {
    private IMailProvider mailProvider;

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
            mailProvider.send(obj);
        } catch (Exception e) {
            // do something here
        }
    }

}
