package com.project.entities;

import javax.persistence.*;
import java.util.*;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(value = "GROUP")
public class GroupEvent extends Event {


    @OneToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST},
            optional = true)
    private Address address;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},
            fetch = FetchType.EAGER)
    private Set<GroupEventSubscriber> subscribers = new HashSet<GroupEventSubscriber>();

    @Column
    private Date eventDate;


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
        this.address = address;
    }

    public Set<GroupEventSubscriber> getSubscribers() {
        return subscribers;
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
        this.eventDate = eventDate;
    }

    public Integer getExpectedParticipants() {
        return expectedParticipants;
    }

    public void setExpectedParticipants(Integer expectedParticipants) {
        this.expectedParticipants = expectedParticipants;
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
}
