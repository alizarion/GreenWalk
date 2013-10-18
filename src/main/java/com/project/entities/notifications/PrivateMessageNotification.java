package com.project.entities.notifications;

import com.project.entities.Account;
import com.project.entities.PrivateMessage;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 27/12/12
 * Time: 14:02
 * To change this template use File | Settings | File Templates.
 */
@Entity
@DiscriminatorValue(value = "PrivateMessageNotification")
public class PrivateMessageNotification extends Notification implements Serializable {

    public static final String NOTIFICATION_LABEL = "common.notifications.private.message";
    public static final String FIND_ACCOUNT_COMMENT_NOTIFICATION = "FIND_ACCOUNT_COMMENT_NOTIFICATION";
    public static final String COUNT_ACCOUNT_COMMENT_NOTIFICATION = "COUNT_ACCOUNT_COMMENT_NOTIFICATION";


    @ManyToOne(optional = true)
    @JoinColumn(name = "pc_id")
    private PrivateMessage privateMessage;

    @Column(updatable = false, insertable = false)
    private String type;

    public PrivateMessageNotification() {
        super();
    }

    @Override
    public Account getNotificationFrom() {
       return this.privateMessage.getSender();
    }

    public PrivateMessageNotification(
            PrivateMessage privateConversation, Account account) {
        super();
        super.setAccountListener(account);
        this.privateMessage = privateConversation;
    }

    public PrivateMessageNotification(
            PrivateMessage privateConversation) {
        super();
        this.privateMessage = privateConversation;
    }


    public PrivateMessage getPrivateMessage() {
        return privateMessage;
    }

    public void setPrivateMessage(PrivateMessage privateMessage) {
        this.privateMessage = privateMessage;
    }

    public String getNotificationLabel(){

        return NOTIFICATION_LABEL;
    }

    @Override
    public String getNotificationOutcome() {
        //TODO : not implemented yet
        return null;
    }

    @Override
    public String getNotificationOutcomeParam() {
        //TODO : not implemented yet
        return null;
    }


    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() !=
                o.getClass()) return false;
        if (!super.equals(o)) return false;

        PrivateMessageNotification that = (PrivateMessageNotification) o;

        if (privateMessage != null ?
                !privateMessage.equals(that.privateMessage) :
                that.privateMessage != null)
            return false;
        if (type != null ? !type.equals(that.type) :
                that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (privateMessage != null ? privateMessage.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
