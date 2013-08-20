package com.project.entities;

import com.project.Helper;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;
import java.io.File;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 20/01/12
 * Time: 20:13
 * To change this template use File | Settings | File Templates.
 */
@Entity
@DiscriminatorValue(value = AvatarImageFile.DISCRIMINATOR_VALUE)
public class AvatarImageFile extends UploadedFile  implements Serializable {

    public static final String DISCRIMINATOR_VALUE = "AvatarImageFile";

    @OneToOne(mappedBy = "avatarImageFile")
    @Fetch(FetchMode.SELECT)
    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public File getFullPath() {
        return new File(Helper.getUserDirectoryPath()
                + File.pathSeparator +
                this.account.getId().toString() +
                File.pathSeparator +
                this.getId().toString());
    }

    public AvatarImageFile() {
    }


    public AvatarImageFile(String fileName) {
        super(fileName);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }


    @PreRemove
    public void onPreRemove(){
        File file = getFullPath();
        if (file != null){
            file.delete();
        }
    }



    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
