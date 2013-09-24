package com.project.entities.notifications;

import com.project.entities.Account;
import com.project.entities.PrivateConversation;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@DiscriminatorValue(value = "PrivateMessageNotification")
public class PrivateMessageNotification extends Notification implements Serializable {

    public static final String NOTIFICATION_LABEL = "notification-label-comment-FR";
    public static final String FIND_ACCOUNT_COMMENT_NOTIFICATION = "FIND_ACCOUNT_COMMENT_NOTIFICATION";
    public static final String COUNT_ACCOUNT_COMMENT_NOTIFICATION = "COUNT_ACCOUNT_COMMENT_NOTIFICATION";


    @ManyToOne(optional = true)
    @JoinColumn(name = "pc_id")
    private PrivateConversation privateConversation;


    public PrivateMessageNotification() {
        super();
    }

    public PrivateMessageNotification(
            PrivateConversation privateConversation, Account account) {
        super();
        super.setAccountListener(account);
        this.privateConversation = privateConversation;
    }

    public PrivateMessageNotification(
            PrivateConversation privateConversation) {
        super();
        this.privateConversation = privateConversation;
    }


    public PrivateConversation getPrivateConversation() {
        return privateConversation;
    }

    public void setPrivateConversation(
            PrivateConversation privateConversation) {
        this.privateConversation = privateConversation;
    }

    public Account getNotificationFrom(){
        return this.privateConversation.getSender();
    }

    public String getNotificationLabel(){
        final ResourceBundle resourceBundle =
                    ResourceBundle.getBundle("msg");
        return resourceBundle.getString(NOTIFICATION_LABEL);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrivateMessageNotification)) return false;
        if (!super.equals(o)) return false;

        PrivateMessageNotification that = (PrivateMessageNotification) o;

        if (privateConversation != null ? !privateConversation.equals(
                that.privateConversation) :
                that.privateConversation != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (privateConversation != null ?
                privateConversation.hashCode() : 0);
        return result;
    }
}
