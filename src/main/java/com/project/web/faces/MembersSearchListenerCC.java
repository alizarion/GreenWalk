package com.project.web.faces;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.MarkerDragEvent;

import javax.el.MethodExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;


@FacesComponent(value="MembersSearchListenerCC")
public class MembersSearchListenerCC extends UINamingContainer {

    public void handleMemberSelected(SelectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        MethodExpression ajaxEventListener = (MethodExpression) getAttributes().get("handleMemberSelected");
        ajaxEventListener.invoke(context.getELContext(), new Object[] { event });
    }

}