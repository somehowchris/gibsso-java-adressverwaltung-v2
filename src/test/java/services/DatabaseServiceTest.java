/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import adressverwaltung.enums.DotEnvEnum;
import adressverwaltung.errors.CanNotConnectToDatabaseError;
import adressverwaltung.errors.WrongSchemaError;
import adressverwaltung.models.Person;
import adressverwaltung.models.Town;
import adressverwaltung.services.DatabaseService;
import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 *
 * @author Christof Weickhardt
 */
public class DatabaseServiceTest {

    DB db;
    DatabaseService dbService;

    /**
     * Empty Constructor
     */
    public DatabaseServiceTest() {

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
     * @throws java.lang.Exception
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
            dbService = new DatabaseService(fakeKeys);
        } catch (ManagedProcessException ex) {
            throw new Exception("Could not create database on port 3308");
        } catch (WrongSchemaError | CanNotConnectToDatabaseError ex) {
            Logger.getLogger(DatabaseServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Teadown after tests
     */
    @After
    public void tearDown() {
        try {
            db.stop();
        } catch (ManagedProcessException ex) {
        }
    }

    /**
     * Deleting Person
     * @throws Exception
     */
    @Test
    public void deletePerson() throws Exception {
        Person p = new Person("Larray", "Hausmann");
        long count = dbService.countPeople();
        Long id;
        id = dbService.insertPerson(p);
        assertNotNull(id);
        dbService.countPeople();
        assertEquals(count + 1L, (long) dbService.countPeople());

        dbService.deletePerson(p);

        assertEquals(count, (long) dbService.countPeople());
    }

    /**
     * Creating Person
     * @throws Exception
     */
    @Test
    public void createPerson() throws Exception {
        Person p = new Person("Meier", "Hein");
        Long id;
        id = dbService.insertPerson(p);
        Person result = dbService.getPerson(id);

        p.setId(id);

        assertEquals(result, p);
    }

    /**
     * Deleting Town
     * @throws Exception
     */
    @Test
    public void deleteTown() throws Exception {
        Town t = new Town(4800, "Zofingen");
        long count = dbService.countTown();
        Long id;
        id = dbService.insertTown(t);

        assertNotNull(id);

        assertEquals(count + 1, (long) dbService.countTown());

        dbService.deleteTown(t);

        assertEquals(count, (long) dbService.countTown());
    }

    /**
     * Creating Town
     * @throws Exception
     */
    @Test
    public void createTown() throws Exception {
        Town t = new Town(3212, "Gurmels");
        Long id;
        id = dbService.insertTown(t);
        Town result = dbService.getTown(id);

        t.setTid(id);

        assertEquals(result, t);
    }

    /**
     * Updating Person
     * @throws Exception
     */
    @Test
    public void updatePerson() throws Exception {
        Person p = new Person("Meier", "Hein");
        Long id;
        id = dbService.insertPerson(p);
        p = dbService.getPerson(id);

        p.setFirstName("NewBie");
        p.setEmail("none@business.yours");

        dbService.updatePerson(p);

        Person result = dbService.getPerson(id);

        assertEquals(result, p);
    }

    /**
     * Updating Town
     * @throws Exception
     */
    @Test
    public void updateTown() throws Exception {
        Town t = new Town(3212, "Gurmels");
        Long id;
        id = dbService.insertTown(t);
        t = dbService.getTown(id);

        t.setPlz(4800);
        t.setName("Zofingen");

        dbService.updateTown(t);

        Town result = dbService.getTown(id);

        assertEquals(result, t);
    }
    
    /**
     * Searching Person
     * @throws Exception
     */
    @Test
    public void searchPerson() throws Exception {
        if (dbService.countPeople() > 0) {
            dbService.getPeople(new Integer(dbService.countPeople() + ""), 0).forEach((p) -> {
                dbService.deletePerson(p);
            });
        }

        Person p1 = new Person("FirstNameA", "LastNameA");
        Person p2 = new Person("FirstNameB", "LastNameB");
        Person p3 = new Person("FirstNameC", "LastNameC");

        dbService.insertPerson(p1);
        dbService.insertPerson(p2);
        dbService.insertPerson(p3);

        List<Person> search = dbService.searchPerson(new Person("FirstName", "LastName"));

        assertEquals(search.size(), 3);

        search = dbService.searchPerson(p2);

        assertEquals(search.size(), 1);

        search = dbService.searchPerson(new Person("NotInTheDataBase", ""));

        assertEquals(search.size(), 0);
    }

    /**
     * Searching Town
     * @throws Exception
     */
    @Test
    public void searchTown() throws Exception {
        if (dbService.countTown() > 0) {
            dbService.getTown(new Integer(dbService.countTown() + ""), 0).forEach((t) -> {
                dbService.deleteTown(t);
            });
        }

        Town t1 = new Town(3212, "Gurmels");
        Town t2 = new Town(3212, "Kleingurmels");
        Town t3 = new Town(4800, "Zofingen");

        dbService.insertTown(t1);
        dbService.insertTown(t2);
        dbService.insertTown(t3);

        List<Town> searchTown = dbService.searchTown(t1);

        assertEquals(searchTown.size(), 2);

        searchTown = dbService.searchTown(t3);

        assertEquals(searchTown.size(), 1);
    }

    /**
     * Counting Towns
     * @throws Exception
     */
    @Test
    public void countTown() throws Exception {
        long init = dbService.countTown();
        long added = 0;
        int rnd = ThreadLocalRandom.current().nextInt(0, 100 + 1);
        for (int i = 0; i < rnd; i++) {
            Town t = new Town(3212, "PopulatedCity");
            dbService.insertTown(t);
            added++;
        }
        assertEquals((long) dbService.countTown(), init + added);

        long removed = 0;

        for (int i = 0; i < ThreadLocalRandom.current().nextInt(0, rnd + 1); i++) {
            Town t = dbService.getTown(1, 0).get(0);
            dbService.deleteTown(t);
            removed++;
        }

        assertEquals((long) dbService.countTown(), init + added - removed);
    }

    /**
     * Counting People
     * @throws Exception
     */
    @Test
    public void countPerson() throws Exception {
        long init = dbService.countPeople();
        long added = 0;
        int rnd = ThreadLocalRandom.current().nextInt(0, 100 + 1);
        for (int i = 0; i < rnd; i++) {
            Person p = new Person("Too", "Many");
            dbService.insertPerson(p);
            added++;
        }
        assertEquals((long) dbService.countPeople(), init + added);

        long removed = 0;

        for (int i = 0; i < ThreadLocalRandom.current().nextInt(0, rnd + 1); i++) {
            Person p = dbService.getPeople(1, 0).get(0);
            dbService.deletePerson(p);
            removed++;
        }

        assertEquals((long) dbService.countPeople(), init + added - removed);
    }

}
