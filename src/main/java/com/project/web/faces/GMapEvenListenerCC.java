package com.project.web.faces;

import org.primefaces.event.map.MarkerDragEvent;

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

}