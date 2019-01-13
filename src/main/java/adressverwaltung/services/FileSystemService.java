/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung.services;

import adressverwaltung.enums.SystemPropertyEnum;
import adressverwaltung.models.Town;
import adressverwaltung.models.Person;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A File System Service managing the data set in a .fsdb folder of the users
 * home
 *
 * @author Christof Weickhardt
 */
public class FileSystemService implements Service {

    String fsdbdir;

    HashMap<Long, Person> redismapPeople = new HashMap<>();
    HashMap<Long, Town> redismapTown = new HashMap<>();

    /**
     * Constructor to open a FileSystemService
     *
     * @param dir Directory of the data set
     * @param sep Platform depending seperator of uris
     */
    public FileSystemService(String dir, String sep) {
        if (dir.split("")[dir.split("").length - 1] == null ? sep != null : !dir.split("")[dir.split("").length - 1].equals(sep)) {
            dir += sep;
        }
        if (!new File(dir + ".fsdb" + sep).exists()) {
            new File(dir + ".fsdb" + sep).mkdir();
        }
        fsdbdir = dir + ".fsdb" + sep;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Person getPerson(Long id) {
        if (redismapPeople.containsKey(id)) {
            return redismapPeople.get(id);
        }
        File f = new File(fsdbdir + id + ".person");
        if (f.exists()) {
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(f));
                String line;
                int i = 0;
                Person p = new Person();
                while ((line = reader.readLine()) != null) {
                    switch (i) {
                        case 0:
                            p.setPid(new Long(line));
                            break;
                        case 1:
                            p.setLastName(line);
                            break;
                        case 2:
                            p.setFirstName(line);
                            break;
                        case 3:
                            p.setAddress(line);
                            break;
                        case 4:
                            p.setOid(new Integer(line));
                        case 5:
                            p.setPhone(line);
                            break;
                        case 6:
                            p.setMobile(line);
                            break;
                        case 7:
                            p.setEmail(line);
                            break;
                    }
                    i++;
                }
                reader.close();
                redismapPeople.put(id, p);
                return p;
            } catch (IOException e) {
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Town getTown(Long id) {
        if (redismapTown.containsKey(id)) {
            return redismapTown.get(id);
        }
        File f = new File(fsdbdir + id + ".town");
        if (f.exists()) {
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(f));
                String line;
                int i = 0;
                Town o = new Town();
                while ((line = reader.readLine()) != null) {
                    switch (i) {
                        case 0:
                            o.setTid(new Long(line));
                            break;
                        case 1:
                            o.setName(line);
                            break;
                        case 2:
                            o.setPlz(new Integer(line));
                            break;
                    }
                    i++;
                }
                reader.close();
                redismapTown.put(id, o);
                return o;
            } catch (IOException e) {
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Person> searchPerson(Person person) {
        String[] files = searchInDir(".person");
        ArrayList<Person> people = new ArrayList<>();
        for (String file : files) {
            Person p = getPerson(new Long(file.replace(".person", "")));
            if (p.getLastName().toLowerCase().contains(person.getLastName().toLowerCase()) && p.getFirstName().toLowerCase().contains(person.getFirstName().toLowerCase())) {
                people.add(p);
            }
        }
        return people;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Town> searchTown(Town town) {
        String[] files = searchInDir(".town");
        ArrayList<Town> townlist = new ArrayList<>();
        for (String file : files) {
            Town o = getTown(new Long(file.replace(".town", "")));
            System.out.println();
            if (o.getName().toLowerCase().contains(town.getName().toLowerCase())) {
                townlist.add(o);
            }
        }
        return townlist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long insertPerson(Person person) {
        Long id = 0L;
        if (getPeople(1, new Integer(countPeople() + "") - 2).size() > 0) {
            id = getPeople(1, new Integer(countPeople() + "") - 2).size() + 1L;
        }
        while (new File(fsdbdir + id + ".person").exists()) {
            id++;
        }
        person.setId(id);
        updatePerson(person);
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long insertTown(Town town) {
        Long id = 0L;
        if (getTown(1, new Integer(countTown() + "") - 2).size() > 0) {
            id = getTown(1, new Integer(countTown() + "") - 2).get(0).getTid() + 1L;
        }
        while (new File(fsdbdir + id + ".town").exists()) {
            id++;
        }
        town.setTid(id);
        updateTown(town);
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long updatePerson(Person person) {
        String linesep = SystemPropertyEnum.LINE_SEPERATOR.get();
        String data = person.getId() + linesep + person.getLastName() + linesep + person.getFirstName() + linesep
                + person.getAddress() + linesep + person.getOid() + linesep + person.getPhone() + linesep
                + person.getMobile() + linesep + person.getEmail();
        String file = fsdbdir + person.getId() + ".person";
        writeData(data, file);
        redismapPeople.put(person.getId(), person);
        return person.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long updateTown(Town town) {
        String data = town.getTid() + SystemPropertyEnum.LINE_SEPERATOR.get() + town.getName()
                + SystemPropertyEnum.LINE_SEPERATOR.get() + town.getPlz();
        String file = fsdbdir + town.getTid() + ".town";
        writeData(data, file);
        redismapTown.put(town.getTid(), town);
        return town.getTid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePerson(Person person) {
        File f = new File(fsdbdir + person.getId() + ".person");
        if (f.exists()) {
            f.delete();
            redismapPeople.remove(person.getId());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTown(Town town) {
        File f = new File(fsdbdir + town.getTid() + ".town");
        long references = getPeople(new Integer(countPeople() + ""), 0).stream().filter(el -> {
            return el.getOid().equals(town.getTid());
        }).count();
        if (f.exists() && references == 0) {
            f.delete();
            redismapTown.remove(town.getTid());
        } else {
            throw new Error("References on this place still exist " + town.getTid());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Person> getPeople(int amount, int offset) {
        String[] consideredFiles = searchInDir(".person");
        ArrayList<Person> townlist = new ArrayList<>();
        try {
            for (int i = offset; i < offset + amount; i++) {
                if (consideredFiles.length > i && i >= 0) {
                    townlist.add(getPerson(new Long(consideredFiles[i].replace(".person", ""))));

                }
            }
        } catch (NumberFormatException e) {
        }
        return townlist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Town> getTown(int amount, int offset) {
        String[] consideredFiles = searchInDir(".town");

        ArrayList<Town> townlist = new ArrayList<>();
        try {
            for (int i = offset; i < offset + amount; i++) {
                if (consideredFiles.length > i && i >= 0) {
                    townlist.add(getTown(new Long(consideredFiles[i].replace(".town", ""))));
                }
            }
        } catch (NumberFormatException e) {
        }
        return townlist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Town> getTown() {
        int offset = 0;
        int amount = new Integer(countTown() + "");
        String[] consideredFiles = searchInDir(".town");

        ArrayList<Town> townlist = new ArrayList<>();
        try {
            for (int i = offset; i < offset + amount; i++) {
                if (consideredFiles.length >= i) {
                    townlist.add(getTown(new Long(consideredFiles[i].replace(".town", ""))));
                }
            }
        } catch (NumberFormatException e) {
        }
        return townlist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countPeople() {
        Long total = new Long(searchInDir(".person").length);
        return total;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countTown() {
        Long total = new Long(searchInDir(".town").length);
        return total;
    }

    /**
     * Searches for files wich contain a spefic character chain
     *
     * @param contains String to be contained in the found file names of the
     * home directory
     * @return All files found and passed the contains check
     * @see FilenameFilter
     */
    private String[] searchInDir(String contains) {
        return new File(fsdbdir).list((File dir, String name) -> name.contains(contains));
    }

    /**
     * Writes data to the file system
     *
     * @param data Data to write
     * @param file File path to store on
     * @see BufferedWriter
     * @see FileWriter
     */
    private void writeData(String data, String file) {
        BufferedWriter br = null;
        FileWriter fr = null;
        String dataWithNewLine = data + SystemPropertyEnum.LINE_SEPERATOR.get();
        try {
            fr = new FileWriter(file);
            br = new BufferedWriter(fr);
            br.write(dataWithNewLine);
        } catch (IOException e) {
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * Cleans all the local data on the file system related to this application
     *
     * @see BufferedWriter
     * @see FileWriter
     */
    public void clean() {
        String[] townlist = searchInDir(".town");
        String[] people = searchInDir(".person");
        for (String pstrg : people) {
            Person p = new Person();
            p.setId(new Long(pstrg.replace(".person", "")));
            deletePerson(p);
        }

        for (String ostrg : townlist) {
            Town o = new Town();
            o.setTid(new Long(ostrg.replace(".town", "")));
            deleteTown(o);
        }
    }
}
