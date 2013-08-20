package com.project.web.controlers;

import com.project.entities.AvailableLang;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author selim@openlinux.fr.
 */
@Named
@SessionScoped
public class SessionAttributeCtrl implements Serializable {

    private List<AvailableLang> langs = new ArrayList<AvailableLang>(
            Arrays.asList(AvailableLang.values()));

    private AvailableLang selectedLang = AvailableLang.en;


    public List<AvailableLang> getLangs() {
        return langs;
    }

    public void setLangs(List<AvailableLang> langs) {
        this.langs = langs;
    }

    public AvailableLang getSelectedLang() {
        return selectedLang;
    }

    public void setSelectedLang(AvailableLang selectedLang) {
        this.selectedLang = selectedLang;
    }
}

