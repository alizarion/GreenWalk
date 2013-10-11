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



    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(updatable = false,insertable = false,name = "owner_id")
    private Account singleEventOwner;


    @Column(updatable = false, insertable = false)
    private String type;


}
