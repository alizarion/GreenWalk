package com.project.entities;

import com.project.Helper;
import org.primefaces.model.map.LatLng;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 18/12/12
 * Time: 11:52
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = Helper.ENTITIES_CATALOG, name = "position")
public class Position implements Serializable {

    @Id
    @TableGenerator(name="position_SEQ", table="sequence", catalog = Helper.ENTITIES_CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="position_SEQ")
    @Column
    private Long id;

    @OneToOne(optional = true)
    private Address address;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    public Position() {
    }

    public Position(Address address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }



    public LatLng getAsLatLng(){
        if (this.latitude != null && this.longitude != null){
            return new LatLng(this.latitude,this.longitude);
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (id != null ? !id.equals(position.id)
                : position.id != null) return false;
        if (latitude != null ? !latitude.equals(position.latitude)
                : position.latitude != null) return false;
        if (longitude != null ? !longitude.equals(position.longitude)
                : position.longitude != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return latitude.toString().replaceAll(",",".") + "," + longitude.toString().replaceAll(",",".") ;
    }
}
