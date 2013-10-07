package com.project;

import org.primefaces.model.map.LatLng;

import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 02/08/13
 * Time: 11:34
 * To change this template use File | Settings | File Templates.
 */
public  class Helper {

    public static final String ENTITIES_CATALOG = "green_walk";

    public static File getUserDirectoryPath(){
        ResourceBundle bundle = ResourceBundle.getBundle("app");
        return new File(getRootDirectoryPath(),bundle.getString("USER_DIRECTORY"));
    }


    public static File getRootDirectoryPath(){
        ResourceBundle bundle = ResourceBundle.getBundle("app");
        return new File(bundle.getString("ROOT_DIRECTORY"));
    }


    public  static String getAppCatalogName(){
        ResourceBundle bundle = ResourceBundle.getBundle("app");
        return  bundle.getString("APP_CATALOG");
    }

    public  static String getMailTemplatePathDirectory(){
           ResourceBundle bundle = ResourceBundle.getBundle("app");
           return  bundle.getString("MAIL_TEMPLATE_DIRECTORY");
       }

    public static File getTempDirectoryPath(){
        ResourceBundle bundle = ResourceBundle.getBundle("app");
        return new File(getRootDirectoryPath(),bundle.getString("TEMP_DIRECTORY"));
    }

    public static LatLng getLatLngFromSeparatedString(String latlng){
       if (latlng!= null){
           if (latlng.contains(",")){
               String [] latLngTab =  latlng.split(",");
               LatLng latLng =  new LatLng(Double.parseDouble(latLngTab[0]),Double.parseDouble(latLngTab[1]));
               return latLng;
           }
       }
       return null;
    }

    public static String getRandomHash(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

}
