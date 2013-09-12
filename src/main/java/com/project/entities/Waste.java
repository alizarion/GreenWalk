package com.project.entities;

import com.project.Helper;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 12/09/13
 * Time: 16:54
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = Helper.ENTITIES_CATALOG,name = "waste")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries({
        @NamedQuery(name= Content.ALL_ABSOLUTE_CONTENT,
                hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                query="select w "
                        + " from Waste w  ")})
@DiscriminatorColumn(name = "type")
public class Waste {

    @Id
    @TableGenerator(name="Waste_SEQ", table="sequence", catalog = Helper.ENTITIES_CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="Waste_SEQ")
    @Column
    private String id;

    @Column(updatable = false,insertable = false)
    private String type;



}
