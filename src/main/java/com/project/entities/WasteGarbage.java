package com.project.entities;

import com.project.Helper;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 13/09/13
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema= Helper.ENTITIES_CATALOG, name="garbage")
public class WasteGarbage  {

    @Id
    @TableGenerator(name="WasteGarbage_SEQ", table="sequence", catalog = Helper.ENTITIES_CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="WasteGarbage_SEQ")
    @Column
    private String id;

    @Column
    private Long quantity;

    @ManyToOne
    private Waste waste;



}
