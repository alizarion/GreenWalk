package com.project.entities.notifications;

import com.project.entities.Account;
import com.project.entities.Comment;

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
@DiscriminatorValue(value = "CommentNotification")
public class CommentNotification extends Notification implements Serializable {

    public static final String NOTIFICATION_LABEL = "notification-label-comment-FR";
    public static final String FIND_ACCOUNT_COMMENT_NOTIFICATION = "FIND_ACCOUNT_COMMENT_NOTIFICATION";
    public static final String COUNT_ACCOUNT_COMMENT_NOTIFICATION = "COUNT_ACCOUNT_COMMENT_NOTIFICATION";


    @ManyToOne(optional = true)
    @JoinColumn(name = "comment_id")
    private Comment comment;


    public CommentNotification() {
        super();
    }

    public CommentNotification(Comment comment,Account account) {
        super();
        super.setAccountListener(account);
        this.comment = comment;
    }

    public CommentNotification(Comment comment) {
        super();
        this.comment = comment;
    }


    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {

        this.comment = comment;
    }

    public Account getNotificationFrom(){
        return this.comment.getCommentOwner();
    }

    public String getNotificationLabel(){
        final ResourceBundle resourceBundle =
                    ResourceBundle.getBundle("msg");
        return resourceBundle.getString(NOTIFICATION_LABEL);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentNotification)) return false;
        if (!super.equals(o)) return false;

        CommentNotification that = (CommentNotification) o;

        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }
}
