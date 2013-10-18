package com.project.web.controlers;

import com.project.entities.Account;
import com.project.entities.PrivateConversation;
import com.project.services.EntityFacade;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@ManagedBean
@ViewScoped
public class privateConversationCtrl implements Serializable {

    private final static Logger LOG = Logger.getLogger(privateConversationCtrl.class);

    @Inject
    EntityFacade facade;


    @Inject
    SessionAttributeCtrl sessionAttribute;

    private PrivateConversation privateConversation;

    private Account account;

    @PostConstruct
    private void postInit(){
        LOG.info("privateConversationCtrl : PostConstruct");
        FacesContext.getCurrentInstance().getViewRoot().getAttributes().put(
                EntityFacade.EF_NAME, this.facade);
        this.account = facade.getActiveUser();
        HttpServletRequest request  =  (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        String convId = (String) request.getAttribute("convId");
       this.privateConversation = facade.findConversationById(Long.parseLong(convId));
        if (privateConversation != null){
            for (PrivateConversation privateConv : this.account.)
        }


    }

    public PrivateConversation getPrivateConversation() {
        return privateConversation;
    }

    public void setPrivateConversation(PrivateConversation privateConversation) {
        this.privateConversation = privateConversation;
    }
}
