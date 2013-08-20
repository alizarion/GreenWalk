package com.project.entities;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 16/12/11
 * Time: 00:28
 * To change this template use File | Settings | File Templates.
 */
@Entity
@DiscriminatorValue("VIDEO")
public class VideoContent extends Content implements Serializable {

    public final static String YOUTUBE_SERVICENAME= "YOUTUBE";

    public final static String DAILYMOTION_SERVICENAME= "DAILYMOTION";

    public final static String VIMEO_SERVICENAME= "VIMEO";


    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH},
            fetch = FetchType.EAGER, orphanRemoval = true)
    private VideoContentFile icon;

    @Column(name = "service_name",length = 1024)
    private String service;

    @Column(name = "video_id",length = 1024)
    private String video_id;

    @Column(name = "video",length = 2048)
    private String video;

    public VideoContent() {
        super();
    }

    @Override
    public UploadedFile getFile() {
        return icon;
    }

    public void setIcon(VideoContentFile icon) {
        this.icon = icon;
        this.icon.setVideoContent(this);
    }


    public VideoContentFile getIcon() {
        return icon;
    }

    @Override
    public String getVideo() {
        return this.video;
    }

    @Override
    public void setVideo(String video) {
        super.setLasModificationDate(new Date());
        this.video = video;
    }

    @Override
    public String getResizedVideo(String width,String height ) {
        if(this.video != null){
            String resizedVideo  = this.video;
            resizedVideo = resizedVideo.replaceAll("width=\"[0-9]{1,4}\"", "width=\""+width+"\"");
            resizedVideo =resizedVideo.replaceAll("height=\"[0-9]{1,4}\"", "height=\""+height+"\"");
            return resizedVideo;
        } else {
            return this.video;
        }
    }


    public String getEmbedVideoCode(HttpServletRequest request) throws
            URISyntaxException, MalformedURLException, UnsupportedEncodingException {
        String media = request.getParameter("media");
        String url = request.getParameter("url");

        if (url.contains("http://vimeo.com")){
            String videoId = url.substring(url.lastIndexOf("/")+1,url.length());
            if (videoId == null){
                return null;
            } else {
                this.video_id = videoId;
                this.service = VIMEO_SERVICENAME;
                return "<iframe src='http://player.vimeo.com/video/" + videoId +
                        "?title=0&amp;byline=0&amp;portrait=0' width=\"550\" height=\"315\"" +
                        " frameborder='0' webkitAllowFullScreen mozallowfullscreen " +
                        "allowFullScreen></iframe>";
            }
        }
        if (media.contains("http://img.youtube.com/vi/")) {
            int posi = media.indexOf("/vi/");
            String videoId = media.substring(media.lastIndexOf("/vi/") + 4,media.indexOf("/0.jpg"));

            if (videoId == null){
                return null;
            } else {
                this.video_id = videoId;
                this.service = YOUTUBE_SERVICENAME;
                return "<iframe width=\"550\" height=\"315\" src='http://www.youtube.com/embed/"
                        + videoId +"' frameborder='0' allowfullscreen></iframe>";
            }
        }

        if (media.contains("http://www.dailymotion.com/")) {
            int posi = media.indexOf("/video/");
            String videoId = media.substring(media.lastIndexOf("/video/") + 7,media.length());

            if (videoId == null){
                return null;
            } else {
                this.video_id = videoId;
                this.service = DAILYMOTION_SERVICENAME;
                return "<iframe frameborder='0' width=\"550\" height=\"315\"" +
                        " src='http://www.dailymotion.com/embed/video/"+videoId+"'></iframe>";
            }
        }
        return null;
    }

    public static Map<String, List<String>> getUrlParameters(String url)
            throws UnsupportedEncodingException {
        Map<String, List<String>> params = new HashMap<String, List<String>>();
        String[] urlParts = url.split("\\?");
        if (urlParts.length > 1) {
            String query = urlParts[1];
            for (String param : query.split("&")) {
                String pair[] = param.split("=");
                String key = URLDecoder.decode(pair[0], "UTF-8");
                String value = "";
                if (pair.length > 1) {
                    value = URLDecoder.decode(pair[1], "UTF-8");
                }
                List<String> values = params.get(key);
                if (values == null) {
                    values = new ArrayList<String> ();
                    params.put(key, values);
                }
                values.add(value);
            }
        }
        return params;
    }


    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public VideoContent clone() throws CloneNotSupportedException {
        VideoContent videoContent = (VideoContent) super.clone();
        videoContent.setId(null);
        return videoContent;

    }

}
