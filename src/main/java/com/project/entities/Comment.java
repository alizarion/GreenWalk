package com.project.entities;



import com.project.Helper;
import com.project.entities.notifications.CommentNotification;
import com.project.entities.notifications.Notification;
import com.project.entities.notifications.Notified;

import javax.persistence.*;
import java.io.Serializable;
import java.security.Principal;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 04/05/12
 * Time: 12:59
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = Helper.ENTITIES_CATALOG, name = "comments")
public class Comment implements Serializable, Comparable,Notified {

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
    @JoinColumn(name="answerto_id")
    private Comment answerTo;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "answerTo",
            cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<Comment> answers = new HashSet<Comment>();

    @ManyToOne
    @JoinColumn(name="content_id")
    private Content content;

    @ManyToOne
    @JoinColumn(name="id")
    private Account commentOwner;


    public Comment() {
        this.creationdate = new Date();
    }

    public Comment(Event event) {
        this.creationdate = new Date();
        this.event = event;
    }

    public Comment(Account account) {
        this.creationdate = new Date();
        this.commentOwner = account;
    }

    public Comment(Event event,Account account) {
        this.creationdate = new Date();
        this.event = event;
        if (this.event != null){
            this.event.getComments().add(this);
        }
        this.commentOwner = account;
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

    public Set<Comment> getAnswers() {
        if(this.answers == null){
            this.answers = new HashSet<Comment>();
        }
        return answers;
    }

    public void setAnswers(Set<Comment> answers) {
        this.answers = answers;
    }

    public List<Comment> getAnswersAsList() {
        List<Comment> comments = new ArrayList<Comment>(answers);
        Collections.sort(comments);
        return comments;
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

    public Event getEvent() {
        return event;
    }

    public Boolean getHaveAnswer(){
        return (this.answers.size() >0);
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Comment getAnswerTo() {
        return answerTo;
    }

    public void setAnswerTo(Comment answerTo) {
        this.answerTo = answerTo;
    }

    public void addAnswer(Comment answer){
        answer.setAnswerTo(this);

        this.answers.add(answer);
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

    @Override
    public int compareTo(Object o) {
        Comment content1 = (Comment) o;
        Comment content2 = (Comment) this;
        Date creationDate1 = content1.getCreationdate();
        Date creationDate2 =  content2.getCreationdate();
        if (creationDate1.before(creationDate2) ) {
            return +1;
        } else{
            return -1;

        }
    }


    @Override
    public List<Notification>  pushNotifications() {
        List<Notification> notifications = new ArrayList<Notification>();
        if (this.getAnswerTo() != null){
            Comment answerto =this.getAnswerTo();
            if (!commentOwner.equals(answerto.getCommentOwner())){
                notifications.add(new CommentNotification(this,
                        answerto.getCommentOwner()));
            }

            while (answerto.getEvent() == null &&
                    answerto.getAnswerTo() != null ){
                answerto =answerto.getAnswerTo();
            }

            if (!commentOwner.equals(answerto.getCommentOwner())){
                notifications.add(new CommentNotification(this,
                        answerto.getCommentOwner()));
            }
        } else {
            if (!commentOwner.equals(getEvent().getOwner())){
                notifications.add(new CommentNotification(this,
                        getEvent().getOwner()));
            }
        }
        return notifications;

    }

}
