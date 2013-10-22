package com.project.entities;

import com.project.entities.notifications.*;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(value = "GROUP")
@NamedQueries({
        @NamedQuery(name= GroupEvent.FIND_ALL,
                query="SELECT ge FROM GroupEvent ge  order by ge.creationDate desc "),
        @NamedQuery(name= GroupEvent.FIND_BY_FILTER,
                query="SELECT ge FROM GroupEvent ge where (:city is null or ge.address.city like :city) " +
                        "and ( :country IS NULL or ge.address.country like  :country) " +
                        "and (:afterDate is null or ge.eventDate >= :afterDate) and " +
                        "(:beforeDate is null or ge.eventDate <= :beforeDate )" +
                        " order by ge.creationDate desc ")})

public class GroupEvent extends Event implements Notified {

    public final static String FIND_ALL = "GroupEvent.FIND_ALL";
    public final static String FIND_BY_FILTER = "GroupEvent.FIND_BY_FILTER";


    @OneToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST},
            optional = true)
    private Address address;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},
            fetch = FetchType.EAGER)
    private Set<GroupEventSubscriber> subscribers = new HashSet<GroupEventSubscriber>();

    @Column
    private Date eventDate;

    @Column
    private Date eventDateEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false,insertable = false,name = "owner_id")
    private Account groupEventOwner;

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

    public void setPartialAddress(Address address) {
        if (this.address!= null){
            if (!this.address.equals(address)){
                super.setEventUpdated(true);
                if (StringUtils.isNotEmpty(address.getCountry())){
                    this.address.setCountry(address.getCountry());
                }
                if (StringUtils.isNotEmpty(address.getCity())){

                    this.address.setCity(address.getCity());
                }
                if (StringUtils.isNotEmpty(address.getZipCode())){
                    this.address.setZipCode(address.getZipCode());
                }
                if (address.getPosition().getAsLatLng()!= null){
                    this.address.setPosition(address.getPosition());
                }
            }
        }
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

    public String getOverlayDescription(String localeKey){
        return   new SimpleDateFormat("EEEE, d MMMM yyyy HH:mm",
                LocaleUtils.toLocale(localeKey)).format(this.eventDate) + " : "+ "<br/><h3 class='titleHomeText5'>"+ this.getTitleShort() + "</h3><br/>"+
                "<a target='_blank' href='../group-pick-up/"+super.getId() +"'>"+
                this.getDescriptionShort()+"..more</a>";
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
        if (eventDate != null){
            this.eventDate = eventDate;
            if (this.eventDateEnd== null){
                this.eventDateEnd  = eventDate;
            }
        }
    }

    public void setEventDateEnd(Date eventDateEnd) {
        if (this.eventDateEnd != null && eventDateEnd != null){
            if (!(this.eventDateEnd.getTime() == eventDateEnd.getTime())){
                super.setEventUpdated(true);
            }
        }
        this.eventDateEnd = eventDateEnd;
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

    public Date getEventDateEnd() {
        return eventDateEnd;
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
