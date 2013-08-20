package com.project.entities;

import com.project.Helper;

import javax.persistence.*;
import java.io.File;

/**
 * @author selim@openlinux.fr
 *
 */
@Entity
@Table(schema= Helper.ENTITIES_CATALOG, name="eventsd")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name= Event.FIND_ALL,
        query="SELECT e FROM Event e")
@DiscriminatorColumn(name = "type")
public abstract class Event {

    public final static String FIND_ALL = "Event.FIND_ALL";

    @Id
    @TableGenerator(name="event_SEQ", table="sequence",  catalog = Helper.ENTITIES_CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy= GenerationType.TABLE, generator="event_SEQ")
    @Column(name="id")
    private Long id;

    @ManyToOne
    private Account owner;


    @Column(updatable = false, insertable = false)
    private String type;

    public File getEventPathDirectoryFile(){
        return new File(Helper.getUserDirectoryPath() +
                File.pathSeparator +
                this.owner.getId() + this.id.toString());
    }

}
