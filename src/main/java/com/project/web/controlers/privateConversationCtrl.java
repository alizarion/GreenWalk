package com.project.web.controlers;

import com.project.entities.Account;
import com.project.entities.PrivateMessage;
import com.project.services.EntityFacade;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

/**
 * @author selim@openlinux.fr.
 */
@ManagedBean
@ViewScoped
public class PrivateConversationCtrl implements Serializable  {

    private final static Logger LOG = Logger.getLogger(PrivateConversationCtrl.class);

    @Inject
    EntityFacade facade;


    @Inject
    SessionAttributeCtrl sessionAttribute;

    private Account converseWith;

    private Account currentUser;

    private PrivateMessage privateMessage = new PrivateMessage();

    @PostConstruct
    private void postInit(){
        LOG.info("privateConversationCtrl : PostConstruct");
        FacesContext.getCurrentInstance().getViewRoot().getAttributes().put(
                EntityFacade.EF_NAME, this.facade);
        this.currentUser = facade.loadAccountPrivateMessages(facade.getActiveUser());
        HttpServletRequest request  =  (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        String convId = (String) request.getAttribute("eventId");
        if(convId!= null){
            this.converseWith = facade.findAccountById(Long.parseLong(convId));
            this.privateMessage.setSender(this.currentUser);
            this.privateMessage.setReceiver(this.converseWith);
        }
    }

    public List<PrivateMessage>  getConversation(){
        return  this.currentUser.getConversationWith(converseWith);
    }

    public Account getConverseWith() {
        return converseWith;
    }

    public Account getCurrentUser() {
        return currentUser;
    }

    public PrivateMessage getPrivateMessage() {
        return privateMessage;
    }

    public void setPrivateMessage(PrivateMessage privateMessage) {
        this.privateMessage = privateMessage;
    }

    public void sendPrivateMessage(){
        facade.sendPrivateMessage(this.privateMessage);

    }
}
