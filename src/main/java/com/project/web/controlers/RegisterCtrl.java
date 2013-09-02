package com.project.web.controlers;

import com.project.entities.Account;
import com.project.entities.SexeEnum;
import com.project.services.EntityFacade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 30/09/11
 * Time: 22:48
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class RegisterCtrl implements Serializable {

    private Account account = new Account();

    private String sexe;

    private Boolean legales;

    @EJB
    EntityFacade facade;

    @Inject
    SessionAttributeCtrl sessionAttribute;

    @PostConstruct
    private void postInit(){
        FacesContext.getCurrentInstance().getViewRoot().getAttributes().put(
                EntityFacade.EF_NAME, this.facade);
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getSexe() {
        return sexe;
    }

    public Boolean getLegales() {
        return legales;
    }

    public void setLegales(Boolean legales) {
        this.legales = legales;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void submit(ActionEvent event) throws IOException {
        final ResourceBundle resourceBundle =
                            ResourceBundle.getBundle("lang");
        if (legales){
            if (this.sexe.equals("Homme")){
                account.setSexe(SexeEnum.M);
            } else if (this.sexe.equals("Femme")){
                account.setSexe(SexeEnum.F);
            }
            account.setIndexed(true);
            account.setLikeEnabled(true);
            account.setCommentEnabled(true);

            facade.createAccount(account);
            FacesContext context = FacesContext.getCurrentInstance();
            context.getViewRoot().getAttributes().put("username", account.getCredential().getUserName());
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() +
                    "/registerSuccess.jsf?username=" +account.getCredential().getUserName());
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            resourceBundle.getString("CGU.validation.required-"+
                                    sessionAttribute.getSelectedLang().getKey()), null));
        }

    }
}
