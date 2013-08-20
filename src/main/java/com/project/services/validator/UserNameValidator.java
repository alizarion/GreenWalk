package com.project.services.validator;

import com.bookmgr.EntityFacade;
import com.bookmgr.entity.biz.Credential;

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
@FacesValidator("com.bookmgr.jsf.validator.UserNameValidator")
public class UserNameValidator implements Validator{

    private static final String USER_ACCOUNT_PATTERN = "^[0-9a-zA-Z]+";

    private Pattern pattern;
    private Matcher matcher;

    public UserNameValidator() {
        this.pattern = Pattern.compile(USER_ACCOUNT_PATTERN);
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        matcher = pattern.matcher(value.toString());
        if(value.toString().length() <= 2){
            FacesMessage msg =
                    new FacesMessage("Ce champ doit contenir 3 caracteres minimum.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
        if(!matcher.matches()){
            FacesMessage msg =
                    new FacesMessage("Ce champ ne peux contenir que des caracteres alphanumŽrique");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }

        EntityFacade facade = (EntityFacade)
                context.getViewRoot().getAttributes().
                        get(EntityFacade.EL_NAME);
        final Credential credential = facade.getCredentialByUserName(value.toString());

        if(credential != null){
            FacesMessage msg =
                    new FacesMessage("Ce nom d'utilisateur existe deja, veuillez en saisir un autre");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}
