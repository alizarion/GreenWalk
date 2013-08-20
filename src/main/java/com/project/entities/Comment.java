package com.project.entities;



import com.project.Helper;

import javax.persistence.*;
import java.io.Serializable;
import java.security.Principal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 04/05/12
 * Time: 12:59
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = Helper.ENTITIES_CATALOG, name = "comments")
public class Comment implements Serializable {

    @Id
    @TableGenerator(name="comments_SEQ", table="sequence",  catalog = Helper.ENTITIES_CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="comments_SEQ")
    @Column(name="comment_id")
    private Long id;

    @Column(name = "value")
    private String value;

    @Column(name = "creationdate")
    @OrderColumn
    private Date creationdate;


    @Column
    private Boolean signaled;


    @ManyToOne
    @JoinColumn(name="event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name="content_id")
    private Content content;

    @ManyToOne
    @JoinColumn(name="id")
    private Account commentOwner;


    public Comment() {
        this.creationdate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getSignaled() {
        return signaled;
    }

    public void setSignaled(Boolean signaled) {
        this.signaled = signaled;
    }

    public Date getCreationdate() {
        return creationdate;
    }


    public Account getCommentOwner() {
        return commentOwner;
    }

    public void setCommentOwner(Account commentOwner) {
        this.commentOwner = commentOwner;
    }


    public Boolean isPrincipalCommentOwner(Principal principal){
        if (principal != null){
            if (principal.getName().equalsIgnoreCase(this.getCommentOwner().getCredential().getUserName())){
                return true;
            }
            return false;
        }
        return false;
    }

    @PrePersist
    public void onPrePersist(){
        /* CommentNotification notification = new CommentNotification();
   notification.setComment(this);
   notification.setDestinateAccount(this.getContent().getSubCategory().getCategory().getAccount());
   this.notification = notification;
   this.lastmodificationdate = new Date();  */
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;

        Comment comment = (Comment) o;

        if (commentOwner != null ? !commentOwner.equals(comment.commentOwner) : comment.commentOwner != null)
            return false;
        if (creationdate != null ? !creationdate.equals(comment.creationdate) : comment.creationdate != null)
            return false;
        if (event != null ? !event.equals(comment.event) : comment.event != null) return false;
        if (id != null ? !id.equals(comment.id) : comment.id != null) return false;
        if (signaled != null ? !signaled.equals(comment.signaled) : comment.signaled != null) return false;
        if (value != null ? !value.equals(comment.value) : comment.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (creationdate != null ? creationdate.hashCode() : 0);
        result = 31 * result + (signaled != null ? signaled.hashCode() : 0);
        result = 31 * result + (event != null ? event.hashCode() : 0);
        result = 31 * result + (commentOwner != null ? commentOwner.hashCode() : 0);
        return result;
    }
}
