package com.project.services.validator;

import com.project.services.EntityFacade;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 17/01/12
 * Time: 00:07
 * To change this template use File | Settings | File Templates.
 */
@FacesValidator("com.bookmgr.jsf.validator.RegisterEmailValidator")
public class RegisterEmailValidator implements Validator{
     private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\." +
            "[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*" +
            "(\\.[A-Za-z]{2,})$";

    private Pattern pattern;
    private Matcher matcher;

    public RegisterEmailValidator(){
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    @Override
    public void validate(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {
        ResourceBundle bundle = ResourceBundle.getBundle("lang");
        matcher = pattern.matcher(value.toString());
        EntityFacade facade = (EntityFacade)
                context.getViewRoot().getAttributes().
                        get(EntityFacade.EF_NAME);
        if(!matcher.matches()){

            FacesMessage msg =
                    new FacesMessage("E-mail validation failed.",
                            "Invalid E-mail format.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);

        }  else {
            if (facade.findAccountByEmail(value.toString()) != null){
                 FacesMessage msg =
                    new FacesMessage("Cet Email est déja utilisé, si vous avez oublié " +
                            "votre mot de passe veuillez en demander un nouveau.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
           throw new ValidatorException(msg);
            }
        }



    }
}
