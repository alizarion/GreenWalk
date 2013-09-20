package com.project.web.faces;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.map.MarkerDragEvent;

import javax.el.MethodExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;


@FacesComponent(value="FileUploadListenerCC")
public class FileUploadListenerCC extends UINamingContainer {

    public void fileUploadListener(FileUploadEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        MethodExpression ajaxEventListener = (MethodExpression) getAttributes().get("fileUploadListener");
        ajaxEventListener.invoke(context.getELContext(), new Object[] { event });
    }

}