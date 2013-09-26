package com.project.services.wrappers;

import com.project.entities.GroupEvent;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 26/09/13
 * Time: 18:36
 * To change this template use File | Settings | File Templates.
 */
public class GroupEventTabViewWrapper {

    private List<GroupEvent> eventList;

    private String tabTitle;

    private Boolean editable;

    public GroupEventTabViewWrapper() {
    }

    public GroupEventTabViewWrapper(List<GroupEvent> eventList, String tabTitle, Boolean editable) {
        this.eventList = eventList;
        this.tabTitle = tabTitle;
        this.editable = editable;
    }

    public List<GroupEvent> getEventList() {
        return eventList;
    }

    public void setEventList(List<GroupEvent> eventList) {
        this.eventList = eventList;
    }

    public String getTabTitle() {
        return tabTitle;
    }

    public void setTabTitle(String tabTitle) {
        this.tabTitle = tabTitle;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }
}
