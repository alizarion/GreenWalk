package com.project.entities;


import com.project.Helper;


import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.persistence.*;

import java.io.Serializable;
import java.util.*;

/**
 * @author selim.bensenouci
 */
@Entity
@Table(catalog = Helper.ENTITIES_CATALOG,name = "content")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries({
        @NamedQuery(name= Content.ALL_ABSOLUTE_CONTENT,
                hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                query="select content "
                        + " from Content content order by content.creationDate desc ")})
@DiscriminatorColumn(name = "type")
public abstract class Content implements Serializable, Cloneable{

    public final static Integer FIRST_COMMENTS = 5;
    public final static String FIND_BY_TAG_CAT_LABEL = "FIND_BY_TAG_CAT_LABEL";
    public final static String ALL_CONTENT = "ALL_CONTENT";
    public final static String ALL_FOLLOWING_SHOWABLE_CONTENT  = "ALL_FOLLOWING_SHOWABLE_CONTENT";
    public final static String ALL_SHOWABLE_LATEST_ACCOUNT_CONTENT = "ALL_SHOWABLE_LATEST_ACCOUNT_CONTENT";
    public final static String ALL_ABSOLUTE_CONTENT = "ALL_ABSOLUTE_CONTENT";
    public final static String ALL_SHOWABLE_CONTENT = "ALL_SHOWABLE_CONTENT";
    public final static String ALL_CONTENT_IDS = "ALL_CONTENT_IDS";
    public final static String FIND_BY_TAG_LABEL = "FIND_BY_TAG_LABEL";
    @Id
    @TableGenerator(name="Content_SEQ", table="sequence", catalog = Helper.ENTITIES_CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="Content_SEQ")
    @Column
    private Long id;

    @Column(length = 2048)
    private String comment;

    @Column(length = 2048)
    private String title;

    @Column(name = "creationDate")
    private Date creationDate;
    @Column(name = "lasModificationDate")
    private Date lasModificationDate;


    @OneToMany(mappedBy="content",
            fetch = FetchType.EAGER,
            orphanRemoval = true,cascade = CascadeType.ALL)
    @OrderBy(value = "creationdate")
    private Set<Comment> comments = new HashSet<Comment>();

    @Column
    private String label;

    @Column(nullable = false)
    private Boolean publish;

    @ManyToOne (optional = true,
            cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "event_id")
    private Event event;

    @Column
    private Integer position;

    @Column(name = "type",insertable = false,updatable = false)
    private String type;

    @Transient
    private Integer countView;

    //abstract methods

    public abstract  String getVideo();

    public abstract void setVideo(String video);

    public abstract String getResizedVideo(String width, String height);

    public abstract UploadedFile getFile();

    protected Content() {
        this.creationDate = new Date();
        this.publish = true;
    }




    public List<Comment> getCommentResume(){
        List<Comment> commentList  = new ArrayList<Comment>();
        int i = 0;
        for(Comment comment :this.comments){
            if (i < this.FIRST_COMMENTS ){
                commentList.add(comment);
                i++;
            }  else {
                return  commentList;
            }
        }

        return commentList;

    }

    public DataModel<Comment> getCommentResumeDateModel(){
        List<Comment> commentList  = new ArrayList<Comment>();
        int i = 0;
        for(Comment comment :this.comments){
            if (i < this.FIRST_COMMENTS ){
                commentList.add(comment);
                i++;
            }  else {
                return new ArrayDataModel<Comment>
                        (commentList.toArray(new Comment[commentList.size()] ));
            }
        }

        return new ArrayDataModel<Comment>
                (commentList.toArray(new Comment[commentList.size()] ));

    }

    public Integer getCountView() {
        if (this.countView != null){
            return this.countView;
        }   else {
            return 0;
        }
    }

    public void setCountView(Integer countView) {
        this.countView = countView;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getLabel() {
        return label;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        this.event.getContents().add(this);
    }

    public String getType() {
        return type;
    }

    public Boolean getPublish() {
        return publish;
    }

    public void setPublish(Boolean publish) {
        this.lasModificationDate = new Date();

        this.publish = publish;
    }


    public void setType(String type) {

        this.type = type;
    }


    public void setLabel(String label) {
        this.lasModificationDate =new Date();

        this.label = label;
    }



    public String getComment() {
        if (comment != null){
            if (comment.isEmpty()){
                return null;
            }
        }
        return comment;
    }


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLasModificationDate() {
        return lasModificationDate;
    }

    public void setLasModificationDate(Date lasModificationDate) {
        this.lasModificationDate = lasModificationDate;
    }


    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public List<Comment> getCommentsAsList(){
        if (this.comments != null) {
            return new ArrayList<Comment>(this.comments);
        } else {
            return new ArrayList<Comment>();
        }
    }

    public void setComment(String comment) {
        this.lasModificationDate =new Date();
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public Integer getPosition() {
        return position;
    }

    public String getIdToString(){
        return this.id.toString() ;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public boolean isVideoContent(){
        return this.type.equals("VIDEO");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Content)) return false;

        Content content = (Content) o;

        if (creationDate != null ? !creationDate.equals(content.creationDate) : content.creationDate != null)
            return false;
        if (id != null ? !id.equals(content.id) : content.id != null) return false;
        if (label != null ? !label.equals(content.label) : content.label != null) return false;
        if (type != null ? !type.equals(content.type) : content.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? Integer.parseInt(id.toString()) : 0;
        return result;
    }


    public boolean isImageContent(){
        //  if (this.type != null){
        return this.type.equals("IMAGE");
        /*}  else {
            return false;
        }   */

    }
    public Content clone() throws CloneNotSupportedException {
        Content content = (Content) super.clone();
        content.setId(null);
        content.setComment(null);

        return content;
    }



}
