package com.project.entities;

import com.project.Helper;
import com.project.entities.notifications.Notification;
import org.apache.commons.lang.StringEscapeUtils;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

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
        @NamedQuery(name= Account.FIND_ACCOUNT_BY_KEYWORD,
                hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                query="select compte"
                        + " from Account compte "
                        + " where "
                        + "compte.lastName like :query or compte.firstName like :query or compte.credential.userName "
                        + "like :query or compte.address.city like :query order by compte.lastName"),
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
    public static final String FIND_ACCOUNT_BY_KEYWORD ="FIND_ACCOUNT_BY_KEYWORD";
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

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "accountListener")
    private Set<Notification> notifications= new HashSet<Notification>();

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

    @OneToMany(mappedBy ="receiver",
            fetch = FetchType.LAZY)
    private Set<PrivateMessage> receivedMessages =
            new HashSet<PrivateMessage>();

    @OneToMany(mappedBy = "sender",
            fetch = FetchType.LAZY)
    private Set<PrivateMessage> sentMessages =
            new HashSet<PrivateMessage>();

    @OneToMany(mappedBy = "singleEventOwner",
            fetch = FetchType.LAZY)
    @OrderBy("creationDate desc ")
    private Set<SingleEvent> singleEvents =
            new HashSet<SingleEvent>();

    @OneToMany(mappedBy = "groupEventOwner",
            fetch = FetchType.LAZY)
    @OrderBy("creationDate desc ")
    private Set<GroupEvent> groupEvents =
            new HashSet<GroupEvent>();

    @OneToMany(mappedBy = "account",
            fetch = FetchType.LAZY)
    private Set<GroupEventSubscriber> subscribedEvents =
            new HashSet<GroupEventSubscriber>();

    @OneToMany(mappedBy = "wasteDeclaring",
            fetch = FetchType.LAZY)
    private Set<WasteGarbage> wasteGarbages = new HashSet<WasteGarbage>();

    @Transient
    private MapModel mapModel = null;

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

    public void consumeAllNotifications(){
        for (Notification notification :this.notifications){
            notification.setViewed(true);
        }
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public Set<PrivateMessage> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(Set<PrivateMessage> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public Set<PrivateMessage> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(Set<PrivateMessage> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public Set<SingleEvent> getSingleEvents() {
        return singleEvents;
    }

    public List<SingleEvent> getSingleEventsAsList() {
        return new ArrayList<SingleEvent>(singleEvents);
    }

    public List<Account> getCorrespondingAccounts(){
        Set<Account>  accounts = new HashSet<Account>();
        for(PrivateMessage privateMessage : this.receivedMessages){
            accounts.add(privateMessage.getSender());
        }
        for(PrivateMessage privateMessage : this.sentMessages){
            accounts.add(privateMessage.getReceiver());
        }
        return new ArrayList<Account>(accounts);
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

    public List<PrivateMessage> getConversationWith(Account account){
        List<PrivateMessage> privateMessageList = new ArrayList<PrivateMessage>();
        for(PrivateMessage privateMessage: this.receivedMessages){
            if (privateMessage.getSender().equals(account)){
                privateMessageList.add(privateMessage);
            }
        }
        for(PrivateMessage privateMessage: this.sentMessages){
            if (privateMessage.getReceiver().equals(account)){
                privateMessageList.add(privateMessage);
            }
        }
        Collections.sort(privateMessageList);
        return privateMessageList;
    }

    public void setIndexed(Boolean indexed) {
        this.indexed = indexed;
    }

    public Set<GroupEventSubscriber> getSubscribedEvents() {
        return subscribedEvents;
    }

    public void setSubscribedEvents(Set<GroupEventSubscriber> subscribedEvents) {
        this.subscribedEvents = subscribedEvents;
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

    public void loadConversations(){
        for(PrivateMessage privateMessage : this.receivedMessages){
            privateMessage.getId();
        }
        for(PrivateMessage privateMessage : this.sentMessages){
            privateMessage.getId();
        }
    }

    public MapModel getLastActionAreasMap(String localeKey){
        if (this.mapModel == null){
            this.mapModel = new DefaultMapModel();
        for(GroupEvent groupEvent :  this.groupEvents){
            if (groupEvent.getAddress().getPosition().getAsLatLng() != null){
                mapModel.addOverlay(new Marker(groupEvent.getAddress().
                        getPosition().getAsLatLng(),
                        groupEvent.getOverlayDescription(localeKey)));
            }
        }
        for(GroupEventSubscriber subscriber : this.subscribedEvents){
            if (subscriber.getGroupEvent().
                    getAddress().getPosition().getAsLatLng() != null&&subscriber.getConfirmed() )
                mapModel.addOverlay(new Marker(subscriber.getGroupEvent().
                        getAddress().getPosition().getAsLatLng(),
                        subscriber.getGroupEvent().getOverlayDescription(localeKey)));
        }

        }
        return mapModel;
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


    public void loadSingleEventEagerly(){
        for (SingleEvent singleEvent : this.singleEvents){
            singleEvent.getId();
        }
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

    public String getAllMemberGarbageAsJSObjectList(){
        GroupEvent eventtmp= new GroupEvent();
        for ( WasteGarbage wasteGarbage : this.wasteGarbages ){
            eventtmp.addNewGarbage(wasteGarbage);
        }
        return eventtmp.getGarbageAsJSObjectList();
    }

    @PostPersist
    public void postPersist(){
        File userDirectory = new File(Helper.getUserDirectoryPath(),
                this.id.toString());
        if(!userDirectory.exists()){
            userDirectory.mkdirs();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (address != null ? !address.equals(account.address) :
                account.address != null) return false;
        if (birthDay != null ? !birthDay.equals(account.birthDay) :
                account.birthDay != null) return false;
        if (commentEnabled != null ? !commentEnabled.equals(account.commentEnabled)
                : account.commentEnabled != null)
            return false;
        if (creationDate != null ? !creationDate.equals(account.creationDate) :
                account.creationDate != null)
            return false;
        if (credential != null ? !credential.equals(account.credential) :
                account.credential != null) return false;
        if (emailAddress != null ? !emailAddress.equals(account.emailAddress) :
                account.emailAddress != null)
            return false;
        if (firstName != null ? !firstName.equals(account.firstName) :
                account.firstName != null) return false;
        if (id != null ? !id.equals(account.id) : account.id != null) return false;
        if (indexed != null ? !indexed.equals(account.indexed) :
                account.indexed != null) return false;
        if (lastName != null ? !lastName.equals(account.lastName) :
                account.lastName != null) return false;
        if (lastNotificationMailDate != null ? !lastNotificationMailDate.equals(account.lastNotificationMailDate) :
                account.lastNotificationMailDate != null)
            return false;
        if (likeEnabled != null ? !likeEnabled.equals(account.likeEnabled) :
                account.likeEnabled != null) return false;
        if (localisation != null ? !localisation.equals(account.localisation) :
                account.localisation != null)
            return false;
        if (phoneNumber != null ? !phoneNumber.equals(account.phoneNumber) :
                account.phoneNumber != null) return false;
        if (profession != null ? !profession.equals(account.profession) :
                account.profession != null) return false;
        if (sexe != account.sexe) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (birthDay != null ? birthDay.hashCode() : 0);
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
        result = 31 * result + (sexe != null ? sexe.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (indexed != null ? indexed.hashCode() : 0);
        result = 31 * result + (lastNotificationMailDate != null ? lastNotificationMailDate.hashCode() : 0);
        result = 31 * result + (commentEnabled != null ? commentEnabled.hashCode() : 0);
        result = 31 * result + (likeEnabled != null ? likeEnabled.hashCode() : 0);
        result = 31 * result + (profession != null ? profession.hashCode() : 0);
        return result;
    }


}
