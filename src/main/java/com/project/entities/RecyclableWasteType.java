package com.project.entities;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 13/09/13
 * Time: 11:17
 * To change this template use File | Settings | File Templates.
 */
public enum RecyclableWasteType {

    ALU("ALU"),
    CARDBOARD("CARDBOARD"),
    GLASS("GLASS"),
    PAPER("PAPER"),
    PLASTICS("PLASTICS"),
    STEEL("STEEL");

    private String keyLabel;


    private RecyclableWasteType() {
        this.keyLabel = keyLabel;
    }

    private RecyclableWasteType(String keyLabel) {
        this.keyLabel = keyLabel;
    }
}
