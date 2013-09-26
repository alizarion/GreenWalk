package com.project.entities;

import com.project.entities.notifications.*;

import javax.persistence.*;
import java.util.*;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(value = "GROUP")
@NamedQuery(name= GroupEvent.FIND_ALL,
        query="SELECT ge FROM GroupEvent ge order by ge.creationDate desc ")
public class GroupEvent extends Event implements Notified {

    public final static String FIND_ALL = "GroupEvent.FIND_ALL";


    @OneToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST},
            optional = true)
    private Address address;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},
            fetch = FetchType.EAGER)
    private Set<GroupEventSubscriber> subscribers = new HashSet<GroupEventSubscriber>();

    @Column
    private Date eventDate;

    @Transient
    private Set<Account> invitedMember = new HashSet<Account>();

    @Transient
    private Boolean newSubscriber = false;

    @Column
    private Integer expectedParticipants;

    public GroupEvent() {
        this.address = new Address();
        this.expectedParticipants = 1;
        this.subscribers.add(new GroupEventSubscriber(this,getOwner()));
    }

    public GroupEvent(Account owner) {
        this.address = new Address();
        this.expectedParticipants = 1;
        setOwner(owner);
        this.subscribers.add(new GroupEventSubscriber(this,getOwner(),true));
    }


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        if (this.address!= null){
            if (!this.address.equals(address)){
                super.setEventUpdated(true);
            }
        }
        this.address = address;
    }

    public Boolean getEndedEvent(){
        if(this.eventDate.getTime()<(new Date()).getTime()){
            return true;
        }  else {
            return false;
        }
    }

    public Set<GroupEventSubscriber> getSubscribers() {
        return subscribers;
    }

    public void addSubscriber(GroupEventSubscriber eventSubscriber){
        for (GroupEventSubscriber subscriber : this.subscribers){
            if (subscriber.equals(eventSubscriber)){
                if (!subscriber.getConfirmed()) {
                    subscriber.setConfirmed(eventSubscriber.getConfirmed());
                    this.newSubscriber= true;
                    return;
                }
            }
        }
        this.subscribers.add(eventSubscriber);
        if (!eventSubscriber.getConfirmed()){
            this.invitedMember.add(eventSubscriber.getAccount());
        }
        this.newSubscriber= true;
    }

    public List<GroupEventSubscriber> getConfirmedSubscribersAsList() {
        List<GroupEventSubscriber> resultList = new ArrayList<GroupEventSubscriber>();
        for (GroupEventSubscriber groupEventSubscriber :this.subscribers){
            if (groupEventSubscriber.getConfirmed()){
                resultList.add(groupEventSubscriber);
            }
        }
        return resultList;
    }

    public List<GroupEventSubscriber> getUnConfirmedSubscribersAsList() {
        List<GroupEventSubscriber> resultList = new ArrayList<GroupEventSubscriber>();
        for (GroupEventSubscriber groupEventSubscriber :this.subscribers){
            if (!groupEventSubscriber.getConfirmed()){
                resultList.add(groupEventSubscriber);
            }
        }
        return resultList;
    }


    public void setSubscribers(Set<GroupEventSubscriber> subscribers) {
        this.subscribers = subscribers;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        if (this.eventDate!= null && eventDate != null){
            if (!(this.eventDate.getTime() ==eventDate.getTime())){
                super.setEventUpdated(true);
            }
        }
        this.eventDate = eventDate;

    }

    public Integer getExpectedParticipants() {
        return expectedParticipants;
    }

    public void setExpectedParticipants(Integer expectedParticipants) {
        this.expectedParticipants = expectedParticipants;
    }

    public Boolean getNewSubscriber() {
        return newSubscriber;
    }

    public Set<Account> getInvitedMember() {
        return invitedMember;
    }

    public void setInvitedMember(Set<Account> invitedMember) {
        this.invitedMember = invitedMember;
    }

    public void setNewSubscriber(Boolean newSubscriber) {
        this.newSubscriber = newSubscriber;
    }

    public  void restoreTransientAttributes(GroupEvent event){
        this.newSubscriber = event.getNewSubscriber();
        super.setEventUpdated(super.getEventUpdated());
        this.invitedMember = event.getInvitedMember();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        GroupEvent that = (GroupEvent) o;

        if (address != null ? !address.equals(that.address) : that.address != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public List<Notification> pushNotifications() {
        List<Notification> notifications = new ArrayList<Notification>();
        if (super.getEventUpdated()){
            for(GroupEventSubscriber subscriber :this.subscribers){
                if (subscriber.getConfirmed()){
                    if (!subscriber.getAccount().equals(getOwner())){
                        notifications.add(new GroupEventUpdatedNotification(
                                this, subscriber.getAccount()));
                    }
                }
            }
        }
        if (this.newSubscriber){
            notifications.add(new GroupEventSubscriptionNotification(this, getOwner()));
        }

        for (Account member: invitedMember){
            notifications.add(new GroupEventInvitationNotification(this, member));
        }
        return  notifications;
    }
}
