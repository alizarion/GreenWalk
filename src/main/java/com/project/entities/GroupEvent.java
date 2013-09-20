package com.project.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(value = "GROUP")
public class GroupEvent extends Event {


    @OneToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST},
            optional = false)
    private Address address;

    @ManyToMany
    private Set<Account> subscribers = new HashSet<Account>();

    @Column
    private Date eventDate;

    public GroupEvent() {
        this.address = new Address();
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Account> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<Account> subscribers) {
        this.subscribers = subscribers;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        GroupEvent that = (GroupEvent) o;

        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (subscribers != null ? !subscribers.equals(that.subscribers) : that.subscribers != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = address != null ? address.hashCode() : 0;
        result = 31 * result + (subscribers != null ? subscribers.hashCode() : 0);
        return result;
    }
}
