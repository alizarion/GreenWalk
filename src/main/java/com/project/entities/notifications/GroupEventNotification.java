package com.project.entities.notifications;

import com.project.entities.Account;
import com.project.entities.GroupEvent;
import com.project.entities.notifications.Notification;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 27/12/12
 * Time: 14:02
 * To change this template use File | Settings | File Templates.
 */
@Entity
@DiscriminatorValue(value = "GroupEventNotification")
public class GroupEventNotification extends Notification implements Serializable {

    public static final String NOTIFICATION_LABEL = "notification-label-comment-FR";
    public static final String FIND_ACCOUNT_COMMENT_NOTIFICATION = "FIND_ACCOUNT_COMMENT_NOTIFICATION";
    public static final String COUNT_ACCOUNT_COMMENT_NOTIFICATION = "COUNT_ACCOUNT_COMMENT_NOTIFICATION";


    @ManyToOne(optional = true,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "group_event_id")
    private GroupEvent groupEvent;


    public GroupEventNotification() {
        super();
    }

    public GroupEventNotification(GroupEvent groupEvent, Account account) {
        super();
        super.setAccountListener(account);
        this.groupEvent = groupEvent;
    }

    public GroupEventNotification(GroupEvent groupEvent) {
        super();
        this.groupEvent = groupEvent;
    }

    public GroupEvent getGroupEvent() {
        return groupEvent;
    }

    public void setGroupEvent(GroupEvent groupEvent) {
        this.groupEvent = groupEvent;
    }

    public Account getNotificationFrom(){
        return this.groupEvent.getOwner();
    }

    public String getNotificationLabel(){
        final ResourceBundle resourceBundle =
                    ResourceBundle.getBundle("msg");
        return resourceBundle.getString(NOTIFICATION_LABEL);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupEventNotification)) return false;
        if (!super.equals(o)) return false;

        GroupEventNotification that = (GroupEventNotification) o;

        if (groupEvent != null ? !groupEvent.equals(that.groupEvent) : that.groupEvent != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (groupEvent != null ? groupEvent.hashCode() : 0);
        return result;
    }
}
