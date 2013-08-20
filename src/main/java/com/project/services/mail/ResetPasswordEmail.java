package com.project.services.mail;

import com.project.Helper;
import com.project.entities.Account;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 31/07/12
 * Time: 21:37
 * To change this template use File | Settings | File Templates.
 */
public class ResetPasswordEmail extends EmailObject {

    public final static String MAIL_TEMPLATE_DIRECTORY ="reset-password-email";

    private Account registredAccount;

    private String newPassword;



    public Account getregistredAccount() {
        return registredAccount;
    }

    public void setregistredAccount(Account registredAccount) {
        this.registredAccount = registredAccount;
    }

    public ResetPasswordEmail() {
    }

    public ResetPasswordEmail(Account registredAccount, String newPass) {
        super();
        super.setToAddr(registredAccount.getEmailAddress());
        this.registredAccount = registredAccount;
        this.newPassword = newPass;

    }

    @Override
    public String getHTML() {
        try {
            VelocityEngine ve = this.getVelocityEngine();
            this.getVelocityContext().put("Account", this.registredAccount);
            this.getVelocityContext().put("newPass", this.newPassword);
            Properties properties = new Properties();
            properties.setProperty("file.resource.loader.path", Helper.getMailTemplatePathDirectory());
            ve.init(properties);
            Template template = ve.getTemplate(HTML_VM);
            final StringWriter emailWriter = new StringWriter();
            template.merge(this.getVelocityContext(), emailWriter);
            return emailWriter.toString().trim();
        } catch (final Exception e1) {
            return "";
        }
    }

    @Override
    public String getText() {
        try {
            VelocityEngine ve = this.getVelocityEngine();
            this.getVelocityContext().put("Account", this.registredAccount);
            this.getVelocityContext().put("newPass", this.newPassword);
            Properties properties = new Properties();
            properties.setProperty("file.resource.loader.path", Helper.getMailTemplatePathDirectory());
            ve.init(properties);
            Template template = ve.getTemplate(TEXT_VM);
            final StringWriter emailWriter = new StringWriter();
            template.merge(this.getVelocityContext(), emailWriter);

            return emailWriter.toString().trim();
        } catch (final Exception e1) {
            return "";
        }
    }

    @Override
    public String getSubject() {
        try {
            VelocityEngine ve = this.getVelocityEngine();
            this.getVelocityContext().put("Account", this.registredAccount);
            this.getVelocityContext().put("newPass", this.newPassword);
            Properties properties = new Properties();
            properties.setProperty("file.resource.loader.path", Helper.getMailTemplatePathDirectory());
            ve.init(properties);
            Template template = ve.getTemplate(SUBJECT_VM);
            final StringWriter emailWriter = new StringWriter();
            template.merge(this.getVelocityContext(), emailWriter);
            return emailWriter.toString().trim();
        } catch (final Exception e1) {
            return "";
        }

    }
}
