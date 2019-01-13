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
public class WriteError extends Exception {

    /**
     * Constructor to create a custom WriteError exception
     */
    public WriteError() {
        super("Could not write changes to the database. Please check your connection and table setup.");
    }

}
