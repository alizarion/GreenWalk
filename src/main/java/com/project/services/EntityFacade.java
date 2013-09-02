package com.project.services;

import com.project.comingsoon.MailingList;
import com.project.entities.*;
import com.project.services.mail.MailSender;
import com.project.services.mail.RegisterEmail;
import com.project.services.mail.SMTPEmailProvider;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity facade Service used for fonctional application
 * @author Selim.Bensenouci
 */
@Named(value = EntityFacade.EF_NAME)
@Stateless
@TransactionAttribute
public class EntityFacade implements Serializable{

    private final static Logger LOG = Logger.getLogger(EntityFacade.class);

    public static final String EF_NAME = "entityFacade";

    @PersistenceContext
    EntityManager em;

    @PostConstruct
    protected void postConstruct() {
        LOG.debug("PostConstruct " + this);
    }


    /**
     * Method to merge person with the PersistantContext
     * @param person to merge
     * @return
     */
    public Person createPerson(Person person){
        return this.em.merge(person);
    }


    /**
     * Method to delete person from the dataBase
     * @param person to delete
     */
    public void deletePerson(Person person){
        this.em.remove(person);
    }

    /**
     * Simple method to find person from this PersistantContext
     * @param id of the wanted person
     */
    public void findPerson(Long id){
        this.em.find(Person.class,id);
    }


    public List<Person> findAllPersons(){
        return  new ArrayList<Person>(this.em.createNamedQuery(Person.FIND_ALL).
                getResultList());
    }


    public Account findAccountByEmail(String email){
        try{
            Account account =  (Account) em.createNamedQuery(
                    Account.FIND_ACCOUNT_BY_EMAIL).
                    setParameter("email", email).getSingleResult();
            return account;
        }
        catch (NoResultException e ){
            return null;
        }
    }

    public void createAccount(Account account){


        account.getCredential().setKeyGen(account.getFirstName().hashCode()
                + '-' + account.getLastName().hashCode() +
                "-" + account.getCredential().getUserName().hashCode());
        account.getCredential().setState(CredentialsState.P);
        em.persist(account);
        account = em.merge(account);
        em.flush();
        em.clear();

        //TODO change for production Email
        EmailCounter emailCounter =  new EmailCounter(account.getEmailAddress(),"inscription",new Date());

        mergeEmailCounter(emailCounter);
        MailSender sender = new MailSender(new SMTPEmailProvider());
        sender.sendEmail(new RegisterEmail(account));
    }

    public Credential getCredentialByUserName(String userName){
        try{
            Credential credential = (Credential) em.createNamedQuery(
                    Credential.FIND_CREDENTIAL_BY_USERNAME).
                    setParameter("query",userName).
                    getSingleResult();
            // credential.getAccountEagerly();
            credential.getAccount().loadFollowEagerly();
            return credential;
        } catch (NoResultException e){
            return null;
        }
    }

    public Account findAccountByName(String name){
        return (Account) em.createNamedQuery(
                Account.FIND_ACCOUNT_BY_NAME).
                setParameter("name", name).getSingleResult();
    }

    public Account mergeAccount(Account account){
        Account mergedAccount = em.merge(account);

        return mergedAccount;
    }


    public void mergeEmailCounter(EmailCounter emailCounter){
        em.merge(emailCounter);
    }

    public ImageContent addNewTemporaryImageContent(ImageContent content) {
        content = em.merge(content);
        em.flush();
        em.clear();
        return content;

    }

    public UploadedFile getUploadedFileById(Long id){
        return em.find(UploadedFile.class, id);

    }

    public void updateUploadedFile(UploadedFile contentFile) {
        em.merge(contentFile);
    }


    public void registerFromComingSoon(MailingList mailingList){
        em.merge(mailingList);
    }
}
