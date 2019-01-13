/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung.errors;

/**
 *
 * @author Chrristof Weickhardt
 */
public class DatabaseSelfHealingError extends Exception{
    /**
     * Constructor to create a custom DatabaseSelfHealingError exception
     */
    public DatabaseSelfHealingError() {
        super("Could not reconnect to healed database schema");
    }
}
