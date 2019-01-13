/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung.errors;

/**
 *
 * @author Christof Weickhardt
 */
public class TownNotFoundError extends Exception {

    /**
     * Constructor to create a custom OrtNotFoundError exception
     */
    public TownNotFoundError() {
        super("Could not find the requested ort");
    }

}
