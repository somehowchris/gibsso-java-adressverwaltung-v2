/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import adressverwaltung.enums.DotEnvEnum;
import adressverwaltung.models.Person;
import adressverwaltung.models.Town;
import adressverwaltung.utils.InOut;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
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
public class InOutTest {

    InOut io;

    /**
     * Empty Constructor
     */
    public InOutTest() {

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
        try {
            HashMap<String, String> fakeKeys = new HashMap<>();
            fakeKeys.put(DotEnvEnum.DB_USE.get(), "false");
            io = new InOut(fakeKeys);
        } catch (SQLException ex) {
            throw new Exception("Could not create read or write stream to file system");
        }
    }

    /**
     * Tear down after test
     */
    @After
    public void tearDown() {
    }

    /**
     * Deleting Person
     * @throws Exception
     */
    @Test
    public void deletePerson() throws Exception {
        Person p = new Person("Larray", "Hausmann");
        long count = io.countPeople();
        Long id;
        try {
            id = io.savePerson(p);
        } catch (SQLException ex) {
            throw new Exception("Could not save Person");
        }
        assertNotNull(id);

        assertEquals(count + 1, io.countPeople());

        io.deletePerson(p);

        assertEquals(count, io.countPeople());
    }

    /**
     * Deleting Town
     * @throws Exception
     */
    @Test
    public void deleteTown() throws Exception {
        Town t = new Town(4800, "Zofingen");
        long count = io.countTowns();
        Long id;
        try {
            id = io.saveTown(t);
        } catch (SQLException ex) {
            throw new Exception("Could not save Town");
        }

        assertNotNull(id);

        assertEquals(count + 1, io.countTowns());

        io.deleteTown(t);

        assertEquals(count, io.countTowns());
    }

    /**
     * Creating Person
     * @throws Exception
     */
    @Test
    public void createPerson() throws Exception {
        Person p = new Person("Meier", "Hein");
        Long id;
        try {
            id = io.savePerson(p);
        } catch (SQLException ex) {
            throw new Exception("Could not save Person");
        }
        Person result = io.getPerson(id);

        p.setId(id);

        assertEquals(result, p);
    }

    /**
     * Creating Towns
     * @throws Exception
     */
    @Test
    public void createTown() throws Exception {
        Town t = new Town(3212, "Gurmels");
        Long id;
        try {
            id = io.saveTown(t);
        } catch (SQLException ex) {
            throw new Exception("Could not save Town");
        }
        Town result = io.getTown(id);

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
        try {
            id = io.savePerson(p);
        } catch (SQLException ex) {
            throw new Exception("Could not save Person");
        }
        p = io.getPerson(id);

        p.setFirstName("NewBie");
        p.setEmail("none@business.yours");

        io.savePerson(p);

        Person result = io.getPerson(id);

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
        try {
            id = io.saveTown(t);
        } catch (SQLException ex) {
            throw new Exception("Could not save Person");
        }
        t = io.getTown(id);

        t.setPlz(4800);
        t.setName("Zofingen");

        io.saveTown(t);

        Town result = io.getTown(id);

        assertEquals(result, t);
    }

    /**
     * Searching Person
     * @throws Exception
     */
    @Test
    public void searchPerson() throws Exception {
        if (io.countPeople() > 0) {
            for (Person p : io.getPeople((int) io.countPeople(), 0)) {
                try {
                    io.deletePerson(p);
                } catch (SQLException ex) {
                    throw new Exception("Could not clean people database");
                }
            }
        }

        Person p1 = new Person("FirstNameA", "LastNameA");
        Person p2 = new Person("FirstNameB", "LastNameB");
        Person p3 = new Person("FirstNameC", "LastNameC");

        io.savePerson(p1);
        io.savePerson(p2);
        io.savePerson(p3);

        List<Person> search = io.searchPerson("FirstName", "LastName");

        assertEquals(search.size(), 3);

        search = io.searchPerson(p2.getFirstName(), p2.getLastName());

        assertEquals(search.size(), 1);

        search = io.searchPerson("NotInTheDataBase", "");

        assertEquals(search.size(), 0);
    }

    /**
     * Searching Town
     * @throws Exception
     */
    @Test
    public void searchTown() throws Exception {
        if (io.countTowns() > 0) {
            for (Town t : io.getTowns((int) io.countTowns(), 0)) {
                try {
                    io.deleteTown(t);
                } catch (SQLException ex) {
                    throw new Exception("Could not clean people database");
                }
            }
        }

        Town t1 = new Town(3212, "Gurmels");
        Town t2 = new Town(3212, "Kleingurmels");
        Town t3 = new Town(4800, "Zofingen");

        io.saveTown(t1);
        io.saveTown(t2);
        io.saveTown(t3);

        List<Town> searchTown = io.searchTown(3212, "gurmels");

        assertEquals(searchTown.size(), 2);

        searchTown = io.searchTown(0, "zofingen");

        assertEquals(searchTown.size(), 1);
    }

    /**
     * Counting Towns
     * @throws Exception
     */
    @Test
    public void countTown() throws Exception {
        long init = io.countTowns();
        long added = 0;
        int rnd = ThreadLocalRandom.current().nextInt(0, 100 + 1);
        for (int i = 0; i < rnd; i++) {
            Town t = new Town(3212, "PopulatedCity");
            try {
                io.saveTown(t);
                added++;
            } catch (SQLException ex) {
                throw new Exception("Could not save towns");
            }
        }
        assertEquals(io.countTowns(), init + added);

        long removed = 0;

        for (int i = 0; i < ThreadLocalRandom.current().nextInt(0, rnd + 1); i++) {
            Town t = io.getTowns(1, 0).get(0);
            try {
                io.deleteTown(t);
                removed++;
            } catch (SQLException ex) {
                throw new Exception("Could not remove towns");
            }
        }

        assertEquals(io.countTowns(), init + added - removed);
    }

    /**
     * Counting People
     * @throws Exception
     */
    @Test
    public void countPerson() throws Exception {
        long init = io.countPeople();
        long added = 0;
        int rnd = ThreadLocalRandom.current().nextInt(0, 100 + 1);
        for (int i = 0; i < rnd; i++) {
            Person p = new Person("Too", "Many");
            try {
                io.savePerson(p);
                added++;
            } catch (SQLException ex) {
                throw new Exception("Could not save people");
            }
        }
        assertEquals(io.countPeople(), init + added);

        long removed = 0;

        for (int i = 0; i < ThreadLocalRandom.current().nextInt(0, rnd + 1); i++) {
            Person p = io.getPeople(1, 0).get(0);
            try {
                io.deletePerson(p);
                removed++;
            } catch (SQLException ex) {
                throw new Exception("Could not remove towns");
            }
        }

        assertEquals(io.countPeople(), init + added - removed);
    }

}
