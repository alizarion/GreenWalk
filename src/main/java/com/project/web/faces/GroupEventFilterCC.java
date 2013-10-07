package com.project.web.faces;

import org.primefaces.event.map.OverlaySelectEvent;

import javax.el.MethodExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;


@FacesComponent(value="GroupEventFilterCC")
public class GroupEventFilterCC extends UINamingContainer {

    public void onMarkerSelect(OverlaySelectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        MethodExpression ajaxEventListener = (MethodExpression) getAttributes().get("onMarkerSelect");
        ajaxEventListener.invoke(context.getELContext(), new Object[] { event });
    }


}