package com.project.entities;

import com.project.Helper;
import com.project.entities.notifications.Notification;
import com.project.entities.notifications.Notified;
import com.project.entities.notifications.PrivateMessageNotification;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 24/09/13
 * Time: 11:13
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = Helper.ENTITIES_CATALOG, name = "private_message")
public class PrivateMessage implements Notified, Comparable<PrivateMessage>  {

    @Id
    @TableGenerator(name="PrivateMessage_SEQ", table="sequence",  catalog = Helper.ENTITIES_CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="PrivateMessage_SEQ")
    @Column(name="message_id")
    private Long id;


    @Column
    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "message_sender")
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "message_receiver")
    private Account receiver;

    @Column(length = 2048)
    private String message;

    public PrivateMessage() {
        this.creationDate = new Date();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        PrivateMessage that = (PrivateMessage) o;

        if (message != null ? !message.equals(that.message) : that.message != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);

        return result;
    }

    @Override
    public List<Notification> pushNotifications() {
        List<Notification> notifications = new ArrayList<Notification>();
        notifications.add(new PrivateMessageNotification(this,getReceiver()));
        return notifications;
    }

    @Override
    public int compareTo(PrivateMessage o) {
        Date date1 = this.creationDate;
        Date date2 = o.getCreationDate();
        if (date1.after(date2)){
            return 1;
        }
        else {
            return -1;
        }
    }
}
