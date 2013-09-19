package com.project.entities;

import com.project.Helper;
import org.apache.commons.lang.StringEscapeUtils;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;
import java.util.*;

/**
 * @author selim.bensenouci
 */
@Entity
@Table(catalog = Helper.ENTITIES_CATALOG, name = "account")
@NamedQueries({

        @NamedQuery(name= Account.FIND_LAST_CREATED,
                hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                query="select compte "
                        + " from Account compte where compte.credential.state = " +
                        "com.project.entities.CredentialsState.A " +
                        "order by compte.creationDate desc"),
        @NamedQuery(name= Account.FIND_ACCOUNT_BY_NAME,
                hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                query="select compte"
                        + " from Account compte "
                        + " where compte.credential.userName"
                        + "= :name "),
        @NamedQuery(name= Account.FIND_ALL_NOT_NOTIFIED,
                hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                query="select compte"
                        + " from Account compte "
                        + " where "
                        + "compte.lastNotificationMailDate < :date or compte.lastNotificationMailDate  = null"),
        @NamedQuery(name= Account.FIND_ACCOUNT_BY_EMAIL,
                query="select compte"
                        + " from Account compte "
                        + " where compte.emailAddress"
                        + "= :email ")})
public class Account implements Serializable {


    public static final String EL_NAME ="el_account";
    public static final String FIND_ACCOUNT_BY_NAME ="FIND_ACCOUNT_BY_NAME";
    public static final String FIND_ACCOUNT_BY_EMAIL ="FIND_ACCOUNT_BY_EMAIL";
    public static final String FIND_LAST_CREATED ="FIND_LAST_CREATED";
    public static final String FIND_ALL_NOT_NOTIFIED ="FIND_ALL_NOT_NOTIFIED";

    @Id
    @TableGenerator(name="Account_SEQ", table="sequence", catalog = Helper.ENTITIES_CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="Account_SEQ")
    @Column
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private Date creationDate;

    @Column
    private Date birthDay;

    @Column(unique = true)
    private String emailAddress;

    @Enumerated(EnumType.STRING)
    @Column(name="sexe")
    private SexeEnum sexe;

    @Column
    private String phoneNumber;

    @Column
    private String localisation;


    @Column
    private Boolean indexed;

    @Column
    private Date lastNotificationMailDate;

    @Column(name="commentenabled")
    private Boolean commentEnabled;

    @Column(name="likeenabled")
    private Boolean likeEnabled;

    @OneToOne(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable(name = "avatarImageFile_account", catalog = Helper.ENTITIES_CATALOG,
            joinColumns = @JoinColumn(name="account_fk"),
            inverseJoinColumns = @JoinColumn(name="avatarImageFile_fk"))
    private AvatarImageFile avatarImageFile;


    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinTable(schema=Helper.ENTITIES_CATALOG, name="account_and_followers",
            joinColumns=@JoinColumn(name="account_id"),
            inverseJoinColumns=@JoinColumn(name="followers_id"))
    private Set<Account> following = new HashSet<Account>();

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinTable(schema=Helper.ENTITIES_CATALOG, name="account_and_followers",
            joinColumns=@JoinColumn(name="followers_id"),
            inverseJoinColumns=@JoinColumn(name="account_id"))
    private Set<Account> followers = new HashSet<Account>();

    @OneToMany(mappedBy = "owner",
            fetch = FetchType.EAGER,
            targetEntity = Event.class)
    private Set<SingleEvent> singleEvents;

    @OneToMany(mappedBy = "owner",
            fetch = FetchType.EAGER,
            targetEntity = Event.class)
    private Set<GroupEvent> groupEvents;

    @PrePersist
    public void onPrePersist(){
        File file = new File(Helper.getUserDirectoryPath(),
                this.getCredential().getUserName());
        file.mkdirs();
    }

    @PreRemove
    public void onPreRemove(){
        File file = new File(Helper.getUserDirectoryPath(),
                this.getCredential().getUserName());
        file.delete();
    }

    @Column
    private String profession;

    @OneToOne(mappedBy="account", cascade=CascadeType.ALL)
    private Credential credential;

    @OneToOne(mappedBy = "account",optional = true,
            cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Address address;



    public Account() {
        this.credential = new Credential();
        this.creationDate = new Date();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFirstNameHtml() {
        return StringEscapeUtils.escapeHtml(firstName).replaceAll("'", "").replaceAll("\"", "");
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public Set<SingleEvent> getSingleEvents() {
        return singleEvents;
    }

    public List<SingleEvent> getSingleEventsAsList() {
        return new ArrayList<SingleEvent>(singleEvents);
    }


    public void setSingleEvents(Set<SingleEvent> singleEvents) {
        this.singleEvents = singleEvents;
    }

    public Set<GroupEvent> getGroupEvents() {
        return groupEvents;
    }

    public void setGroupEvents(Set<GroupEvent> groupEvents) {
        this.groupEvents = groupEvents;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLocalisation() {
        if (localisation != null) {
            if (localisation.isEmpty()){
                return null;
            }
        }
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public SexeEnum getSexe() {
        return sexe;
    }

    public void setSexe(SexeEnum sexe) {
        this.sexe = sexe;
    }



    public Date getLastNotificationMailDate() {
        return lastNotificationMailDate;
    }

    public void setLastNotificationMailDate(Date lastNotificationMailDate) {
        this.lastNotificationMailDate = lastNotificationMailDate;
    }

    public Set<Account> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<Account> followers) {
        this.followers = followers;
    }

    public List<Account> getFollowersAsList() {
        return new ArrayList<Account>(this.followers);
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Boolean getCommentEnabled() {
        return commentEnabled;
    }

    public void setCommentEnabled(Boolean commentEnabled) {
        this.commentEnabled = commentEnabled;
    }

    public Boolean getLikeEnabled() {
        return likeEnabled;
    }

    public void setLikeEnabled(Boolean likeEnabled) {
        this.likeEnabled = likeEnabled;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    public Boolean getIndexed() {
        return indexed;
    }

    public void setIndexed(Boolean indexed) {
        this.indexed = indexed;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLastName() {
        return lastName;
    }


    public String getLastNameHtml() {
        return StringEscapeUtils.escapeHtml(lastName).replaceAll("'", "").replaceAll("\"", "");
    }


    public void loadFollowEagerly(){
        for (Account account: this.following){
            account.getEmailAddress();
        }

        for (Account account: this.followers){
            account.getEmailAddress();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Account> getFollowing() {
        return following;
    }

    public void setFollowing(Set<Account> following) {
        this.following = following;
    }

    public List<Account> getFollowingAsList() {
        return new ArrayList<Account>(following);
    }


    public void setLastName(String secondName) {
        this.lastName = secondName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }


    public Address getAddress() {
        if (this.address==null){
            this.address = new Address();
            this.address.setAccount(this);
        }
        return address;
    }

    public void setAddress(Address address) {
        if (address!= null){
            this.address.setAccount(this);
        }
        this.address = address;
    }

    public AvatarImageFile getAvatarImageFile() {
        return avatarImageFile;
    }

    public void setAvatarImageFile(AvatarImageFile avatarImageFile) {
        this.avatarImageFile = avatarImageFile;
        if(this.avatarImageFile != null){
            this.avatarImageFile.setAccount(this);
        }
    }

    @PostPersist
    public void postPersist(){
        File userDirectory = new File(Helper.getUserDirectoryPath(),
                this.id.toString());
        if(!userDirectory.exists()){
            userDirectory.mkdirs();
        }
    }

}
