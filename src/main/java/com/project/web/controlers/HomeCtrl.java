package com.project.web.controlers;

import com.project.entities.SingleEvent;
import com.project.services.EntityFacade;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * @author selim@openlinux.fr.
 */
@Named
@ConversationScoped
public class HomeCtrl implements Serializable {


    @Inject
    EntityFacade facade;

    private List<SingleEvent> singleEvents;

    @PostConstruct
    private void postInit(){
        FacesContext.getCurrentInstance().getViewRoot().getAttributes().put(
                EntityFacade.EF_NAME, this.facade);
        this.singleEvents = facade.findLastSingleEvents();
    }


    public List<SingleEvent> getSingleEvents() {
        return singleEvents;
    }
}
