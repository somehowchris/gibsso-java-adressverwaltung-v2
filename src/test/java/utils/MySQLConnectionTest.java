/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import adressverwaltung.errors.CanNotConnectToDatabaseError;
import adressverwaltung.utils.MySQLConnection;
import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Christof Weickhardt
 */
public class MySQLConnectionTest {

    DB db;

    /**
     * Empty Constructor
     */
    public MySQLConnectionTest() {

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
    public void setUp() throws SQLException {
        try {
            db = DB.newEmbeddedDB(3308);
            db.start();
        } catch (ManagedProcessException ex) {
            throw new Error("Could not create database on port 3308");
        }
    }

    /**
     * Tear down after test
     */
    @After
    public void tearDown() {
        try {
            db.stop();
        } catch (ManagedProcessException ex) {
        }
    }

    /**
     * Checking on database availability
     * @throws SQLException
     */
    @Test
    public void verifyConnection() throws SQLException {
        try {
            MySQLConnection mysql = new MySQLConnection("localhost", "root", "test", "3308", "", true, "mysql");
            mysql.verify();
        } catch (CanNotConnectToDatabaseError ex) {
            Logger.getLogger(MySQLConnectionTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
