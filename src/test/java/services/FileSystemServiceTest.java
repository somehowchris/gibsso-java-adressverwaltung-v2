/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import adressverwaltung.enums.SystemPropertyEnum;
import adressverwaltung.models.Person;
import adressverwaltung.models.Town;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import adressverwaltung.services.FileSystemService;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author chris
 */
public class FileSystemServiceTest {

    FileSystemService dbService;
    static int count = 0;
    String path = "";

    /**
     * Empty Constructor
     */
    public FileSystemServiceTest() {

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
        try {
            File f = new File(SystemPropertyEnum.USER_DIR.get() + SystemPropertyEnum.FILE_SEPERATOR.get() + ".test" + count);
            if (f.exists() && f.isDirectory()) {
                FileUtils.deleteDirectory(new File(path));
            }
            f.mkdir();
            path = f.getAbsolutePath();
            dbService = new FileSystemService(path, SystemPropertyEnum.FILE_SEPERATOR.get());
            count++;
        } catch (IOException ex) {
            Logger.getLogger(FileSystemServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Tear down after test
     */
    @After
    public void tearDown() {
        try {
            FileUtils.deleteDirectory(new File(path));
        } catch (IOException ex) {
            Logger.getLogger(FileSystemServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Deleting person
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
     * Creating new Person
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
     * Deleting town
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
     * Creating town
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
     * Searching for Person
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
     * Searching for Town
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
