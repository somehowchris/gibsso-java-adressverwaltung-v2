/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung;

import adressverwaltung.forms.AddressForm;
import adressverwaltung.utils.DotEnv;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Main operator class
 *
 * @author Christof Weickhardt, Nicola Temporal
 * @since Release 2
 */
public class main {

    /**
     * Static available adress form
     */
    public static AddressForm af;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        af = new AddressForm();
        if (DotEnv.getDotEnv().keySet().contains("DATABASE_USE")) {
            af.setVisible(true);
        }
    }

    /**
     * Function to distplay the adress form
     */
    public static void viewAdressForm() throws SQLException {
        if (af == null) {
            af = new AddressForm();
        }
        af.setVisible(true);
        af.requestFocus();
    }
}
