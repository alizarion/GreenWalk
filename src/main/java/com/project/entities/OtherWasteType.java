package com.project.entities;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 13/09/13
 * Time: 11:17
 * To change this template use File | Settings | File Templates.
 */
public enum OtherWasteType {

    TEXTILE("TEXTILE"),
    ELECTRONIC("ELECTRONIC");

    private String keyLabel;

    private OtherWasteType() {

    }


    private OtherWasteType(String keyLabel) {
        this.keyLabel = keyLabel;
    }

    public String getKeyLabel() {
        return keyLabel;
    }

    public void setKeyLabel(String keyLabel) {
        this.keyLabel = keyLabel;
    }
}
