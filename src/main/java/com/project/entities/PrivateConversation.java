package com.project.entities;

import com.project.Helper;
import com.project.entities.notifications.Notification;
import com.project.entities.notifications.Notified;
import com.project.entities.notifications.PrivateMessageNotification;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 24/09/13
 * Time: 11:13
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = Helper.ENTITIES_CATALOG, name = "private_conversation")
public class PrivateConversation implements Notified {

    @Id
    @TableGenerator(name="PrivateConversation_SEQ", table="sequence",  catalog = Helper.ENTITIES_CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="PrivateConversation_SEQ")
    @Column(name="conversation_id")
    private Long id;

    @ManyToOne
    private Account sender;

    @ManyToOne
    private Account receiver;

    @OneToMany
    private Set<PrivateMessage> privateMessages = new HashSet<PrivateMessage>();

    public PrivateConversation() {

    }


    public PrivateConversation(Account sender, Account receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    public Account getReceiver() {
        return receiver;
    }

    public void setReceiver(Account receiver) {
        this.receiver = receiver;
    }

    public Set<PrivateMessage> getPrivateMessages() {
        return privateMessages;
    }

    public void setPrivateMessages(Set<PrivateMessage> privateMessages) {
        this.privateMessages = privateMessages;
    }

    public void addNewMessage(PrivateMessage message){
        this.privateMessages.add(message);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        PrivateConversation that = (PrivateConversation) o;

        if (receiver != null ? !receiver.equals(that.receiver) : that.receiver != null) return false;
        if (sender != null ? !sender.equals(that.sender) : that.sender != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sender != null ? sender.hashCode() : 0;
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        return result;
    }

    @Override
    public List<Notification>  pushNotifications() {
        List<Notification>  notifications =new ArrayList<Notification>();
        for (PrivateMessage privateMessage :  this.privateMessages){
            if (privateMessage.getId() == null){
                notifications.add(new PrivateMessageNotification(this,receiver));
                return notifications;
            }
        }
        return notifications;
    }
}
