package com.project.entities;

import com.project.Helper;

import javax.persistence.*;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author selim@openlinux.fr
 *
 */
@Entity
@Table(schema= Helper.ENTITIES_CATALOG, name="event")
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

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(updatable = false, insertable = false)
    private String type;

    @OneToMany(mappedBy="event",
            fetch = FetchType.EAGER,
            orphanRemoval = true,cascade = CascadeType.ALL)
    private Set<Content> contents = new HashSet<Content>();

    @OneToMany(mappedBy="content",
            fetch = FetchType.EAGER,
            orphanRemoval = true,cascade = CascadeType.ALL)
    @OrderBy(value = "creationdate")
    private Set<Comment> comments = new HashSet<Comment>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Content> getContents() {
        return contents;
    }

    public void setContents(Set<Content> contents) {
        this.contents = contents;
    }

    public File getEventPathDirectoryFile(){

        File folder =  new File(Helper.getUserDirectoryPath() +
                File.separator +
                this.owner.getId() +
                File.separator +
                this.id.toString());
        if (!folder.exists()){
            folder.mkdirs();
        }
        return folder;
    }

}
