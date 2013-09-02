package com.project.entities;

import com.project.Helper;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;

/**
 * @author selim.bensenouci
 */
@Entity
@DiscriminatorValue(value = "videoContent")
public class VideoContentFile extends UploadedFile implements Serializable {

    @OneToOne(mappedBy = "icon")
    private VideoContent videoContent;

    public final static double DISPLAY_HEIGHT = 7000;
    public final static double DISPLAY_WIDTH = 192;

    @Column
    private String width;
    @Column
    private String height;

    public void setVideoContent(VideoContent videoContent) {
        this.videoContent = videoContent;
    }


    public VideoContent getVideoContent() {
        return videoContent;
    }

    @Override
    public File getFileFullPath() {
        if (getTemporary()){
            return new File(Helper.getTempDirectoryPath() +
                    File.separator + getId().toString());

        }    else {
            if (this.videoContent != null){
                if (this.videoContent.getEvent() != null) {
                    return new File(this.videoContent.
                            getEvent().getEventPathDirectoryFile(),
                            this.getId().toString()) ;
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
                    File.separator + getId().toString()+ "_" +witdh + "_" + weight);

        }    else {
            if (this.videoContent != null){
                if (this.videoContent.getEvent() != null) {
                    return new File(this.videoContent.
                            getEvent().getEventPathDirectoryFile(),
                            this.getId().toString() + "_" +witdh + "_" + weight) ;
                }
            }else {
                return null;
            }
            return null;
        }
    }


    public VideoContentFile() {
    }

    public VideoContentFile(String fileName) {
        super(fileName);
    }

    @PreRemove
    public void onPreRemove(){

        File file = getFileFullPath();
        if (file != null){
            file.delete();
        }
    }

    public String getHeightForDisplay(String sWidth) {
        try{
            Double width = Double.parseDouble(sWidth);
            if (this.width != null && this.height != null){
                double thumbRatio =   width / DISPLAY_HEIGHT;
                Integer newHeight =(int) DISPLAY_HEIGHT;
                Integer newWidth =(int) width.intValue();
                double aspectRatio =
                        (double) Double.parseDouble(this.width) / Double.parseDouble(this.height);
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

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
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
    public int hashCode() {
        return super.hashCode();
    }
}
