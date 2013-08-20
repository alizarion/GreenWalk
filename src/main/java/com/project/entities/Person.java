package com.project.entities;

import com.project.Helper;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 02/08/13
 * Time: 11:33
 * To change this template use File | Settings | File Templates.
 */
@Entity
@NamedQuery(name = Person.FIND_ALL,query = "select p from Person p")
@Table(catalog = Helper.ENTITIES_CATALOG,name = "person")
public class Person {

    public static final String FIND_ALL = "Person.FIND_ALL";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "phoneNumber")
    private String phoneNumber;


    public Person() {

        }

    public Person(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Person)) return false;

        Person person = (Person) o;

        if (firstName != null ? !firstName.equals(person.firstName) :
                person.firstName != null)
            return false;

        if (lastName != null ? !lastName.equals(person.lastName) :
                person.lastName != null)
            return false;
        if (phoneNumber != null ? !phoneNumber.equals(person.phoneNumber) :
                person.phoneNumber != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        return result;
    }
}
