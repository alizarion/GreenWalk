package com.project.web.controlers;

import com.project.entities.Account;
import com.project.entities.AvatarImageFile;

import com.project.entities.Credential;
import com.project.services.EntityFacade;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 17/09/13
 * Time: 14:48
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class MemberProfileEditCtrl implements Serializable {


    @EJB
    EntityFacade facade;

    private Account account;

    private String previousPassword;

    private String newPassword;

    @Inject
    SessionAttributeCtrl sessionAttribute;

    ResourceBundle bundle = ResourceBundle.getBundle("lang");

    @PostConstruct
    public void postInit(){
        FacesContext.getCurrentInstance().getViewRoot().getAttributes().put(
                EntityFacade.EF_NAME, this.facade);
        HttpServletRequest request  =  (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        if (request.getUserPrincipal() != null){
            this.account =
                    facade.findAccountByName(request.getUserPrincipal().getName());
        }
    }


    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getPreviousPassword() {
        return previousPassword;
    }

    public void setPreviousPassword(String previousPassword) {
        this.previousPassword = previousPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void fileUploadListenerImageContent(FileUploadEvent event)
            throws JAXBException, IOException, CloneNotSupportedException {
        AvatarImageFile file = new AvatarImageFile(event.getFile().getFileName());
        file.setTemporary(true);
        this.account.setAvatarImageFile(file);
        this.account = this.facade.mergeAccount(this.account);
        this.account.getAvatarImageFile().writeFile(event.getFile().getInputstream());
        this.facade.mergeAccount(this.account);
    }

    public void resetPassword() throws NoSuchAlgorithmException {
        if (newPassword !=null){
            if (!newPassword.isEmpty()) {
                String actualSHA1Password = this.account.getCredential().getPassword();
                this.account.getCredential().setPassword(previousPassword);
                String previousSHA1Password =  this.account.getCredential().getPassword();
                if(actualSHA1Password.equals(previousSHA1Password)) {
                    // request.login(this.userAccount.getCredential().getUserName(),this.previousPassword);
                    this.account.getCredential().setPassword(this.newPassword);
                    this.account = facade.mergeAccount(this.account);
                    this.previousPassword =null;
                    this.newPassword = null;
                    FacesContext context = FacesContext.getCurrentInstance();

                    context.addMessage(null, new FacesMessage("Successful",
                            bundle.getString("component.credential.reset.password.success.message-" +
                                    sessionAttribute.getSelectedLang().getKey())));
                    RequestContext reqcontext = RequestContext.getCurrentInstance();
                }
                else {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    bundle.getString("bad.password.or.username-" +
                                            sessionAttribute.getSelectedLang().getKey()), null));

                }
            }
            else  {
                this.account = facade.mergeAccount(this.account);
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("Successful",
                        bundle.getString("component.credential.reset.password.success.message-" +
                        sessionAttribute.getSelectedLang().getKey())));

            }
        }  else {
            this.account = facade.mergeAccount(this.account);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Successful",
                    bundle.getString("component.credential.reset.password.success.message-" +
                    sessionAttribute.getSelectedLang().getKey())));
        }
        this.newPassword = null;
        this.previousPassword = null;
    }

    public void saveProfileChanges(){
        this.account = facade.mergeAccount(this.account);
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage("Successful",
                bundle.getString("common.update.account.profile.success.message-" +
                        sessionAttribute.getSelectedLang().getKey())));
    }
}
