package com.project.services.mail;

import com.project.entities.Account;
import com.project.entities.CredentialsState;
import com.project.services.EntityFacade;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 15/01/12
 * Time: 22:39
 * To change this template use File | Settings | File Templates.
 */
@WebServlet(urlPatterns = "/mailer/mailConfirmation")
public class MailerControllerServlet  extends HttpServlet{

    @EJB
    EntityFacade facade;
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String name = request.getParameter("account");
        if(name != null){
            Account account = facade.findAccountByName(name);
            if (account == null){
                return;
            } else {
                if(account.getCredential().getState().equals(CredentialsState.P)){
                    account.getCredential().setState(CredentialsState.A);
                    facade.mergeAccount(account);
                    response.sendRedirect("/accountActivated.jsf?username="+ account.getCredential().getUserName() );
                    return;
                }
            }

        }
       response.sendRedirect("/login.jsf");

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request,response);
    }
}
