package com.project.services;

import com.project.entities.PersistentEntry;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 30/01/13
 * Time: 21:54
 * To change this template use File | Settings | File Templates.
 */
public abstract class PersistentMBean {

    @PersistenceContext
    EntityManager em;



    public abstract String getPropertiesFilename();

    public String getProperty(String s){
        try{
            PersistentEntry persistentEntry = (PersistentEntry)
                    em.createNamedQuery(PersistentEntry.FIND_BY_CATEGORY_AND_KEY).
                            setParameter("category", getPropertiesFilename()).
                            setParameter("key", s).getSingleResult();

            return persistentEntry.getValue();
        } catch (NoResultException e){
            return null;
        }
    }

    public void setProperty(String key, String value){
        try{
            PersistentEntry persistentEntry = (PersistentEntry)
                    em.createNamedQuery(PersistentEntry.FIND_BY_CATEGORY_AND_KEY).
                            setParameter("category",getPropertiesFilename()).
                            setParameter("key",key).getSingleResult();

            persistentEntry.setValue(value);
            em.merge(persistentEntry);

        } catch (NoResultException e){

        }
    }




}
