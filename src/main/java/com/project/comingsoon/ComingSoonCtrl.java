package com.project.comingsoon;

import com.project.services.EntityFacade;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Named
@ConversationScoped
public class ComingSoonCtrl implements Serializable {


    @Inject
    EntityFacade  facade;

    private MailingList mailingList;


    @PostConstruct
    private void postInit(){
        FacesContext.getCurrentInstance().getViewRoot().getAttributes().put(
                EntityFacade.EF_NAME, this.facade);
        this.mailingList = new MailingList();
    }

    public MailingList getMailingList() {
        return mailingList;
    }

    public void setMailingList(MailingList mailingList) {
        this.mailingList = mailingList;
    }


    public void register(){
        HttpServletRequest httpServletRequest = (HttpServletRequest)
                FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ip = httpServletRequest.getRemoteAddr();
        mailingList.setRemoteAddress(ip);
        facade.registerFromComingSoon(this.mailingList);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,"Thank you for your interest!\n" +
                        "As soon as as we are ready we will" +
                        " send you an email with all the details.", null));


    }
}


