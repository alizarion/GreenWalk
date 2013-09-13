package com.project.entities;

import com.project.Helper;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(value = "SINGLE")
public class SingleEvent extends Event {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(catalog = Helper.ENTITIES_CATALOG, name="events_wastes",
            joinColumns=@JoinColumn(name="event_id"),
            inverseJoinColumns=@JoinColumn(name="waste_id"))
    private Set<WasteGarbage> wastes = new HashSet<WasteGarbage>();


}
