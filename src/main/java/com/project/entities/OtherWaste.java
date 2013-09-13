package com.project.entities;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 13/09/13
 * Time: 11:13
 * To change this template use File | Settings | File Templates.
 */
@Entity
@DiscriminatorValue(value = OtherWaste.DISCRIMINATOR_VALUE)
public class OtherWaste extends Waste {

    public final static String  DISCRIMINATOR_VALUE="OTHER";

    @Enumerated(EnumType.STRING)
    @Column
    private OtherWasteType otherWasteType;





}
