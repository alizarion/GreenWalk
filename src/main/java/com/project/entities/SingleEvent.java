package com.project.entities;

import com.project.Helper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(value = "SINGLE")
@NamedQuery(name= SingleEvent.FIND_ALL,
        query="SELECT se FROM SingleEvent se order by se.creationDate desc ")
public class SingleEvent extends Event {

    public final static String FIND_ALL = "SingleEvent.FIND_ALL";

    @OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(catalog = Helper.ENTITIES_CATALOG, name="events_wastes",
            joinColumns=@JoinColumn(name="event_id"),
            inverseJoinColumns=@JoinColumn(name="waste_id"))
    private Set<WasteGarbage> wastes = new HashSet<WasteGarbage>();

    public Set<WasteGarbage> getWastes() {
        return wastes;
    }


    public List<WasteGarbage> getGarbageAsList() {
        return new ArrayList<WasteGarbage>(this.wastes);
    }


    public void setWastes(Set<WasteGarbage> wastes) {
        this.wastes = wastes;
    }

    public void addNewGarbage(WasteGarbage  wasteGarbage){
        for (WasteGarbage garbage : this.wastes){
            if (garbage.equals(wasteGarbage)){
                garbage.setQuantity(garbage.getQuantity()
                        + wasteGarbage.getQuantity());
                return;
            }
        }
        this.wastes.add(wasteGarbage);
    }

    public void clearBeforePersist() {

        //clearing empty garbage
        for (WasteGarbage garbage :new ArrayList<WasteGarbage>(this.wastes)){
            if (garbage.getQuantity()<0){
                this.wastes.remove(garbage);
            }
        }
        for (Content content: new ArrayList<Content>(super.getContents())){
            if (content instanceof ImageContent){
                ((ImageContent) content).getImage().setTemporary(false);
            }
        }

    }


}
