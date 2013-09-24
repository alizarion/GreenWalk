package com.project.entities;

import com.project.Helper;

import javax.persistence.*;
import java.io.File;
import java.util.*;

/**
 * @author selim@openlinux.fr
 *
 */
@Entity
@Table(schema= Helper.ENTITIES_CATALOG, name="event")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name= Event.FIND_ALL,
        query="SELECT e FROM Event e order by e.creationDate  desc ")
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

    @Column
    private Date creationDate;

    @Column(name = "title")
    private String title;

    @Column(name = "description",length = 2048)
    private String description;

    @Column(updatable = false, insertable = false)
    private String type;

    @OneToMany(mappedBy="event",
            fetch = FetchType.EAGER,
            orphanRemoval = true,cascade = CascadeType.ALL)
    private Set<Content> contents = new HashSet<Content>();

    @OneToMany(mappedBy="event",
            fetch = FetchType.EAGER,
            orphanRemoval = true,cascade = CascadeType.ALL)
    @OrderBy(value = "creationdate")
    private Set<Comment> comments = new HashSet<Comment>();

    protected Event() {
        this.creationDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getOwner() {
        return owner;
    }

    public List<Content> getContentAsList(){
        return new ArrayList<Content>(this.contents);
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public List<Comment> getCommentsAsList() {
        List<Comment> commentList =  new ArrayList<Comment>(comments);
        Collections.sort(commentList);
        return commentList;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getDescription() {
        return description;
    }

    public String getDescriptionShort() {
        if (description.length()>30){
            String subDescription =  description.substring(0,30);
            return subDescription.substring(0,subDescription.lastIndexOf(' '));
        } else {
            return this.description;
        }


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

    public  void addAllContents(HashSet<Content> contents){
        for (Content content : contents){
            content.getFile().setTemporary(false);
            content.setEvent(this);
            this.contents.add(content);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (creationDate != null ? !creationDate.equals(event.creationDate) :
                event.creationDate != null) return false;
        if (description != null ? !description.equals(event.description) :
                event.description != null) return false;
        if (id != null ? !id.equals(event.id) : event.id != null) return false;
        if (title != null ? !title.equals(event.title) : event.title != null) return false;
        if (type != null ? !type.equals(event.type) : event.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
