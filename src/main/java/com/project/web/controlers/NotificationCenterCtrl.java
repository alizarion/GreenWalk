package com.project.web.controlers;

import com.project.entities.Account;
import com.project.entities.Comment;
import com.project.entities.Event;
import com.project.entities.GroupEvent;
import com.project.entities.notifications.Notification;
import com.project.services.EntityFacade;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 30/09/11
 * Time: 22:48
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class NotificationCenterCtrl implements Serializable {

    private final static Logger LOG = Logger.getLogger(NotificationCenterCtrl.class);

    @EJB
    EntityFacade facade;

    @Inject
    SessionAttributeCtrl sessionAttribute;

    private List<Notification> notifications;

    private Notification selectedNotification;

    @PostConstruct
    private void postInit(){
        LOG.info("NotificationCenterCtrl : PostConstruct");
        FacesContext.getCurrentInstance().getViewRoot().getAttributes().put(
                EntityFacade.EF_NAME, this.facade);
        this.notifications = facade.getActiveAccountNotifications();
    }

    public List<Notification> getNotifications() {
        return notifications;
    }


    public void consumeAllLoggedUserNotifications(){
        Account loggedUser  = this.facade.getActiveUser();
        loggedUser.consumeAllNotifications();
        this.facade.mergeAccount(loggedUser);
    }



    public List<Notification> getUnViewedNotifications() {
        List<Notification> unViewedNotifications = new ArrayList<Notification>();
        for (Notification notification:this.notifications ){
            if (!notification.getViewed()){
                unViewedNotifications.add(notification);
            }
        }
        return unViewedNotifications;
    }

    public Notification getSelectedNotification() {
        return selectedNotification;
    }

    public void setSelectedNotification(Notification selectedNotification) {
        this.selectedNotification = selectedNotification;
    }

    public void notificationViewed(){

    }
}
