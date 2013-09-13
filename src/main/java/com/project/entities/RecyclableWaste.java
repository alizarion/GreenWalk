package com.project.entities;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 13/09/13
 * Time: 11:01
 * To change this template use File | Settings | File Templates.
 */
@Entity
@DiscriminatorValue(value = RecyclableWaste.DISCRIMINATOR_VALUE)
public class RecyclableWaste extends Waste {

    public final static String  DISCRIMINATOR_VALUE="RECYCLABLE";

    @Column
    @Enumerated(EnumType.STRING)
    private RecyclableWasteType recyclableWasteType;


}
