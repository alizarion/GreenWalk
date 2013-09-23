package com.project.web.faces;

import com.project.entities.Account;
import com.project.services.EntityFacade;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 23/09/13
 * Time: 15:25
 * To change this template use File | Settings | File Templates.
 */
@FacesConverter(value = "AccountConverter")
public class AccountConverter  implements Converter {


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        final EntityFacade facade = (EntityFacade)
                context.getViewRoot().getAttributes().get(EntityFacade.EF_NAME);
        final Account account =
                facade.findAccountById(Long.parseLong(value));
        return account;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        final Account object = (Account) value;
        final String id = object.getId().toString();
        return id;
    }
}
