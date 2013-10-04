package com.project.entities;

import com.project.Helper;

import javax.persistence.*;
import java.util.*;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(value = "SINGLE")
@NamedQuery(name= SingleEvent.FIND_ALL,
        query="SELECT se FROM SingleEvent se  order by se.creationDate desc  ")
public class SingleEvent extends Event {

    public final static String FIND_ALL = "SingleEvent.FIND_ALL";

    @OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(catalog = Helper.ENTITIES_CATALOG, name="events_wastes",
            joinColumns=@JoinColumn(name="event_id"),
            inverseJoinColumns=@JoinColumn(name="waste_id"))
    private Set<WasteGarbage> wastes = new HashSet<WasteGarbage>();

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(updatable = false,insertable = false,name = "owner_id")
    private Account singleEventOwner;


    @Column(updatable = false, insertable = false)
    private String type;

    public Set<WasteGarbage> getWastes(){
        return wastes;
    }


    public List<WasteGarbage> getGarbageAsList() {
        return new ArrayList<WasteGarbage>(this.wastes);
    }


    public String getGarbageAsJSObjectList(){
        StringBuilder stringBuilder = new StringBuilder();
       boolean first = true;
        Iterator<WasteGarbage> iterator = this.getGarbageAsList().iterator();

        while (iterator.hasNext()){
            WasteGarbage current = iterator.next();
            if(first){
                stringBuilder.append(current.toString());
                first = false;
            } else {
                stringBuilder.append(',').append(current.toString());
            }
        }

        return stringBuilder.toString();
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
