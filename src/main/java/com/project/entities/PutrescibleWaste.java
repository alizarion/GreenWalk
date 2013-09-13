package com.project.entities;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 13/09/13
 * Time: 10:59
 * To change this template use File | Settings | File Templates.
 */
@Entity
@DiscriminatorValue(value = PutrescibleWaste.DISCRIMINATOR_VALUE)
public class PutrescibleWaste extends Waste {

    public final static String  DISCRIMINATOR_VALUE="PUTRESCIBLE";

    @Column
    @Enumerated(EnumType.STRING)
    private PutrescibleWasteType putrescibleWasteType;

}
