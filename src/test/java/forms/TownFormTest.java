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
import adressverwaltung.forms.TownForm;
import adressverwaltung.models.Person;
import adressverwaltung.models.Town;
import adressverwaltung.utils.InOut;
import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import com.github.javafaker.Faker;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chris
 */
public class TownFormTest {

    TownForm tf;
    InOut io;
    DB db;
    Faker faker;
    Person p;
    Town t1;
    Town t2;

    /**
     * Empty Constructor
     */
    public TownFormTest() {

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
            throw new Error("Could not create database on port " + db.getConfiguration().getPort());
        }

        File f;
        if ((f = new File(SystemPropertyEnum.USER_HOME + "" + SystemPropertyEnum.FILE_SEPERATOR + ".env")).exists()) {
            f.renameTo(new File(SystemPropertyEnum.USER_HOME + "" + SystemPropertyEnum.FILE_SEPERATOR + ".env.original"));
        }

        faker = new Faker(new Locale("de-CH"));

        t1 = new Town(4800, "Zofingen");
        t2 = new Town(3212, "Gurmels");
        try {
            long t1id = io.saveTown(t1);
            long t2id = io.saveTown(t2);
            t1.setTid(t1id);
            t2.setTid(t2id);
            p = new Person(faker.name().lastName(), faker.name().firstName(), faker.address().streetAddress(), t1id, faker.phoneNumber().phoneNumber(), "0799069867", faker.internet().emailAddress());
            long pid = io.savePerson(p);
            p.setId(pid);
        } catch (SQLException ex) {
            throw new Exception("Couldnt save town");
        }

        try {
            tf = new TownForm(io);
        } catch (SQLException | CanNotConnectToDatabaseError ex) {
            throw new Error("Could not connect to database");
        }

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
     * JButton newOne = (JButton)UIUtils.getChildNamed(af, "newOne"); JButton
     * save = (JButton)UIUtils.getChildNamed(af, "save"); JButton delete =
     * (JButton)UIUtils.getChildNamed(af, "delete"); JButton search =
     * (JButton)UIUtils.getChildNamed(af, "search"); JButton previous =
     * (JButton)UIUtils.getChildNamed(af, "previous"); JButton next =
     * (JButton)UIUtils.getChildNamed(af, "next");
     *
     * JTextFiled plz = (JTextField)UIUtils.getChildNamed(af, "plz"); JTextFiled
     * name = (JTextField)UIUtils.getChildNamed(af, "name"); JLabel state =
     * (JLabel)UIUtils.getChildNamed(af, "state");
     */

    @Test
    public void createNew() {
        JButton newOne = (JButton) UIUtils.getChildNamed(tf, "newTown");
        JTextField plz = (JTextField) UIUtils.getChildNamed(tf, "townPLZ");
        JTextField name = (JTextField) UIUtils.getChildNamed(tf, "townName");
        JButton save = (JButton) UIUtils.getChildNamed(tf, "saveTown");

        assertNotNull(newOne);
        assertNotNull(plz);
        assertNotNull(name);
        assertNotNull(save);

        newOne.doClick();

        assertEquals(plz.getText(), "");
        assertEquals(name.getText(), "");

        plz.setText(faker.address().zipCode());
        name.setText(faker.address().cityName());

        save.doClick();

        assertEquals(io.countTowns(), 3);
    }

    /**
     * Creating duplicated Town
     */
    @Test
    public void createDuplicate() {
        JButton newOne = (JButton) UIUtils.getChildNamed(tf, "new");
        JTextField plz = (JTextField) UIUtils.getChildNamed(tf, "plz");
        JTextField name = (JTextField) UIUtils.getChildNamed(tf, "name");
        JButton save = (JButton) UIUtils.getChildNamed(tf, "save");

        assertNotNull(newOne);
        assertNotNull(plz);
        assertNotNull(name);
        assertNotNull(save);

        String nameStr = name.getText();
        String plzStr = plz.getText();

        newOne.doClick();

        plz.setText(plzStr);
        name.setText(nameStr);

        save.doClick();

        assertEquals(io.countTowns(), 2);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void delete() throws Exception {
        try {
            io.deletePerson(p);
        } catch (SQLException ex) {
            throw new Exception("Could not delete Person");
        }

        JButton delete = (JButton) UIUtils.getChildNamed(tf, "delete");

        assertNotNull(delete);

        delete.doClick();

        assertEquals(io.countTowns(), 1);
    }

    /**
     * Naviagting through ui
     */
    @Test
    public void navigate() {
        JButton previous = (JButton) UIUtils.getChildNamed(tf, "previous");
        JButton next = (JButton) UIUtils.getChildNamed(tf, "next");
        JLabel state = (JLabel) UIUtils.getChildNamed(tf, "state");

        assertNotNull(next);
        assertNotNull(previous);
        assertNotNull(state);

        next.doClick();

        assertEquals(state.getText(), "2/2");

        previous.doClick();

        assertEquals(state.getText(), "1/2");
    }

    /**
     * Try to delete town with reference
     */
    @Test
    public void deleteWithReference() {
        JButton delete = (JButton) UIUtils.getChildNamed(tf, "delete");

        assertNotNull(delete);

        delete.doClick();

        assertEquals(io.countTowns(), 2);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void update() throws Exception {
        JButton delete = (JButton) UIUtils.getChildNamed(tf, "delete");

        assertNotNull(delete);

        try {
            io.deletePerson(p);
        } catch (SQLException ex) {
            throw new Exception("Could not delete person");
        }

        delete.doClick();

        Town t = io.getTowns().get(0);

        t.setName(faker.address().cityName());
        t.setPlz(3212);

        JTextField plz = (JTextField) UIUtils.getChildNamed(tf, "plz");
        JTextField name = (JTextField) UIUtils.getChildNamed(tf, "name");

        assertNotNull(plz);
        assertNotNull(name);

        plz.setText(t.getPlz() + "");
        name.setText(plz.getText());

        JButton save = (JButton) UIUtils.getChildNamed(tf, "save");

        assertNotNull(save);

        save.doClick();

        Town result = io.getTowns().get(0);

        assertEquals(t, result);
    }

    /**
     * Checking on input validation
     */
    @Test
    public void validationCheck() {
        JTextField plz = (JTextField) UIUtils.getChildNamed(tf, "plz");
        JTextField name = (JTextField) UIUtils.getChildNamed(tf, "name");

        plz.setText("0");
        name.setText("");

        assertEquals(plz.getForeground(), Color.red);
        assertEquals(name.getForeground(), Color.red);

        plz.setText("10000");

        assertEquals(plz.getForeground(), Color.red);

        plz.setText("3212");
        name.setText("Gurmels");

        assertEquals(plz.getForeground(), Color.black);
        assertEquals(name.getForeground(), Color.black);
    }
}
