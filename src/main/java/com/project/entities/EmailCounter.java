package com.project.entities;

import com.project.Helper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author selim.bensenouci
 */
@Entity
@Table(catalog = Helper.ENTITIES_CATALOG,name = "email_counter")
@NamedQueries({
        @NamedQuery(name=EmailCounter.COUNT_MAIL_ON_TIME,
                query="select count(mailcount)"
                        + " from EmailCounter mailcount "
                        + " where mailcount.sendedAt > :dateStart and mailcount.sendedAt < :dateEnd"
                        + "") })
public class EmailCounter implements Serializable{

        public static final String COUNT_MAIL_ON_TIME =
            "COUNT_MAIL_ON_TIME" ;

     @Id
    @TableGenerator(name="emailcount_SEQ", table="sequence",catalog = Helper.ENTITIES_CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy= GenerationType.TABLE, generator="emailcount_SEQ")
    @Column
    private Long id;

    @Column
    private String mailTo;

    @Column
    private String mailSubject;


    @Column
    private Date sendedAt;

    public Long getId() {
        return id;
    }

    public EmailCounter() {
    }

    public EmailCounter(String mailTo, String mailSubject, Date sendedAt) {
        this.mailTo = mailTo;
        this.mailSubject = mailSubject;
        this.sendedAt = sendedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public Date getSendedAt() {
        return sendedAt;
    }

    public void setSendedAt(Date sendedAt) {
        this.sendedAt = sendedAt;
    }
}
