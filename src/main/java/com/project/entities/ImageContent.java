package com.project.entities;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 16/12/11
 * Time: 00:29
 * To change this template use File | Settings | File Templates.
 */
@Entity
@DiscriminatorValue(value = "IMAGE")
public class ImageContent extends Content implements Serializable {

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH,CascadeType.PERSIST},
            fetch = FetchType.EAGER, orphanRemoval = true)
    private ImageContentFile image;




    public ImageContentFile getImage() {
        return image;
    }

    public void setImage(ImageContentFile image) {
        this.image = image;
        if (this.image != null){
            this.image.setImageContent(this);
        }
    }

    @PreRemove
    public void onPreRemove(){
        File file = new File(getEvent().getEventPathDirectoryFile() , this.image.getName() );
        file.delete();
    }

    @Override
    public String getVideo() {
        return null;
    }

    @Override
    public void setVideo(String video) {
    }

    @Override
    public String getResizedVideo(String width, String height) {
        return null;
    }

    @Override
    public UploadedFile getFile() {
        return this.image;
    }

    public ImageContent clone() throws CloneNotSupportedException {
        final ImageContent clone = (ImageContent) super.clone();

        return clone;
    }
}
