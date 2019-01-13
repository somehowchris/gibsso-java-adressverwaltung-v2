/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung;

import adressverwaltung.errors.CanNotConnectToDatabaseError;
import adressverwaltung.errors.DatabaseSelfHealingError;
import adressverwaltung.forms.TownForm;
import adressverwaltung.forms.ConnectionForm;
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
     * Static available connection form
     */
    public static ConnectionForm cn;

    /**
     * Static available io
     */
    public static InOut io;

    /**
     * Static available adress form
     */
    public static AddressForm af;

    /**
     * Static available town form
     */
    public static TownForm tf;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            io = new InOut(null);

            cn = new ConnectionForm();

            af = new AddressForm(io);

            tf = new TownForm(io);
            if (DotEnv.getDotEnv().keySet().contains("DATABASE_USE")) {
                af.setVisible(true);
            } else {
                cn.setVisible(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CanNotConnectToDatabaseError ex) {
            viewConnectionSettings();
        } catch (DatabaseSelfHealingError ex) {
            main(null);
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
            cn.setVisible(false);
            af = new AddressForm(io);
            tf = new TownForm(io);
            return true;
        } catch (CanNotConnectToDatabaseError | SQLException ex) {
            return false;
        } catch (DatabaseSelfHealingError ex) {
            setupConnection(connection);
            return true;
        }
    }

    /**
     * Function to distplay the adress form
     *
     * @throws SQLException If not able to get informations from the database
     * @throws CanNotConnectToDatabaseError If not able to connect to the
     * database
     */
    public static void viewAdressForm() throws SQLException, CanNotConnectToDatabaseError {
        if (af == null) {
            af = new AddressForm(io);
        }
        af.setVisible(true);
        af.requestFocus();

        if (cn != null) {
            cn.setVisible(false);
        }
        if (tf != null) {
            tf.setVisible(false);
        }
    }

    /**
     * Function to display the town form
     *
     * @throws CanNotConnectToDatabaseError If not able to connect to the
     * database
     * @throws SQLException If not able to get informations from the database
     */
    public static void viewTownForm() throws CanNotConnectToDatabaseError, SQLException {
        if (tf == null) {
            tf = new TownForm(io);
        }
        tf.setVisible(true);
        tf.requestFocus();

        if (cn != null) {
            cn.setVisible(false);
        }
        if (af != null) {
            af.setVisible(false);
        }
    }

    /**
     * Function to view the connection settings
     */
    public static void viewConnectionSettings() {
        if (cn == null) {
            cn = new ConnectionForm();
        }
        cn.setVisible(true);
        cn.requestFocus();

        if (tf != null) {
            tf.setVisible(false);
        }
        if (tf != null) {
            af.setVisible(false);
        }
    }
}
