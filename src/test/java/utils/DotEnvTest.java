/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import adressverwaltung.enums.DotEnvEnum;
import adressverwaltung.enums.SystemPropertyEnum;
import adressverwaltung.utils.DotEnv;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Christof Weickhardt
 */
public class DotEnvTest {

    DotEnv dotEnv;

    /**
     * Empty Constructor
     */
    public DotEnvTest() {

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
     */
    @Before
    public void setUp() {
        File f;
        if ((f = new File(SystemPropertyEnum.USER_HOME + "" + SystemPropertyEnum.FILE_SEPERATOR + ".env")).exists()) {
            f.renameTo(new File(SystemPropertyEnum.USER_HOME + "" + SystemPropertyEnum.FILE_SEPERATOR + ".env.original"));
        }
    }

    /**
     * Tear down after tests
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
     * Checking on operations with .evn
     * @throws IOException
     * @throws Exception
     */
    @Test
    public void operateWithDotEnv() throws IOException, Exception {
        HashMap<String, String> test = new HashMap<>();
        test.put(DotEnvEnum.DB_USE.get(), "true");
        test.put(DotEnvEnum.HOST.get(), "localhost");
        test.put(DotEnvEnum.PORT.get(), "3306");
        test.put(DotEnvEnum.TABLE_NAME.get(), "adressverwaltung_test");
        test.put(DotEnvEnum.PASSWORD.get(), "root1234");
        test.put(DotEnvEnum.USER.get(), "root");

        if (!DotEnv.containsAllKeys(test)) {
            throw new Exception("Test does not contain all the needed . env keys");
        }

        DotEnv.setDotEnv(test);

        HashMap<String, String> results = DotEnv.getDotEnv();

        assertNotNull(results);

        if (!DotEnv.containsAllKeys(results)) {
            throw new Exception("Did not receive all needed keys of .env");
        }

        assertEquals(results.get(DotEnvEnum.DB_USE.get()), test.get(DotEnvEnum.DB_USE.get()));
        assertEquals(results.get(DotEnvEnum.HOST.get()), test.get(DotEnvEnum.HOST.get()));
        assertEquals(results.get(DotEnvEnum.PASSWORD.get()), test.get(DotEnvEnum.PASSWORD.get()));
        assertEquals(results.get(DotEnvEnum.PORT.get()), test.get(DotEnvEnum.PORT.get()));
        assertEquals(results.get(DotEnvEnum.USER.get()), test.get(DotEnvEnum.USER.get()));
        assertEquals(results.get(DotEnvEnum.TABLE_NAME.get()), test.get(DotEnvEnum.TABLE_NAME.get()));
    }
}
