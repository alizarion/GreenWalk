package com.project.web.controlers;

import com.project.entities.GroupEvent;
import com.project.entities.SingleEvent;
import com.project.services.EntityFacade;
import com.project.services.wrappers.GroupEventTabViewWrapper;
import org.apache.commons.lang.LocaleUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author selim@openlinux.fr.
 */
@Named
@ConversationScoped
public class HomeCtrl implements Serializable {


    @Inject
    EntityFacade facade;

    private List<SingleEvent> singleEvents;

    private List<GroupEvent> groupEvents;

    private List<GroupEvent> endedThisMonthGroupEvents = new ArrayList<GroupEvent>();
    private List<GroupEvent> endedThisMonth1GroupEvents = new ArrayList<GroupEvent>();
    private List<GroupEvent> endedThisMonth2GroupEvents = new ArrayList<GroupEvent>();
    private List<GroupEvent> currentGroupEvents = new ArrayList<GroupEvent>();

    private List<GroupEventTabViewWrapper> groupEventTabViewWrappers = new ArrayList<GroupEventTabViewWrapper>();
    ResourceBundle bundle = ResourceBundle.getBundle("lang");

    private Date currentDate = new Date();
    private Date month1;
    private Date month2;
    @Inject
    SessionAttributeCtrl sessionAttribute;

    @PostConstruct
    private void postInit(){
        FacesContext.getCurrentInstance().getViewRoot().getAttributes().put(
                EntityFacade.EF_NAME, this.facade);
        this.singleEvents = facade.findLastSingleEvents();
        this.groupEvents = facade.findLastGroupEvents();
        getCurrent();
        getThisMonth();
        getThisMonth1();
        getThisMonth2();
    }

    private  void getCurrent(){
        for (GroupEvent groupEvent : this.groupEvents){
            if (!groupEvent.getEndedEvent()){
                this.currentGroupEvents.add(groupEvent);
            }
        }
        this.groupEventTabViewWrappers.add(new GroupEventTabViewWrapper(
                this.currentGroupEvents,bundle.getString("common.label.group.event.comming-"+
                sessionAttribute.getSelectedLang().getKey()),false));
    }

    private  void getThisMonth(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int currentMonth  = cal.get(Calendar.MONTH);
        for (GroupEvent groupEvent : this.groupEvents){
            cal.setTime(groupEvent.getEventDate());
            int eventMonth  = cal.get(Calendar.MONTH);
            if (eventMonth == currentMonth){
                this.endedThisMonthGroupEvents.add(groupEvent);
            }
        }
        this.groupEventTabViewWrappers.add(new GroupEventTabViewWrapper(
                this.endedThisMonthGroupEvents, new SimpleDateFormat("MMMM",
                LocaleUtils.toLocale(sessionAttribute.getSelectedLang().
                        getKey())).format(cal.getTime()), false));
    }

    private  void getThisMonth1(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1);
        int currentMonth = cal.get(Calendar.MONTH);
        this.month1  =cal.getTime();
        for (GroupEvent groupEvent : this.groupEvents){
            cal.setTime(groupEvent.getEventDate());
            int eventMonth  = cal.get(Calendar.MONTH);
            if (eventMonth == currentMonth){
                this.endedThisMonth1GroupEvents.add(groupEvent);
            }
        }
        this.groupEventTabViewWrappers.add(new GroupEventTabViewWrapper(
                this.endedThisMonth1GroupEvents, new SimpleDateFormat("MMMM",
                LocaleUtils.toLocale(sessionAttribute.getSelectedLang().
                        getKey())).format(this.month1), false));
    }


    private  void getThisMonth2(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -2) ;
        int currentMonth = cal.get(Calendar.MONTH);
        this.month2  =cal.getTime();
        for (GroupEvent groupEvent : this.groupEvents){
            cal.setTime(groupEvent.getEventDate());
            int eventMonth  = cal.get(Calendar.MONTH);
            if (eventMonth == currentMonth){
                this.endedThisMonth2GroupEvents.add(groupEvent);
            }
        }
        this.groupEventTabViewWrappers.add(new GroupEventTabViewWrapper(
                this.endedThisMonth2GroupEvents, new SimpleDateFormat("MMMM",
                LocaleUtils.toLocale(sessionAttribute.getSelectedLang().
                        getKey())).format(this.month2), false));
    }

    public List<GroupEventTabViewWrapper> getGroupEventTabViewWrappers() {
        return groupEventTabViewWrappers;
    }

    public void setGroupEventTabViewWrappers(List<GroupEventTabViewWrapper> groupEventTabViewWrappers) {
        this.groupEventTabViewWrappers = groupEventTabViewWrappers;
    }

    public List<GroupEvent> getEndedThisMonthGroupEvents() {
        return endedThisMonthGroupEvents;
    }

    public void setEndedThisMonthGroupEvents(List<GroupEvent> endedThisMonthGroupEvents) {
        this.endedThisMonthGroupEvents = endedThisMonthGroupEvents;
    }

    public List<GroupEvent> getEndedThisMonth1GroupEvents() {
        return endedThisMonth1GroupEvents;
    }

    public void setEndedThisMonth1GroupEvents(List<GroupEvent> endedThisMonth1GroupEvents) {
        this.endedThisMonth1GroupEvents = endedThisMonth1GroupEvents;
    }

    public List<GroupEvent> getEndedThisMonth2GroupEvents() {
        return endedThisMonth2GroupEvents;
    }

    public void setEndedThisMonth2GroupEvents(List<GroupEvent> endedThisMonth2GroupEvents) {
        this.endedThisMonth2GroupEvents = endedThisMonth2GroupEvents;
    }

    public List<GroupEvent> getCurrentGroupEvents() {
        return currentGroupEvents;
    }

    public void setCurrentGroupEvents(List<GroupEvent> currentGroupEvents) {
        this.currentGroupEvents = currentGroupEvents;
    }

    public List<SingleEvent> getSingleEvents() {
        return singleEvents;
    }

    public List<GroupEvent> getGroupEvents() {
        return groupEvents;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public Date getMonth1() {
        return month1;
    }

    public void setMonth1(Date month1) {
        this.month1 = month1;
    }

    public Date getMonth2() {
        return month2;
    }

    public void setMonth2(Date month2) {
        this.month2 = month2;
    }
}
