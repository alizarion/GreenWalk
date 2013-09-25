package com.project.web.controlers;

import com.project.entities.*;
import com.project.services.AddressByLongLatHelper;
import com.project.services.EntityFacade;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * @author selim@openlinux.fr.
 */
@ManagedBean
@ViewScoped
public class GroupEventCtrl implements Serializable {

    @Inject
    EntityFacade facade;

    @Inject
    SessionAttributeCtrl sessionAttribute;

    private Account userAccount;

    private MapModel draggableModel;

    private Integer mapZoom = 13;

    private LatLng actualLocation;

    private String searchQuery;

    private GroupEvent event;

    private Account selectedMember;

    private Date today;

    private Date  nextYear;


    @PostConstruct
    private void postInit(){
        FacesContext.getCurrentInstance().getViewRoot().getAttributes().put(
                EntityFacade.EF_NAME, this.facade);
        this.userAccount = facade.getActiveUser();
        this.draggableModel = new DefaultMapModel();
        HttpServletRequest request  =  (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        String eventId = (String) request.getAttribute("eventId");
        this.actualLocation = new LatLng(36.879466, 30.667648);
        if (eventId != null){
            this.event = (GroupEvent) facade.findEventById(Long.parseLong(eventId));
            this.actualLocation = this.event.getAddress().getPosition().getAsLatLng();
        }  else {
            this.event = new GroupEvent(this.userAccount);
        }
        draggableModel.addOverlay(new Marker(this.actualLocation, "default"));
        for(Marker marker : draggableModel.getMarkers()) {
            marker.setDraggable(true);
        }
        Calendar todayCalentdar = Calendar.getInstance();
        this.today =  new Date();
        todayCalentdar = Calendar.getInstance();
        todayCalentdar.add(Calendar.YEAR, 1);
        this.nextYear =  todayCalentdar.getTime();
    }

    public Account getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(Account userAccount) {
        this.userAccount = userAccount;
    }

    public MapModel getDraggableModel() {
        return draggableModel;
    }

    public void setDraggableModel(MapModel draggableModel) {
        this.draggableModel = draggableModel;
    }

    public Integer getMapZoom() {
        return mapZoom;
    }

    public void setMapZoom(Integer mapZoom) {
        this.mapZoom = mapZoom;
    }

    public String getActualLocation() {
        return actualLocation.getLat() +", " +actualLocation.getLng();
    }

    public void setActualLocation(String actualLocation) {
        String[] stringlatlng = actualLocation.split(",");
        this.actualLocation = new
                LatLng(Double.parseDouble(stringlatlng[0]),Double.parseDouble(stringlatlng[1]));

        for(Marker marker : draggableModel.getMarkers()) {
            marker.setLatlng(this.actualLocation);

        }
    }



    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public GroupEvent getEvent() {
        return event;
    }

    public void setEvent(GroupEvent event) {
        this.event = event;
    }

    public void searchPosition(){
        this.event.setAddress(AddressByLongLatHelper.getRequestPosition(this.searchQuery));
        if (this.event.getAddress().getPosition().getAsLatLng()!= null){
            this.actualLocation = this.event.getAddress().getPosition().getAsLatLng();
        }
        for(Marker marker : draggableModel.getMarkers()) {
            marker.setLatlng(this.actualLocation);

        }
    }


    public Account getSelectedMember() {
        return selectedMember;
    }

    public void setSelectedMember(Account selectedMember) {
        this.event.addSubscriber(new GroupEventSubscriber(this.getEvent(),selectedMember));
        this.event.getSubscribers();
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public Date getNextYear() {
        return nextYear;
    }

    public void setNextYear(Date nextYear) {
        this.nextYear = nextYear;
    }

    public void onMarkerDrag(MarkerDragEvent event) {
        this.event.setAddress(AddressByLongLatHelper.getLongitudeLatitude(event.getMarker().getLatlng()));
        if (this.event.getAddress().getPosition().getAsLatLng()!= null){
            this.actualLocation = this.event.getAddress().getPosition().getAsLatLng();
        }
        for(Marker marker : draggableModel.getMarkers()) {
            marker.setLatlng(this.actualLocation);

        }
    }

    public String submitGroupEvent(){
        this.facade.submitGroupEvent(this.event);
        return "success-group-event-outcome";
    }

    public void handleMemberSelected(SelectEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected:" + event.getObject().toString(), null);

        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public List<Account>  completeMembers(String query){
        return facade.searchMembers(query);
    }

}
