package com.project.web.controlers;



import com.project.services.EntityFacade;
import org.apache.log4j.Logger;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.ResourceBundle;

/**
 * @author selim.bensenouci
 */
@ManagedBean
@ViewScoped
public class LoginCtrl  implements Serializable {


    private CommandButton commandButton;

    private final static Logger LOG = Logger.getLogger(LoginCtrl.class);
    private String username;
    private String password;
    private String from;
    private String requestQueryString;

    @EJB
    EntityFacade facade;

    @Inject
    SessionAttributeCtrl sessionAttribute;


    @PostConstruct
    public void postInit(){
        HttpServletRequest request  =  (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        this.username = (String) request.getAttribute("username");
        this.requestQueryString = request.getQueryString();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Action method that processes authentication.
     * @throws java.io.IOException On error.
     */
    public void login() throws IOException {
        final HttpServletRequest request = (HttpServletRequest) FacesContext.
                getCurrentInstance().
                getExternalContext().
                getRequest();
        ResourceBundle bundle = ResourceBundle.getBundle("lang");
        final HttpServletResponse response = (HttpServletResponse) FacesContext.
                getCurrentInstance().
                getExternalContext().
                getResponse();
        try {
            request.login(this.username,this.password);
            final RequestContext requestContext =
                    RequestContext.getCurrentInstance();
            requestContext.addCallbackParam("isConnected", true);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            bundle.getString("you.are.connected-"+
                                    sessionAttribute.getSelectedLang().toString()), null));
            if (this.from != null){
                HttpServletRequest requests = (HttpServletRequest)
                        FacesContext.getCurrentInstance().getExternalContext().getRequest();
                FacesContext.getCurrentInstance().getExternalContext().
                        redirect(this.from+"?"+this.requestQueryString);

            }   else {
                FacesContext.getCurrentInstance().getExternalContext().
                        redirect("/home");
            }


        } catch (final ServletException e) {
            // e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(commandButton.
                    getClientId(FacesContext.getCurrentInstance()),
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            bundle.getString("bad.password.or.username-"+
                                    sessionAttribute.getSelectedLang().toString()), null));

        }
    }



    /**
     * Logs out current user.
     * @param facesContext FacesContext to use
     * @return "logout" (JSF outcome)
     */
    public void logout(final FacesContext facesContext) throws IOException {
        final ExternalContext externalContext =
                facesContext.getExternalContext();
        LOG.info("Logout " + externalContext.getRemoteUser());
        externalContext.invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().
                redirect("/home");

    }
}