package com.project.dao;

import com.project.entities.*;
import com.project.entities.notifications.Notification;
import com.project.web.GroupEventFilter;
import org.apache.commons.lang.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 13/09/13
 * Time: 17:53
 * To change this template use File | Settings | File Templates.
 */
public class Explorer {

    private EntityManager em;

    public Explorer(EntityManager em) {
        this.em = em;
    }

    public List<Person> findAllPersons(){
        return  new ArrayList<Person>(this.em.createNamedQuery(Person.FIND_ALL).
                getResultList());
    }

    /**
     * Method to get user credentials by username
     * @param userName
     * @return
     */
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

    /**
     * Method to find account by user name
     * @param name
     * @return
     */
    public Account findAccountByName(String name){
        return (Account) em.createNamedQuery(
                Account.FIND_ACCOUNT_BY_NAME).
                setParameter("name", name).getSingleResult();
    }

    /**
     * Method to get all available wastes
     * @return
     */
    public List<Waste> getAllAvailableWastes(){
        try{
            List<Waste> wastes = new ArrayList<Waste>(em.createNamedQuery(
                    Waste.ALL_WASTES).
                    getResultList());
            return wastes;
        } catch (NoResultException e){
            return null;
        }
    }

    /**
     * Method to get total
     * @return
     */
    public List<WasteGarbage> getPickedUpGarbages(){
        try{
            List<WasteGarbage> wasteGarbages =
                    new ArrayList<WasteGarbage>(em.createNamedQuery(
                    WasteGarbage.ALL_GARBAGES).
                    getResultList());
            return wasteGarbages;
        } catch (NoResultException e){
            return null;
        }
    }


    public List<SingleEvent> findLastSingleEvents() {
        try{
            List<SingleEvent> events = new ArrayList<SingleEvent>(em.createNamedQuery(
                    SingleEvent.FIND_ALL).
                    getResultList());
            return events;
        } catch (NoResultException e){
            return null;
        }
    }


    public List<GroupEvent> findLastGroupEvents() {
        try{
            List<GroupEvent> events = new ArrayList<GroupEvent>(em.createNamedQuery(
                    GroupEvent.FIND_ALL).setMaxResults(20).
                    getResultList());
            return events;
        } catch (NoResultException e){
            return null;
        }
    }


    public List<Account> findAccountByKeyWord(String query) {
        try{
            List<Account> accounts = new ArrayList<Account>(em.createNamedQuery(
                    Account.FIND_ACCOUNT_BY_KEYWORD).setParameter("query", "%"+query+"%").getResultList());
            return accounts;
        } catch (NoResultException e){
            return null;
        }
    }

    public List<Notification> getUserNotifications(Account account){
        try{
            List<Notification> notifications = new ArrayList<Notification>(em.createNamedQuery(
                    Notification.FIND_AGENT_NOTIFICATION).setParameter("accountId", account).setMaxResults(20).getResultList());
            return notifications;
        } catch (NoResultException e){
            return null;
        }
    }

    public List<String> findAdressByCountry(String query) {
        try{
            List<String>  addresses = new ArrayList<String>(em.createNamedQuery(
                    Address.FIND_ADDRESS_BY_COUNTRY).setParameter("countryQuery", query+"%").getResultList());
            return addresses;
        } catch (NoResultException e){
            return null;
        }
    }

    public List<String> findAdressByCity(String query) {
        try{
            List<String>  addresses = new ArrayList<String>(em.createNamedQuery(
                    Address.FIND_ADDRESS_BY_CITY).setParameter("cityQuery", query+"%").getResultList());
            return addresses;
        } catch (NoResultException e){
            return null;
        }
    }

    public List<GroupEvent> findGroupEventsByFilter(GroupEventFilter filter) {
        try{
           Query query = em.createNamedQuery(
                   GroupEvent.FIND_BY_FILTER);
            query.setParameter("afterDate",filter.getFromDate());
            query.setParameter("beforeDate",filter.getToDate());
            query.setParameter("city", StringUtils.isNotEmpty(filter.getCity()) ?
                    "%"+filter.getCity()+"%" : null);
            query.setParameter("country", StringUtils.isNotEmpty(filter.getCountry()) ?
                    "%"+filter.getCountry()+"%" : null);
            List<GroupEvent> events = query.getResultList();
            return events;
        } catch (NoResultException e){
            return null;
        }
    }
}
