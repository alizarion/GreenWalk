package com.project.web.controlers;

import com.project.Helper;
import com.project.entities.AvailableLang;
import com.project.entities.WasteGarbage;
import com.project.services.EntityFacade;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author selim@openlinux.fr.
 */
@Named
@ManagedBean
@SessionScoped
public class SessionAttributeCtrl implements Serializable {

    @Inject
    EntityFacade facade;

    private List<AvailableLang> langs = new ArrayList<AvailableLang>(
            Arrays.asList(AvailableLang.values()));

    private AvailableLang selectedLang = AvailableLang.EN;

    private String collectedWasteJSObjects;

    private Integer totalWastesCount = 0;

    private List<WasteGarbage> garbages;

    @PostConstruct
    private void postInit(){
        this.garbages = facade.findAllGarbages();
        for (WasteGarbage wasteGarbage:this.garbages ) {
            this.totalWastesCount = this.totalWastesCount +
                    wasteGarbage.getQuantity().intValue();
        }
        this.collectedWasteJSObjects =
                Helper.garbageToJSObjectList(this.garbages);
    }

    public List<AvailableLang> getLangs() {
        return langs;
    }

    public void setLangs(List<AvailableLang> langs) {
        this.langs = langs;
    }

    public AvailableLang getSelectedLang() {
        return selectedLang;
    }

    public String getCollectedWasteJSObjects() {
        return collectedWasteJSObjects;
    }

    public void setCollectedWasteJSObjects(String collectedWasteJSObjects) {
        this.collectedWasteJSObjects = collectedWasteJSObjects;
    }

    public Integer getTotalWastesCount() {
        return totalWastesCount;
    }

    public void setTotalWastesCount(Integer totalWastesCount) {
        this.totalWastesCount = totalWastesCount;
    }

    public void setSelectedLang(AvailableLang selectedLang) {
        this.selectedLang = selectedLang;
    }
}

