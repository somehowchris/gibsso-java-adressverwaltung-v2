/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import TestingHelpers.UIUtils;
import adressverwaltung.enums.DotEnvEnum;
import adressverwaltung.enums.SystemPropertyEnum;
import adressverwaltung.errors.CanNotConnectToDatabaseError;
import adressverwaltung.errors.DatabaseSelfHealingError;
import adressverwaltung.forms.ConnectionForm;
import adressverwaltung.utils.InOut;
import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import com.github.javafaker.Faker;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 *
 * @author chris
 */
public class ConnectionFormTest {

    ConnectionForm cf;
    InOut io;
    DB db;
    Faker faker;

    /**
     * Empty Constructor
     */
    public ConnectionFormTest() {

    }

    /**
     * Setup class
     */
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     * Tear down class
     */
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Setup before test
     * @throws SQLException
     */
    @Before
    public void setUp() throws Exception {
        int port = 3006;
        try (ServerSocket socket = new ServerSocket(0)) {
            port = socket.getLocalPort();
            socket.close();
        } catch (IOException ex) {
        }

        try {
            db = DB.newEmbeddedDB(port);
            db.start();
            HashMap<String, String> fakeKeys = new HashMap<>();
            fakeKeys.put(DotEnvEnum.DB_USE.get(), "true");
            fakeKeys.put(DotEnvEnum.HOST.get(), "localhost");
            fakeKeys.put(DotEnvEnum.PASSWORD.get(), "root");
            fakeKeys.put(DotEnvEnum.USER.get(), "");
            fakeKeys.put(DotEnvEnum.TABLE_NAME.get(), "test");
            fakeKeys.put(DotEnvEnum.PORT.get(), db.getConfiguration().getPort() + "");
            io = new InOut(fakeKeys);
        } catch (ManagedProcessException | SQLException | CanNotConnectToDatabaseError ex) {
            throw new Exception("Could not create database on port " + db.getConfiguration().getPort());
        } catch (DatabaseSelfHealingError ex) {
            Logger.getLogger(ConnectionFormTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        File f;
        if ((f = new File(SystemPropertyEnum.USER_HOME + "" + SystemPropertyEnum.FILE_SEPERATOR + ".env")).exists()) {
            f.renameTo(new File(SystemPropertyEnum.USER_HOME + "" + SystemPropertyEnum.FILE_SEPERATOR + ".env.original"));
        }
        cf = new ConnectionForm();
        faker = new Faker();
    }

    /**
     * Tear down after test
     */
    @After
    public void tearDown() {
        File f;
        if ((f = new File(SystemPropertyEnum.USER_HOME + "" + SystemPropertyEnum.FILE_SEPERATOR + ".env.original")).exists()) {
            if (new File(SystemPropertyEnum.USER_HOME + "" + SystemPropertyEnum.FILE_SEPERATOR + ".env").exists()) {
                new File(SystemPropertyEnum.USER_HOME + "" + SystemPropertyEnum.FILE_SEPERATOR + ".env").delete();
            }
            f.renameTo(new File(SystemPropertyEnum.USER_HOME + "" + SystemPropertyEnum.FILE_SEPERATOR + ".env"));
        }
    }

    /**
     * Checking on fsdb feature
     */
    @Test
    public void fsdb() {
        JRadioButton fs = (JRadioButton) UIUtils.getChildNamed(cf, "fs");

        assertNotNull(fs);

        fs.doClick();

        JButton clear = (JButton) UIUtils.getChildNamed(cf, "clearOrtest");

        assertNotNull(clear);

        clear.doClick();

        JButton go = (JButton) UIUtils.getChildNamed(cf, "letsgo");

        assertEquals(go.isEnabled(), true);

    }

    /**
     * Checking mysql db availability
     */
    @Test
    public void mysqlDb() {
        JRadioButton mysql = (JRadioButton) UIUtils.getChildNamed(cf, "mysql");

        assertNotNull(mysql);

        mysql.doClick();

        JTextField host = (JTextField) UIUtils.getChildNamed(cf, "Host");
        JTextField table = (JTextField) UIUtils.getChildNamed(cf, "Table");
        JTextField user = (JTextField) UIUtils.getChildNamed(cf, "User");
        JTextField password = (JTextField) UIUtils.getChildNamed(cf, "Password");
        JSpinner port = (JSpinner) UIUtils.getChildNamed(cf, "Port");

        assertNotNull(host);
        assertNotNull(table);
        assertNotNull(user);
        assertNotNull(password);
        assertNotNull(port);

        assertEquals(host.isEditable(), true);
        assertEquals(table.isEditable(), true);
        assertEquals(user.isEditable(), true);
        assertEquals(password.isEditable(), true);
        assertEquals(port.isEnabled(), true);

        host.setText("localhost");
        table.setText("test");
        user.setText("");
        password.setText("root");
        port.setValue(this.db.getConfiguration().getPort());

        JButton go = (JButton) UIUtils.getChildNamed(cf, "letsgo");
        JButton test = (JButton) UIUtils.getChildNamed(cf, "clearOrtest");

        assertNotNull(go);
        assertNotNull(test);

        test.doClick();

        JLabel status = (JLabel) UIUtils.getChildNamed(cf, "status");

        assertEquals(status.getText(), "Database Connected");
    }

    /**
     * Checking if valid input will stop further actions
     */
    @Test
    public void invalidMysqlDb() {
        JRadioButton mysql = (JRadioButton) UIUtils.getChildNamed(cf, "mysql");

        assertNotNull(mysql);

        mysql.doClick();

        JTextField host = (JTextField) UIUtils.getChildNamed(cf, "Host");
        JTextField table = (JTextField) UIUtils.getChildNamed(cf, "Table");
        JTextField user = (JTextField) UIUtils.getChildNamed(cf, "User");
        JTextField password = (JTextField) UIUtils.getChildNamed(cf, "Password");
        JSpinner port = (JSpinner) UIUtils.getChildNamed(cf, "Port");

        assertNotNull(host);
        assertNotNull(table);
        assertNotNull(user);
        assertNotNull(password);
        assertNotNull(port);

        assertEquals(host.isEditable(), true);
        assertEquals(table.isEditable(), true);
        assertEquals(user.isEditable(), true);
        assertEquals(password.isEditable(), true);
        assertEquals(port.isEnabled(), true);

        host.setText("localhost");
        table.setText("adv");
        user.setText("asdfasdf");
        password.setText("root");
        port.setValue(this.db.getConfiguration().getPort());

        JButton go = (JButton) UIUtils.getChildNamed(cf, "letsgo");
        JButton test = (JButton) UIUtils.getChildNamed(cf, "clearOrtest");

        assertNotNull(go);
        assertNotNull(test);

        test.doClick();

        JLabel status = (JLabel) UIUtils.getChildNamed(cf, "status");

        assertEquals(status.getText(), "<html><font color='red'>Testing connection to database failed</font></html>");
    }
}
