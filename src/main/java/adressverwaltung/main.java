/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung;

import adressverwaltung.forms.AddressForm;
import adressverwaltung.utils.DotEnv;
import adressverwaltung.utils.InOut;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main operator class
 *
 * @author Christof Weickhardt, Nicola Temporal
 */
public class main {

    /**
     * Static available io
     */
    public static InOut io;

    /**
     * Static available adress form
     */
    public static AddressForm af;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            io = new InOut(null);

            af = new AddressForm(io);
            if (DotEnv.getDotEnv().keySet().contains("DATABASE_USE")) {
                af.setVisible(true);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Function to setup a new Connection
     *
     * @param connection Key value pair list of connection information
     * @return Returns a boolean true if the connection is successfully
     * established
     */
    public static boolean setupConnection(HashMap<String, String> connection) {
        try {
            io = new InOut(connection);
            af = new AddressForm(io);
            return true;
        } catch ( SQLException ex) {
            return false;
        } 
    }

    /**
     * Function to distplay the adress form
     *
     * @throws SQLException If not able to get informations from the database
     * @throws CanNotConnectToDatabaseError If not able to connect to the
     * database
     */
    public static void viewAdressForm() throws SQLException{
        if (af == null) {
            af = new AddressForm(io);
        }
        af.setVisible(true);
        af.requestFocus();
    }
}
