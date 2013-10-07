package com.project.entities;

import com.project.Helper;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 29/09/11
 * Time: 20:53
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = Helper.ENTITIES_CATALOG,name = "address")
@NamedQueries({
        @NamedQuery(name= Address.FIND_ADDRESS_BY_COUNTRY,
                hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                query="select distinct address.country  "
                        + " from Address address where  address.country like :countryQuery" +
                        " group by address.country"),
        @NamedQuery(name= Address.FIND_ADDRESS_BY_CITY,
                hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                query="select distinct  address.city "
                        + " from Address address where   address.city like :cityQuery " +
                        " group by address.city")})
public class Address implements Serializable {

    public final static String FIND_ADDRESS_BY_COUNTRY = "FIND_ADDRESS_BY_COUNTRY" ;
    public final static String FIND_ADDRESS_BY_CITY = "FIND_ADDRESS_BY_CITY" ;


    @Id
    @TableGenerator(name="Address_SEQ", table="sequence", catalog = Helper.ENTITIES_CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="Address_SEQ")
    @Column
    private Long id;

    @Column
    private String Street;

    @Column
    private String city;

    @Column
    private String country;

    @Column
    private String zipCode;

    @Column
    private String number;

    @OneToOne(mappedBy="address", cascade=CascadeType.ALL)
    private Position position;

    @OneToOne(optional = true)
    private Account account;

    public Address() {
        this.position = new Position(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Position getPosition() {
        if (this.position == null){
            this.position = new Position(this);
        }
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return  number +
                " " + Street + ' ' +
                city + ' ' +
                country + ' ' +
                zipCode + ' ' ;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (Street != null ? !Street.equals(address.Street) : address.Street != null) return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        if (country != null ? !country.equals(address.country) : address.country != null) return false;
        if (id != null ? !id.equals(address.id) : address.id != null) return false;
        if (number != null ? !number.equals(address.number) : address.number != null) return false;
        if (zipCode != null ? !zipCode.equals(address.zipCode) : address.zipCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (Street != null ? Street.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (zipCode != null ? zipCode.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        return result;
    }
}
