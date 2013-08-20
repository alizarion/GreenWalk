package com.project.entities;

import com.project.Helper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 29/09/11
 * Time: 20:53
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = Helper.ENTITIES_CATALOG,name = "country")
public class Country implements Serializable {

    @Id
    @TableGenerator(name="Country_SEQ", table="sequence", catalog = Helper.ENTITIES_CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="Country_SEQ")
    @Column
    private int id;

    @Column
    private String label;

    @Column
    private String countryName;

    @Column
    private String currency;


    @OneToMany(mappedBy = "country")
    private List<Address> address = new ArrayList<Address>();

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
