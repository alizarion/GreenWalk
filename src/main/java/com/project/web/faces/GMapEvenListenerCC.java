package com.project.web.faces;

import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.StateChangeEvent;

import javax.el.MethodExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;


@FacesComponent(value="GMapEvenListenerCC")
public class GMapEvenListenerCC extends UINamingContainer {

    public void onMarkerDrag(MarkerDragEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        MethodExpression ajaxEventListener = (MethodExpression) getAttributes().get("onMarkerDrag");
        ajaxEventListener.invoke(context.getELContext(), new Object[] { event });
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        MethodExpression ajaxEventListener = (MethodExpression) getAttributes().get("onMarkerSelect");
        ajaxEventListener.invoke(context.getELContext(), new Object[] { event });
    }

    public void onStateChange(StateChangeEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        MethodExpression ajaxEventListener = (MethodExpression) getAttributes().get("onStateChange");
        ajaxEventListener.invoke(context.getELContext(), new Object[] { event });
    }


}