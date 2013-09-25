package com.project.entities;

import com.project.Helper;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 23/09/13
 * Time: 11:17
 * To change this template use File | Settings | File Templates.
 */
@Entity

@Table(schema= Helper.ENTITIES_CATALOG, name="group_event_subscriber")
public class GroupEventSubscriber {

    @Id
    @TableGenerator(name="group_event_subscriber_SEQ", table="sequence",  catalog = Helper.ENTITIES_CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy= GenerationType.TABLE, generator="group_event_subscriber_SEQ")
    @Column(name="id")
    private Long id;

    @Column
    private Date subscribingDate;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private GroupEvent groupEvent;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Account account;

    @Column
    private Boolean confirmed;

    public GroupEventSubscriber() {
        this.subscribingDate = new Date();
    }

    public GroupEventSubscriber(GroupEvent groupEvent, Account account,Boolean confirmed ) {
        this.subscribingDate = new Date();
        this.groupEvent = groupEvent;
        this.account = account;
        this.confirmed = confirmed;
    }


    public GroupEventSubscriber(GroupEvent groupEvent, Account account ) {
        this.subscribingDate = new Date();
        this.groupEvent = groupEvent;
        this.account = account;
        this.confirmed = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getSubscribingDate() {
        return subscribingDate;
    }

    public void setSubscribingDate(Date subscribingDate) {
        this.subscribingDate = subscribingDate;
    }

    public GroupEvent getGroupEvent() {
        return groupEvent;
    }

    public void setGroupEvent(GroupEvent groupEvent) {
        this.groupEvent = groupEvent;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    @Override
    public boolean equals(Object o) {

        GroupEventSubscriber that = (GroupEventSubscriber) o;

        if (account != null ? !account.equals(that.account) : that.account != null) return false;
        if (groupEvent != null ? !groupEvent.equals(that.groupEvent) : that.groupEvent != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result =  (groupEvent != null ? groupEvent.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        return result;
    }
}
