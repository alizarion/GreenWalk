package com.project.entities;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 13/09/13
 * Time: 11:17
 * To change this template use File | Settings | File Templates.
 */
public enum RecyclableWasteType {

    ALU("aluminium"),
    CARDBOARD("cardboard"),
    GLASS("glass"),
    PAPER("paper"),
    PLASTICS("plastic"),
    WOOD("wood"),
    STEEL("steel");

    private String keyLabel;


    private RecyclableWasteType() {
        this.keyLabel = keyLabel;
    }

    private RecyclableWasteType(String keyLabel) {
        this.keyLabel = keyLabel;
    }
}
