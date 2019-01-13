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
 * @author Chirstof Weickhardt
 */
@Entity
public class Town implements Serializable, Comparable<Town> {

    /**
     * Serial verrsion of uid for primary key
     */
    private static final long serialVersionUID = 1L;

    /**
     * Primary key of the town object
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tid;

    /**
     * Name of the town
     */
    private String name;

    /**
     * PLZ of the town
     */
    private int plz;

    /**
     * Constructor to create a town with name and plz
     *
     * @param plz PLZ of the town
     * @param name Name of the town
     */
    public Town(int plz, String name) {
        this.plz = plz;
        this.name = name;
    }

    /**
     * Constructor to create a empty town
     */
    public Town() {
    }

    /**
     * Getter of the primary key
     *
     * @return ID of the town
     */
    public Long getTid() {
        return tid;
    }

    /**
     * Setter of the primary key
     *
     * @param tid ID of the town
     */
    public void setTid(Long tid) {
        this.tid = tid;
    }

    /**
     * Needed function to create a hash out of the primary key
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tid != null ? tid.hashCode() : 0);
        return hash;
    }

    /**
     * Function to check if two objects are the same
     * @param object Object to check to
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the tid fields are not set
        if (!(object instanceof Town)) {
            return false;
        }
        Town other = (Town) object;
        return !((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid)));
    }

    /**
     * Getter of the plz
     *
     * @return Returns the plz of the town
     */
    public int getPlz() {
        return plz;
    }

    /**
     * Setter of the name of the town
     *
     * @param name Name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter of the plz of the town
     *
     * @param plz PLZ of the town
     */
    public void setPlz(int plz) {
        this.plz = plz;
    }

    /**
     * Getter of the name of the town
     *
     * @return Returns the name of the town
     */
    public String getName() {
        return name;
    }

    /**
     * Sets all properties of a given town to the current one
     *
     * @param town Town to be set
     */
    public void setAll(Town town) {
        this.name = town.name;
        this.plz = town.plz;
    }

    /**
     * Comparable function of two towns
     *
     * @param t Town to compare to
     */
    @Override
    public int compareTo(Town t) {
        return this.plz == t.getPlz() ? t.name.compareToIgnoreCase(t.getName()) : this.plz - t.getPlz();
    }

}
