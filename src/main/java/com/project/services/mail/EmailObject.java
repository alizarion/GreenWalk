package com.project.services.mail;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeInstance;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 31/07/12
 * Time: 21:24
 * To change this template use File | Settings | File Templates.
 */
public abstract class EmailObject {


    /** Context used to build emails parts. */
    private VelocityContext velocityContext;
    /** Velocity instance used to build email parts. */
    private RuntimeInstance instance;

    private VelocityEngine velocityEngine;

       /** Subject template name. */
    public static final String SUBJECT_VM = "subject.vm";
    /** HTML template name. */
    public static final String HTML_VM = "html.vm";
    /** Text template name. */
    public static final String TEXT_VM = "text.vm";

    private String toAddr;
    private String replyToAddr;
    private String subjectVM;


    protected EmailObject() {
        this.velocityContext = new VelocityContext();
        this.velocityEngine = new VelocityEngine();
    }

    public abstract String getHTML();
    public abstract String getText();
    public abstract String getSubject();


    public String getReplyToAddr() {
        return replyToAddr;
    }

    public void setReplyToAddr(String replyToAddr) {
        this.replyToAddr = replyToAddr;
    }

    public String getSubjectVM() {
        return subjectVM;
    }

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public void setSubjectVM(String subjectVM) {
        this.subjectVM = subjectVM;
    }

    public String getToAddr() {
        return toAddr;
    }

    public void setToAddr(String toAddr) {
        this.toAddr = toAddr;
    }


    public RuntimeInstance getInstance() {
        return instance;
    }



    public void setInstance(RuntimeInstance instance) {
        this.instance = instance;
    }

    public VelocityContext getVelocityContext() {
        return velocityContext;
    }

    public void setVelocityContext(VelocityContext velocityContext) {
        this.velocityContext = velocityContext;
    }
}
