package com.project.web.controlers;


import com.project.Helper;
import com.project.entities.Account;
import com.project.entities.EmailCounter;
import com.project.services.EntityFacade;
import com.project.services.mail.MailSender;
import com.project.services.mail.Mailer;
import com.project.services.mail.ResetPasswordEmail;
import com.project.services.mail.SMTPEmailProvider;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.mail.MessagingException;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 18/01/12
 * Time: 00:35
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ResetPasswordCtrl implements Serializable{


    private String email;

    @EJB
    EntityFacade facade;

    @EJB
    Mailer mailer;

    private boolean isValidEmail;
    private boolean isValidCapcha;



    public void resetPassword() throws NamingException, MessagingException, NoSuchAlgorithmException, IOException {
        Account account = facade.findAccountByEmail(email);
        if(account !=null){
            String newPass = Helper.getRandomHash();
            account.getCredential().setPassword(newPass);
            facade.mergeAccount(account);
            EmailCounter emailCounter =  new EmailCounter(account.getEmailAddress(),"reset password",new Date());
            facade.mergeEmailCounter(emailCounter);
            MailSender sender = new MailSender(new SMTPEmailProvider());
            sender.sendEmail(new ResetPasswordEmail(account, newPass));

            isValidCapcha = true;

        }
        else
        {
            isValidCapcha = true;
            email = null;
            isValidEmail = true;

        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isValidEmail() {
        return isValidEmail;
    }

    public boolean isValidCapcha() {
        return isValidCapcha;
    }

    public void setValidCapcha(boolean validCapcha) {
        isValidCapcha = validCapcha;
    }

    public void setValidEmail(boolean validEmail) {
        isValidEmail = validEmail;
    }

    public static String randomstring(int lo, int hi){
        int n = rand(lo, hi);
        byte b[] = new byte[n];
        for (int i = 0; i < n; i++)
            b[i] = (byte)rand('a', 'z');
        return new String(b, 0);
    }

    private static int rand(int lo, int hi){
        java.util.Random rn = new java.util.Random();
        int n = hi - lo + 1;
        int i = rn.nextInt(n);
        if (i < 0)
            i = -i;
        return lo + i;
    }

    public static String randomstring(){
        return randomstring(5, 25);
    }
}
