package com.project.web.controlers;

import com.project.entities.Account;
import com.project.services.EntityFacade;
import org.apache.log4j.Logger;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.Marker;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
  * @author selim@openlinux.fr
 */
@ManagedBean
@ViewScoped
public class MemberProfileCtrl implements Serializable {

    private final static Logger LOG = Logger.getLogger(MemberProfileCtrl.class);


    @EJB
    EntityFacade facade;

    private Account account;

    private Account currentAccount;

    private Marker selectedMarker;

    private Integer mapZoom = 4;


    @PostConstruct
    public void postInit(){
        LOG.info("MemberProfileCtrl : PostConstruct");
        FacesContext.getCurrentInstance().getViewRoot().getAttributes().put(
                EntityFacade.EF_NAME, this.facade);
        HttpServletRequest request  =  (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        String username = (String) request.getAttribute("username");
        this.currentAccount= facade.getActiveUser();
        this.account = facade.findAccountProfileByName(username);
    }


    public Account getAccount() {
        return account;
    }

    public Marker getSelectedMarker() {
        return selectedMarker;
    }

    public void setSelectedMarker(Marker selectedMarker) {
        this.selectedMarker = selectedMarker;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        this.selectedMarker = (Marker) event.getOverlay();
    }

    public Integer getMapZoom() {
        return mapZoom;
    }

    public void setMapZoom(Integer mapZoom) {
        this.mapZoom = mapZoom;
    }
}
