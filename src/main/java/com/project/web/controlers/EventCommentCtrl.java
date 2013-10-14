package com.project.web.controlers;

import com.project.entities.*;
import com.project.services.EntityFacade;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

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

    private final static Logger LOG = Logger.getLogger(EventCommentCtrl.class);


    private Event event ;

    @EJB
    EntityFacade facade;

    @Inject
    SessionAttributeCtrl sessionAttribute;

    private Comment newComment;

    private Comment selectedComment;

    private Boolean canSubscribe = false;

    private Account user;

    @PostConstruct
    private void postInit(){
        LOG.info("EventCommentCtrl : PostConstruct");

        FacesContext.getCurrentInstance().getViewRoot().getAttributes().put(
                EntityFacade.EF_NAME, this.facade);

        HttpServletRequest request  =  (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        String eventId = (String) request.getAttribute("eventId");
        this.event = facade.findEventById(Long.parseLong(eventId));
        this.user = facade.getActiveUser();
        if(this.user != null){
            this.newComment = new Comment(this.user);
            if (this.event instanceof  GroupEvent){

            }

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

    public SingleEvent getSingleEvent(){
        if (this.event instanceof SingleEvent){
            return (SingleEvent) this.event;
        }  else {
            return null;
        }
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
