package com.project.web.controlers;

import com.project.entities.*;
import com.project.services.EntityFacade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
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
public class EventCommentCtrl implements Serializable {

    private Event event ;

    @EJB
    EntityFacade facade;

    @Inject
    SessionAttributeCtrl sessionAttribute;

    private Comment newComment;

    private Comment selectedComment;

    @PostConstruct
    private void postInit(){
        FacesContext.getCurrentInstance().getViewRoot().getAttributes().put(
                EntityFacade.EF_NAME, this.facade);

        HttpServletRequest request  =  (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        String eventId = (String) request.getAttribute("eventId");
        this.event = facade.findEventById(Long.parseLong(eventId));
        if(FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() != null){
            Credential credential = this.facade.getCredentialByUserName(FacesContext.
                    getCurrentInstance().getExternalContext().getUserPrincipal().getName());
            this.newComment = new Comment(facade.getActiveUser());

        }


    }


    public Comment getSelectedComment() {
        return selectedComment;
    }

    public void setSelectedComment(Comment selectedComment) {
        this.selectedComment = selectedComment;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Comment getNewComment() {
        return newComment;
    }

    public void setNewComment(Comment newComment) {
        this.newComment = newComment;
    }

    public void submitComment(){
        if (this.selectedComment!= null){
            this.selectedComment.addAnswer(this.newComment);
            this.facade.mergeComment(this.selectedComment);
        }   else {
            this.newComment.setEvent(this.event);
            this.facade.mergeComment(this.newComment);
        }

    }
}
