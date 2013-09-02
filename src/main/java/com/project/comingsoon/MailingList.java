package com.project.comingsoon;

import com.project.Helper;

import javax.persistence.*;
import java.util.Date;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(catalog = Helper.ENTITIES_CATALOG, name = "mailing_list")
public class MailingList {


    @Id
    @TableGenerator(name="MailingList_SEQ", table="sequence", catalog = Helper.ENTITIES_CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy= GenerationType.TABLE, generator="MailingList_SEQ")
    @Column
    private Long id;

    @Column
    private Date creationDate;

    @Column
    private String email;

    private String remoteAddress;

    public MailingList() {
        this.creationDate = new Date();

    }


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }
}
