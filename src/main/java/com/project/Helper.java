package com.project;

import java.io.File;
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
        return new File(getRootDirectoryPath(),bundle.getString("TMP_DIRECTORY"));
    }

}
