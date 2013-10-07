package com.project.web.faces;



import org.apache.commons.lang.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/** JSF  Date  converter for view param.
 * @author selim.bensenouci
 */
@FacesConverter(value = "viewParamDateConverter")
public class DateViewParamConverter implements Converter {

    private final SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");

    /**
     * Method to get Date Object from String value as formatted date.
     * @param context as facesContext Object.
     * @param component as UIcomponent Object.
     * @param value as String used to get Date Object.
     * @return  Object as Date.
     */
    @Override
    public Object getAsObject (
            FacesContext context, UIComponent component, String value) {
        if(StringUtils.isNotEmpty(value)){
            try {
                return formatter.parse(value);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }

    }

    /**
     * Method to get String value of formatted Date object.
     * @param context as facesContext Object.
     * @param component as UIcomponent Object.
     * @param value as String used to get Date Object.
     * @return String as formatted date.
     */
    @Override
    public String getAsString(
            FacesContext context, UIComponent component, Object value) {
        if (value == null){
            return null;
        }
        final Date date = (Date) value;
        return formatter.format(date);
    }
}
