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
public class CanNotConnectToDatabaseError extends Exception {

    /**
     * Constructor to create a custom CanNotConnectToDatabaseError exception
     */
    public CanNotConnectToDatabaseError() {
        super("Could not connect to the database");
    }

}
