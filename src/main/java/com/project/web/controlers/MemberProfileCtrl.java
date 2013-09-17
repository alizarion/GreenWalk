package com.project.web.controlers;

import com.project.entities.Account;
import com.project.services.EntityFacade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 17/09/13
 * Time: 14:48
 * To change this template use File | Settings | File Templates.
 */
@Named
@ViewScoped
public class MemberProfileCtrl implements Serializable {


    @EJB
    EntityFacade facade;

    private Account account;


    @PostConstruct
    public void postInit(){
        FacesContext.getCurrentInstance().getViewRoot().getAttributes().put(
                EntityFacade.EF_NAME, this.facade);
        HttpServletRequest request  =  (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        String username = (String) request.getAttribute("username");

        this.account = facade.findAccountByName(username);
    }


    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
