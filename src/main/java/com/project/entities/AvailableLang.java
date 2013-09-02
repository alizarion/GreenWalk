package com.project.entities;

/**
 * @author selim@openlinux.fr.
 */
public enum  AvailableLang {

    FR("fr"),
    EN("en");

    private String key;

    private AvailableLang() {

    }

    private AvailableLang(final String key) {
       this.key = key;
    }

    public String getKey() {
        return key;
    }

}
