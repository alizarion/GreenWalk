package com.project.entities;

import com.project.Helper;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 24/09/13
 * Time: 11:13
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = Helper.ENTITIES_CATALOG, name = "private_message")
public class PrivateMessage {

    @Id
    @TableGenerator(name="PrivateMessage_SEQ", table="sequence",  catalog = Helper.ENTITIES_CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="PrivateMessage_SEQ")
    @Column(name="message_id")
    private Long id;

    @ManyToOne
    private PrivateConversation privateConversation;

    @Column
    private Date creationDate;

    @Column(length = 2048)
    private String message;

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

    public PrivateConversation getPrivateConversation() {
        return privateConversation;
    }

    public void setPrivateConversation(PrivateConversation privateConversation) {
        this.privateConversation = privateConversation;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        PrivateMessage that = (PrivateMessage) o;

        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (privateConversation != null ? !privateConversation.equals(
                that.privateConversation) : that.privateConversation != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = privateConversation != null ? privateConversation.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
