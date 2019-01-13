/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Christof Weickhardt
 */
@Entity
public class Person implements Serializable {

    /**
     * Serial verrsion of uid for primary key
     */
    private static final long serialVersionUID = 1L;

    /**
     * Primary key of the person object
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pid;

    /**
     * Last name of the person
     */
    private String lastName;

    /**
     * First name of the person
     */
    private String firstName;

    /**
     * Adress of the person
     */
    private String address;

    /**
     * Town primary key
     */
    private long tid = -1;

    /**
     * Phone nr of the person
     */
    private String phone;

    /**
     * Mobile nrr of the person
     */
    private String mobile;

    /**
     * Email of the person
     */
    private String email;

    /**
     * Constructor to populate a person object
     *
     * @param lastName Last name of the person
     * @param firstName First name of the person
     * @param street Street of the person
     * @param tid Town primary key
     * @param phone Phone nr of the person
     * @param mobile Mobile nr of the person
     * @param email Email of the person
     */
    public Person(String lastName, String firstName, String street, long tid, String phone, String mobile, String email) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.address = street;
        this.tid = tid;
        this.phone = phone;
        this.mobile = mobile;
        this.email = email;
    }

    /**
     * Constructor to create a empty person object
     */
    public Person() {
    }

    /**
     * Constructor to create a person with first and last anme
     *
     * @param firstName First name of the person
     * @param lastName Last name of the person
     */
    public Person(String firstName, String lastName) {
        this.lastName = lastName;
        this.firstName = firstName;
    }

    /**
     * Needed function to create a hash out of the primary key
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pid != null ? pid.hashCode() : 0);
        return hash;
    }

    /**
     * Function to check if two objects are the same
     * @param object Object to check to
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.pid == null && other.pid != null) || (this.pid != null && !this.pid.equals(other.pid))) {
            return false;
        }
        return true;
    }

    /**
     * Getter of the email
     *
     * @return Returns the mail as a String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter of the mobile nr
     *
     * @return Returns the mobile as a String
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Getter of the last name
     *
     * @return Returns the last anme
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Getter of the town primary key
     *
     * @return Returns the id of the town
     */
    public Long getOid() {
        return tid;
    }

    /**
     * Getter of the adress
     *
     * @return Returns the adress as a String
     */
    public String getAddress() {
        return address;
    }

    /**
     * Getter of the phone nr
     *
     * @return Returns the phone as a String
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Getter of the first name
     *
     * @return Returns the first anme
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Getter of the email
     *
     * @param email Returns the email as a String
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Setter of the mobile nr
     *
     * @param mobile Mobile nr to set to
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * Setter of the last name
     *
     * @param lastName Last name to set to
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Setter of the town Primary key
     *
     * @param oid Primary key of the town to set to
     */
    public void setOid(int oid) {
        this.tid = oid;
    }

    /**
     * Setter of the person primary key
     *
     * @param pid Primary key of the person to set to
     */
    public void setPid(Long pid) {
        this.pid = pid;
    }

    /**
     * Setter of the adress
     *
     * @param address Adress to set to
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Setter of the phone nr
     *
     * @param phone Phone nr to set to
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Setter of the first name
     *
     * @param firstName First name to set to
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter of the person primary key
     *
     * @return Returns the person ID
     */
    public Long getId() {
        return pid;
    }

    /**
     * Setter of the person primary key
     *
     * @param pid Primary key to set to
     */
    public void setId(Long pid) {
        this.pid = pid;
    }

    /**
     * Setter to replace the current person object
     *
     * @param person Person object to set to
     * @deprecated
     */
    public void setAll(Person person) {
        this.lastName = person.getLastName();
        this.firstName = person.getFirstName();
        this.address = person.getAddress();
        this.tid = person.getOid();
        this.phone = person.getPhone();
        this.mobile = person.getMobile();
        this.email = person.getEmail();
    }

}
