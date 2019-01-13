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
import adressverwaltung.forms.AddressForm;
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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
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
public class AddressFormTest {

    AddressForm af;
    InOut io;
    DB db;
    Faker faker;
    Person p1;
    Person p2;
    long p1id;
    long p2id;

    /**
     * Empty Constructor
     */
    public AddressFormTest() {

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

        try {
            af = new AddressForm(io);
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
     * Creating new Town
     * @throws Exception
     */
    @Test
    public void createNew() throws Exception {
        Town t = new Town(4800, "Zofingen");
        try {
            io.saveTown(t);
        } catch (SQLException ex) {
            throw new Exception("Couldnt save town");
        }

        JTextField lastname = (JTextField) UIUtils.getChildNamed(af, "name");
        JTextField firstname = (JTextField) UIUtils.getChildNamed(af, "firstname");
        JTextField street = (JTextField) UIUtils.getChildNamed(af, "street");
        JTextField phone = (JTextField) UIUtils.getChildNamed(af, "phone");
        JTextField mobile = (JTextField) UIUtils.getChildNamed(af, "mobile");
        JTextField email = (JTextField) UIUtils.getChildNamed(af, "email");

        JButton newOne = (JButton) UIUtils.getChildNamed(af, "newOne");
        JButton save = (JButton) UIUtils.getChildNamed(af, "save");

        assertNotNull(newOne);
        assertNotNull(save);

        assertNotNull(lastname);
        assertNotNull(firstname);
        assertNotNull(street);
        assertNotNull(phone);
        assertNotNull(mobile);
        assertNotNull(email);

        newOne.doClick();

        assertEquals(lastname.getText(), "");
        assertEquals(firstname.getText(), "");
        assertEquals(street.getText(), "");
        assertEquals(phone.getText(), "");
        assertEquals(mobile.getText(), "");
        assertEquals(email.getText(), "");

        lastname.setText(faker.name().lastName());
        firstname.setText(faker.name().firstName());
        street.setText(faker.address().streetAddress());
        phone.setText("0266753737");
        mobile.setText("0799069867");
        email.setText(faker.internet().emailAddress());

        save.doClick();

        assertEquals(io.countPeople(), 3);
    }

    /**
     * Testing update function in ui
     * @throws Exception
     */
    @Test
    public void update() throws Exception {
        JTextField lastname = (JTextField) UIUtils.getChildNamed(af, "name");
        JTextField firstname = (JTextField) UIUtils.getChildNamed(af, "firstname");
        JTextField street = (JTextField) UIUtils.getChildNamed(af, "street");

        JButton newOne = (JButton) UIUtils.getChildNamed(af, "newOne");
        JButton save = (JButton) UIUtils.getChildNamed(af, "save");

        assertNotNull(save);

        assertNotNull(lastname);
        assertNotNull(firstname);
        assertNotNull(street);

        p1.setFirstName(faker.name().firstName());
        p1.setLastName(faker.name().lastName());
        p1.setAddress(faker.address().streetAddress());
        lastname.setText(p1.getLastName());
        firstname.setText(p1.getFirstName());
        street.setText(p1.getAddress());

        save.doClick();

        assertEquals(io.countPeople(), 2);

        Person result = io.getPerson(p1id);

        p1.setId(p1id);

        assertEquals(result, result);

    }

    /**
     * Testing delet button in ui
     */
    @Test
    public void delete() {
        JButton delete = (JButton) UIUtils.getChildNamed(af, "delete");

        assertNotNull(delete);

        delete.doClick();

        assertEquals(1, io.countPeople());
    }

    /**
     * Navigating through the ui
     */
    @Test
    public void navigate() {
        JButton next = (JButton) UIUtils.getChildNamed(af, "next");
        JButton previous = (JButton) UIUtils.getChildNamed(af, "previous");

        JLabel state = (JLabel) UIUtils.getChildNamed(af, "controll");

        assertNotNull(next);
        assertNotNull(previous);
        assertNotNull(state);

        next.doClick();

        assertEquals("2/2", state.getText());

        previous.doClick();

        assertEquals("1/2", state.getText());
    }

    /**
     * Checking input falidation
     */
    @Test
    public void validationCheck() {
        JTextField lastname = (JTextField) UIUtils.getChildNamed(af, "name");
        JTextField firstname = (JTextField) UIUtils.getChildNamed(af, "firstname");
        JTextField street = (JTextField) UIUtils.getChildNamed(af, "street");
        JTextField phone = (JTextField) UIUtils.getChildNamed(af, "phone");
        JTextField mobile = (JTextField) UIUtils.getChildNamed(af, "mobile");
        JTextField email = (JTextField) UIUtils.getChildNamed(af, "email");
        JComboBox plz = (JComboBox) UIUtils.getChildNamed(af, "plz");
        JButton save = (JButton) UIUtils.getChildNamed(af, "save");

        assertNotNull(lastname);
        assertNotNull(firstname);
        assertNotNull(street);
        assertNotNull(phone);
        assertNotNull(mobile);
        assertNotNull(email);
        assertNotNull(plz);
        assertNotNull(save);

        lastname.setText("");
        firstname.setText("");
        street.setText("");
        phone.setText("hello");
        mobile.setText("world");
        email.setText("none");
        plz.setSelectedIndex(0);

        assertEquals(lastname.getForeground(), Color.red);
        assertEquals(firstname.getForeground(), Color.red);
        assertEquals(street.getForeground(), Color.red);
        assertEquals(phone.getForeground(), Color.red);
        assertEquals(mobile.getForeground(), Color.red);
        assertEquals(email.getForeground(), Color.red);
        assertEquals(plz.getForeground(), Color.red);
        assertEquals(save.isEnabled(), false);
    }

    /**
     * Testing search ui
     * @throws Exception
     */
    @Test
    public void search() throws Exception {
        JTextField lastname = (JTextField) UIUtils.getChildNamed(af, "name");
        JTextField firstname = (JTextField) UIUtils.getChildNamed(af, "firstname");
        JButton search = (JButton) UIUtils.getChildNamed(af, "search");
        JLabel state = (JLabel) UIUtils.getChildNamed(af, "controll");
        JList list = (JList) UIUtils.getChildNamed(af, "list");

        assertNotNull(lastname);
        assertNotNull(firstname);
        assertNotNull(search);
        assertNotNull(state);
        assertNotNull(list);

        assertEquals(state.getText(), "1/2");
        Person p;
        try {
            p = io.searchPerson(firstname.getText(), lastname.getText()).get(0);
            assertNotNull(p);
        } catch (SQLException ex) {
            throw new Exception("Could not find person");
        }

        search.doClick();

        list.setSelectedIndex(0);

        assertEquals(state.getText(), "1/" + io.searchPerson(firstname.getText(), lastname.getText()).size());
        assertEquals(list.getSelectedValue(), p.getFirstName() + " " + p.getLastName());
    }

}
