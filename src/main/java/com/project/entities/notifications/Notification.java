package com.project.entities.notifications;

import com.project.Helper;
import com.project.entities.Account;
import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 27/12/12
 * Time: 13:59
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = Helper.ENTITIES_CATALOG, name = "notification")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries({
        @NamedQuery(name= Notification.FIND_AGENT_NOTIFICATION,
                hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                query="select notification "
                        + " from Notification notification where  notification.accountListener = :accountId and" +
                        " notification.viewed = false"),
        @NamedQuery(name= Notification.COUNT_AGENT_RATING_NOTIFICATION,
                hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                query="select count(notification.id) "
                        + " from Notification notification where  notification.accountListener = :accountId " +
                        "and  notification.type = 'RatingNotification'"),
        @NamedQuery(name= Notification.COUNT_AGENT_COMMENT_NOTIFICATION,
                hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                query="select count(notification.id) "
                        + " from Notification notification where  notification.accountListener = :accountId " +
                        "and  notification.type = 'CommentNotification'")  })
@DiscriminatorColumn(name = "type")
@DiscriminatorOptions(force=true)
public abstract class Notification implements Serializable, Cloneable {

    public final static String FIND_AGENT_NOTIFICATION = "FIND_ACCOUNT_NOTIFICATION" ;
    public final static String COUNT_AGENT_RATING_NOTIFICATION = "COUNT_ACCOUNT_RATING_NOTIFICATION" ;
    public final static String COUNT_AGENT_COMMENT_NOTIFICATION = "COUNT_ACCOUNT_COMMENT_NOTIFICATION" ;

    @Id
    @TableGenerator(name="notification_SEQ", table="sequence", catalog = Helper.ENTITIES_CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy= GenerationType.TABLE, generator="notification_SEQ")
    @Column(name="notification_id")
    private Long id;

    @Column(name = "viewed")
    private Boolean viewed;

    @ManyToOne(cascade = {CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.DETACH})
    @JoinColumn(name="account_listener_id")
    private Account accountListener ;

    @Column(name = "creationdate")
    private Date creationDate;



    @Column(updatable = false,insertable = false,name = "type")
    private String type;


    public Notification() {
        this.creationDate = new Date();
        this.viewed = false;
    }



    public Boolean getViewed() {
        return viewed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccountListener() {
        return accountListener;
    }

    public void setAccountListener(Account accountListener) {
        this.accountListener = accountListener;
    }

    public abstract Account getNotificationFrom();



    public abstract String getNotificationLabel();

    public abstract String getNotificationOutcome();

    public abstract String getNotificationOutcomeParam();

    public void setViewed(Boolean viewed) {
        this.viewed = viewed;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    public Object clone() {
        Notification notification = null;
        try {
            // On récupère l'instance à renvoyer par l'appel de la
            // méthode super.clone()
            notification = (Notification) super.clone();
        } catch(CloneNotSupportedException cnse) {
            // Ne devrait jamais arriver car nous implémentons
            // l'interface Cloneable
            cnse.printStackTrace(System.err);
        }
        // on renvoie le clone
        return notification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;

        Notification that = (Notification) o;

        if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (viewed != null ? !viewed.equals(that.viewed) : that.viewed != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (viewed != null ? viewed.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        return result;
    }
}
