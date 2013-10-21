package com.project.web.controlers;

import com.project.entities.Account;
import com.project.entities.GroupEvent;
import com.project.entities.GroupEventSubscriber;
import com.project.services.EntityFacade;
import com.project.services.wrappers.GroupEventTabViewWrapper;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author selim@openlinux.fr.
 */
@ManagedBean
@ViewScoped
public class MemberGroupEventPanelCtrl implements Serializable {

    @Inject
    EntityFacade facade;

    @Inject
    SessionAttributeCtrl sessionAttribute;

    private final static Logger LOG =
            Logger.getLogger(MemberGroupEventPanelCtrl.class);

    private Account currentUser;

    ResourceBundle bundle = ResourceBundle.getBundle("lang");

    private List<GroupEventTabViewWrapper> groupEventTabViewWrappers =
            new ArrayList<GroupEventTabViewWrapper>();

    @PostConstruct
    private void postInit(){
        LOG.info("GroupEventCtrl : PostConstruct");

        FacesContext.getCurrentInstance().getViewRoot().getAttributes().put(
                EntityFacade.EF_NAME, this.facade);
        this.currentUser = facade.loadAccountWithLinkedEventGroups(facade.getActiveUser());
        this.groupEventTabViewWrappers.add(new GroupEventTabViewWrapper(
                new ArrayList<GroupEvent>(this.currentUser.getGroupEvents()),
                bundle.getString("common.group.event.owner.tab.label-"+
                        sessionAttribute.getSelectedLang().getKey()),true));
        List<GroupEvent> groupEvents =  new ArrayList<GroupEvent>();
        for (GroupEventSubscriber subscriber: this.currentUser.getSubscribedEvents()){
            if (subscriber.getConfirmed()){
                groupEvents.add(subscriber.getGroupEvent());
            }
        }
        this.groupEventTabViewWrappers.add(new GroupEventTabViewWrapper(
                groupEvents,
                bundle.getString("common.group.event.subscribed.tab.label-"+
                        sessionAttribute.getSelectedLang().getKey()),false));
    }

    public List<GroupEventTabViewWrapper> getGroupEventTabViewWrappers() {
        return groupEventTabViewWrappers;
    }

    public void setGroupEventTabViewWrappers(List<GroupEventTabViewWrapper> groupEventTabViewWrappers) {
        this.groupEventTabViewWrappers = groupEventTabViewWrappers;
    }
}
