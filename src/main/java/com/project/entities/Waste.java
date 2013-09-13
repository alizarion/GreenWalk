package com.project.entities;

import com.project.Helper;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
        @NamedQuery(name= Waste.ALL_WASTES,
                hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                query="select w "
                        + " from Waste w  ")})
@DiscriminatorColumn(name = "type")
public abstract class Waste {

    public static final String ALL_WASTES = "Waste.ALL_WASTES";

    @Id
    @TableGenerator(name="Waste_SEQ", table="sequence", catalog = Helper.ENTITIES_CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="Waste_SEQ")
    @Column
    private String id;

    @Column(updatable = false,insertable = false)
    private String type;

    @Column
    private Long approximateWeight;

    @Column
    private String labelProperty;

    @ManyToMany
    private Set<SingleEvent> event = new HashSet<SingleEvent>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getApproximateWeight() {
        return approximateWeight;
    }

    public void setApproximateWeight(Long approximateWeight) {
        this.approximateWeight = approximateWeight;
    }

    public Set<SingleEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<SingleEvent> event) {
        this.event = event;
    }

    public String getLabelProperty() {
        return labelProperty;
    }

    public void setLabelProperty(String labelProperty) {
        this.labelProperty = labelProperty;
    }
}
