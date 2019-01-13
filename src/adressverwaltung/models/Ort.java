/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung.models;

/**
 *
 * @author Christof Weickhardt
 * @author Nicola Temporal
 */
public class Ort {

    private String name;
    private int plz;

    public Ort() {
    }

    public Ort(String name, int plz) {
        this.name = name;
        this.plz = plz;
    }

    public String getName() {
        return name;
    }

    public int getPlz() {
        return plz;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlz(int plz) {
        this.plz = plz;
    }
    
    
    
}
