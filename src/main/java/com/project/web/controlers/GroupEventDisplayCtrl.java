package com.project.web.controlers;

import com.project.entities.*;
import com.project.services.EntityFacade;

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
public class GroupEventDisplayCtrl implements Serializable {

    private GroupEvent event ;

    @EJB
    EntityFacade facade;

    @Inject
    SessionAttributeCtrl sessionAttribute;

    private Comment newComment;

    private Comment selectedComment;

    private Boolean canSubscribe = true;

    private Account user;

    @PostConstruct
    private void postInit(){
        FacesContext.getCurrentInstance().getViewRoot().getAttributes().put(
                EntityFacade.EF_NAME, this.facade);

        HttpServletRequest request  =  (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        String eventId = (String) request.getAttribute("eventId");
        this.event = (GroupEvent) facade.findEventById(Long.parseLong(eventId));
        this.user = facade.getActiveUser();
        if(this.user != null){
            this.newComment = new Comment(this.user);
            for (GroupEventSubscriber subscriber : this.event.getConfirmedSubscribersAsList()){
                if (subscriber.getAccount().equals(this.user)){
                    canSubscribe = false;
                }
            }
        } else {
            canSubscribe = false;
        }
    }

    public Boolean getCanSubscribe() {
        return canSubscribe;
    }

    public void setCanSubscribe(Boolean canSubscribe) {
        this.canSubscribe = canSubscribe;
    }

    public Comment getSelectedComment() {
        return selectedComment;
    }

    public void setSelectedComment(Comment selectedComment) {
        this.selectedComment = selectedComment;
    }

    public GroupEvent getEvent() {
        return event;
    }

    public void setEvent(GroupEvent event) {
        this.event = event;
    }

    public Comment getNewComment() {
        return newComment;
    }

    public void setNewComment(Comment newComment) {
        this.newComment = newComment;
    }

    public void subscribe(){
        GroupEventSubscriber  subscriber = new GroupEventSubscriber(this.event,this.user,true);
        this.event.addSubscriber(subscriber);
        facade.submitGroupEvent(this.event);
        canSubscribe = false;
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
