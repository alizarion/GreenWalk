package com.project.entities;

import com.project.Helper;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 30/12/11
 * Time: 23:07
 * To change this template use File | Settings | File Templates.
 */
@Entity
@DiscriminatorValue(value = "imageContent")
public class ImageContentFile extends UploadedFile implements Serializable {

    @OneToOne(mappedBy = "image")
    private ImageContent imageContent;
    private final static Logger LOG = Logger.getLogger(ImageContentFile.class);


    public final static double DISPLAY_HEIGHT = 7000;
    public final static double DISPLAY_WIDTH = 192;

    @Column
    private String width;
    @Column
    private String height;

    public ImageContent getImageContent() {
        return imageContent;
    }

    public void setImageContent(ImageContent imageContent) {
        this.imageContent = imageContent;
    }

    public ImageContentFile() {

    }

    public String getHeightForDisplay(String sWidth) {
        try{
            Double width = Double.parseDouble(sWidth);
            if (this.width != null && this.height != null){
                double thumbRatio =   width / DISPLAY_HEIGHT;
                Integer newHeight =(int) DISPLAY_HEIGHT;
                Integer newWidth =(int) width.intValue();
                double aspectRatio = (double) Double.parseDouble(this.width) / Double.parseDouble(this.height);
                if (thumbRatio < aspectRatio) {
                    newHeight = (int) (newWidth / aspectRatio);
                } else {
                    newWidth = (int) (DISPLAY_HEIGHT * aspectRatio);
                }

                return newHeight.toString();    }
            else {return null; }
        }catch (NumberFormatException e)   {
            return null;
        }
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    @Override
    public File getFileFullPath() {
        if (getTemporary()){
            return new File(Helper.getTempDirectoryPath() +
                    File.separator + getId().toString());
        }    else {
            if (this.imageContent != null){
                if (this.imageContent.getEvent() != null) {
                    return new File(this.imageContent.
                            getEvent().getEventPathDirectoryFile(),
                            this.getId().toString());
                }
            }else {
                return null;
            }
            return null;
        }
    }

    @Override
    public File getResizedFileFullPath(Integer witdh, Integer weight) {
        if (getTemporary()){
                 return new File(Helper.getTempDirectoryPath() +
                         File.separator + getId().toString() + "_" +witdh + "_" + weight);


             }    else {
                 if (this.imageContent != null){
                     if (this.imageContent.getEvent() != null) {
                         return new File(this.imageContent.
                                 getEvent().getEventPathDirectoryFile(),
                                 this.getId().toString() + "_" +witdh + "_" + weight);
                     }
                 }else {
                     return null;
                 }
                 return null;
             }
    }

    public ImageContentFile(String fileName) {
        super(fileName);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @PreRemove
    public void onPreRemove(){
        File file = getFileFullPath();
        if (file != null){
            file.delete();
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


}
