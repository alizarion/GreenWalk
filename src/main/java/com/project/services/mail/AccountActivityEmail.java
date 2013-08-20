package com.project.services.mail;

import com.project.Helper;
import com.project.entities.Account;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 31/07/12
 * Time: 21:37
 * To change this template use File | Settings | File Templates.
 */
public class AccountActivityEmail extends EmailObject {

    public final static String MAIL_TEMPLATE_DIRECTORY ="account-activity-email";

    private Account registredAccount;

    private Integer privates;

    private List<Account> localAccounts = new ArrayList<Account>();

    private Integer comments;

    public Account getRegistredAccount() {
        return registredAccount;
    }

    public void setRegistredAccount(Account registredAccount) {
        this.registredAccount = registredAccount;
    }

    public AccountActivityEmail() {
    }

    public AccountActivityEmail(Account registredAccount,
                                Integer privates,
                                Integer comments,
                                List<Account> agents) {
        super();
        super.setToAddr(registredAccount.getEmailAddress());
        this.registredAccount = registredAccount;
        this.privates =  privates;
        this.comments = comments;
        this.localAccounts =  agents;

    }

    @Override
    public String getHTML() {
        try {
            VelocityEngine ve = this.getVelocityEngine();
            this.getVelocityContext().put("notifiedagent", this.registredAccount);
            this.getVelocityContext().put("privates", this.privates);
            this.getVelocityContext().put("comments", this.comments);
            this.getVelocityContext().put("localAgents", this.localAccounts);

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
            this.getVelocityContext().put("notifiedagent", this.registredAccount);
            this.getVelocityContext().put("privates", this.privates);
            this.getVelocityContext().put("comments", this.comments);
            this.getVelocityContext().put("localAgents", this.localAccounts);
            Properties properties = new Properties();
            properties.setProperty("file.resource.loader.path",Helper.getMailTemplatePathDirectory());
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
            this.getVelocityContext().put("notifiedagent", this.registredAccount);
            this.getVelocityContext().put("privates", this.privates);
            this.getVelocityContext().put("comments", this.comments);
            this.getVelocityContext().put("localAgents", this.localAccounts);
            Properties properties = new Properties();
            properties.setProperty("file.resource.loader.path",Helper.getMailTemplatePathDirectory());
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
