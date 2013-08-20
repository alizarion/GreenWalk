package com.project.entities;

import com.project.Helper;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 29/09/11
 * Time: 21:40
 * To change this template use File | Settings | File Templates.
 */


@Entity
@Table(catalog = Helper.ENTITIES_CATALOG,name = "credential")
@NamedQueries({
        @NamedQuery(name=Credential.FIND_CREDENTIAL_BY_USERNAME,
                query="select cre"
                        + " from Credential cre "
                        + " where cre.userName "
                        + " like :query ") })

public class Credential implements Serializable{

    public static final String FIND_CREDENTIAL_BY_USERNAME =
            "FIND_CREDENTIAL_BY_USERNAME" ;

    @Id
    @TableGenerator(name="Credential_SEQ", table="sequence",catalog = Helper.ENTITIES_CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="Credential_SEQ")
    @Column
    private Long id;


    @Column(name = "userName",unique = true)
    private String userName;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name="state")
    private CredentialsState state;

    @Column
    private String keyGen;

    @Column
    private Date creationDate;

    @Column
    private String role;

    @OneToOne
    @PrimaryKeyJoinColumn(name="credential_id")
    private Account account;
    //TODO corrigé la gestion des roles
    public Credential() {
        this.role = "Users";
        this.creationDate = new Date();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password)
            throws NoSuchAlgorithmException {
        this.password = getEncryptedPassword(password);
    }

    public Account getAccount() {
        return account;
    }


    public String getKeyGen() {
        return keyGen;
    }

    public CredentialsState getState() {
        return state;
    }

    public void setState(CredentialsState state) {
        this.state = state;
    }

    public void setKeyGen(String keyGen) {
        this.keyGen = keyGen;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getRole() {
        return role;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private  String getEncryptedPassword(String clearTextPassword)
            throws NoSuchAlgorithmException {
        String result = clearTextPassword;
        if(clearTextPassword != null) {
            MessageDigest md = MessageDigest.getInstance("SHA-1"); //or "MD5"
            md.update(clearTextPassword.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            while(result.length() < 32) {
                result = "0" + result;
            }
        }
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Credential)) return false;

        Credential that = (Credential) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
