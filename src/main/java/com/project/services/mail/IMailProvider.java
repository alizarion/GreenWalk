package com.project.services.mail;

import javax.naming.NamingException;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 31/07/12
 * Time: 21:23
 * To change this template use File | Settings | File Templates.
 */
public interface IMailProvider {
    public void send(EmailObject obj) throws NamingException;
}

