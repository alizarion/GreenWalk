package com.project.services.validator;


import com.project.services.EntityFacade;
import com.project.web.controlers.SessionAttributeCtrl;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 18/01/12
 * Time: 01:05
 * To change this template use File | Settings | File Templates.
 */
@FacesValidator("com.bookmgr.jsf.validator.SimpleEmailValidator")
public class SimpleEmailValidator implements Validator{
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\." +
            "[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*" +
            "(\\.[A-Za-z]{2,})$";

    private Pattern pattern;
    private Matcher matcher;

    @Inject
    SessionAttributeCtrl sessionAttribute;

    public SimpleEmailValidator(){
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    @Override
    public void validate(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {
        ResourceBundle bundle = ResourceBundle.getBundle("lang");

        matcher = pattern.matcher(value.toString());

        matcher = pattern.matcher(value.toString());
        EntityFacade facade = (EntityFacade)
                context.getViewRoot().getAttributes().
                        get(EntityFacade.EF_NAME);
        if(!matcher.matches()){

            FacesMessage msg =
                    new FacesMessage(bundle.getString("ctrl.validator.email.failed-"+
                            sessionAttribute.getSelectedLang().getKey()),
                            bundle.getString("ctrl.validator.email.failed-"+
                                    sessionAttribute.getSelectedLang().getKey()));
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);


        }



    }
}
