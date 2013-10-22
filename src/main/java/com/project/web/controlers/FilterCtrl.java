package com.project.web.controlers;

import com.project.entities.Address;
import com.project.entities.GroupEvent;
import com.project.entities.SingleEvent;
import com.project.services.EntityFacade;
import com.project.services.wrappers.GroupEventTabViewWrapper;
import com.project.web.GroupEventFilter;
import org.apache.commons.lang.LocaleUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author selim@openlinux.fr.
 */
@ManagedBean
@ViewScoped
public class FilterCtrl implements Serializable {


    @Inject
    EntityFacade facade;

    private final static Logger LOG = Logger.getLogger(FilterCtrl.class);


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

    private Address actualLocationAdress;

    private Integer mapZoom = 4;

    private LatLng actualLocation;

    private MapModel mapModel;

    private Marker selectedMarker;

    private GroupEventFilter filter = new GroupEventFilter();

    private Boolean mapModelUpdated = false;


    @PostConstruct
    private void postInit(){
        LOG.info("FilterCtrl : PostConstruct");

        FacesContext.getCurrentInstance().getViewRoot().getAttributes().put(
                EntityFacade.EF_NAME, this.facade);
     //   this.singleEvents = facade.findLastSingleEvents();


    }

    public void updateMapModel(){
        if (!mapModelUpdated){
        LOG.info("FilterCtrl : updateMapModel");
        this.groupEvents = facade.getGroupEventByFilter(this.filter);
        getCurrent();
        getThisMonth();
        getThisMonth1();
        getThisMonth2();
      //  this.groupEvents = facade.getGroupEventByFilter(this.filter);
        this.mapModel = new DefaultMapModel();
        for (GroupEvent groupEvent: this.groupEvents){
            if (groupEvent.getAddress().getPosition().getAsLatLng() != null){
                new SimpleDateFormat("MM/dd/yyyy HH:mm",
                        LocaleUtils.toLocale(sessionAttribute.getSelectedLang().
                                getKey())).format(groupEvent.getEventDate());
                mapModel.addOverlay(new Marker(groupEvent.getAddress().
                        getPosition().getAsLatLng(),
                        groupEvent.getOverlayDescription(
                                sessionAttribute.getSelectedLang().getKey())));
            }
        }
            mapModelUpdated = true;
        }
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
                        getKey())).format(new Date()), false));
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

    public Address getActualLocationAdress() {
        return actualLocationAdress;
    }

    public void setActualLocationAdress(Address actualLocationAdress) {
        this.actualLocationAdress = actualLocationAdress;
    }

    public GroupEventFilter getFilter() {
        return filter;
    }

    public void setFilter(GroupEventFilter filter) {
        this.filter = filter;
    }

    public LatLng getActualLocation() {
        return actualLocation;
    }

    public void setActualLocation(LatLng actualLocation) {
        this.actualLocation = actualLocation;
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    public Integer getMapZoom() {
        return mapZoom;
    }

    public void setMapZoom(Integer mapZoom) {
        this.mapZoom = mapZoom;
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        this.selectedMarker = (Marker) event.getOverlay();
    }

    public Marker getSelectedMarker() {
        return selectedMarker;
    }

    public void setSelectedMarker(Marker selectedMarker) {
        this.selectedMarker = selectedMarker;
    }

    public List<String> completeCountry(String query){
        return this.facade.getCountryAdresses(query);
    }

    public List<String> completeCity(String query){
        return this.facade.getCityAdresses(query);
    }



    public String applyFilter(){
        return "next-missions-outcome";
    }
}
