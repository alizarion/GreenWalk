package com.project.services.validator;

import com.bookmgr.EntityFacade;
import com.bookmgr.entity.biz.Account;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author selim.bensenouci
 */
@FacesValidator("com.bookmgr.jsf.validator.EmailValidator")
public class EmailValidator implements Validator{
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\." +
            "[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*" +
            "(\\.[A-Za-z]{2,})$";

    private Pattern pattern;
    private Matcher matcher;

    public EmailValidator(){
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    @Override
    public void validate(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {

        matcher = pattern.matcher(value.toString());

        matcher = pattern.matcher(value.toString());
        EntityFacade facade = (EntityFacade)
                context.getViewRoot().getAttributes().
                        get(EntityFacade.EL_NAME);
        if(!matcher.matches()){

            FacesMessage msg =
                    new FacesMessage("E-mail validation failed.",
                            "Invalid E-mail format.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);

        }  else {
            if (facade.findAccountByEmail(value.toString()) != null){
                context.getExternalContext().getUserPrincipal().getName();
                Account account = facade.findAccountByName(context.getExternalContext().getUserPrincipal().getName());
                if(!account.getEmailAddress().equals(value.toString())){
                    FacesMessage msg =
                            new FacesMessage("Cet Email est déja utilisé");
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    throw new ValidatorException(msg);
                }
            }
        }



    }
}
