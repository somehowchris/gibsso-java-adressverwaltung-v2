/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung.utils;

import adressverwaltung.models.Person;

import java.util.*;
import java.io.File;
import adressverwaltung.services.DatabaseService;
import adressverwaltung.services.FileSystemService;
import adressverwaltung.errors.CanNotConnectToDatabaseError;
import adressverwaltung.models.Town;
import adressverwaltung.enums.DotEnvEnum;
import adressverwaltung.enums.SystemPropertyEnum;
import adressverwaltung.errors.DatabaseSelfHealingError;
import adressverwaltung.errors.WrongSchemaError;
import java.sql.SQLException;
import adressverwaltung.services.Service;

/**
 * A middleware class to operate with the Service interface
 *
 * @author Nicola Temporal
 */
public class InOut {

    /**
     * Service connection which will be used to get data out of databases and
     * the file sytem
     */
    public Service connection = null;

    /**
     * Local home of the user data
     */
    String home = SystemPropertyEnum.USER_HOME.get();

    /**
     * Prepares the connnection.&nbsp;Checks on all the variables to get the
     * best connection
     *
     * @param dotEnv List of preset values to get the chosen connection type
     * @throws SQLException If connected to the database without the needed
     * permissions
     * @throws CanNotConnectToDatabaseError If all the values lead to a database
     * which can not be accessed via the given inputs
     * @throws adressverwaltung.errors.DatabaseSelfHealingError if not possible to reconnect to healed database
     */
    public InOut(HashMap<String, String> dotEnv) throws SQLException, CanNotConnectToDatabaseError, DatabaseSelfHealingError {
        if (dotEnv == null) {
            dotEnv = new HashMap<>();
        }
        File dotEnvFile = new File(home);
        if (dotEnvFile.exists() && dotEnv.isEmpty() ? !(dotEnv = DotEnv.getDotEnv()).isEmpty() : true) {
            if (dotEnv.containsKey(DotEnvEnum.DB_USE.get()) && DotEnv.containsAllKeys(dotEnv)) {
                switch (dotEnv.get(DotEnvEnum.DB_USE.get())) {
                    case "true":
                        try {
                            MySQLConnection con = new MySQLConnection(dotEnv.get(DotEnvEnum.HOST.get()), dotEnv.get(DotEnvEnum.PASSWORD.get()), dotEnv.get(DotEnvEnum.TABLE_NAME.get()), dotEnv.get(DotEnvEnum.PORT.get()), dotEnv.get(DotEnvEnum.USER.get()), true, "mysql");
                            if(con.verify()){
                                connection = new DatabaseService(dotEnv);
                                break;
                            }
                            throw new CanNotConnectToDatabaseError();
                        } catch (WrongSchemaError ex) {

                        }
                    case "false":
                        connection = new FileSystemService(home, SystemPropertyEnum.FILE_SEPERATOR.get());
                        break;
                    default:
                        throw new CanNotConnectToDatabaseError();
                }
            } else {
                connection = new FileSystemService(home, SystemPropertyEnum.FILE_SEPERATOR.get());
            }
        } else {
            connection = new FileSystemService(home, SystemPropertyEnum.FILE_SEPERATOR.get());
        }
    }

    /**
     * Search for a Person in your data set
     *
     * @param vorname First name of the person to search
     * @param name Last name of the person to search
     * @return Returns a list of people which
     * @throws SQLException If not able to read data from database
     */
    public List<Person> searchPerson(String vorname, String name) throws SQLException {
        return connection.searchPerson(new Person(vorname, name));
    }

    /**
     * Save a person.&nbsp; no matterr if creating or updating
     *
     * @param p Person to save
     * @return Return the id of the person
     * @throws SQLException If not able to write changes
     */
    public long savePerson(Person p) throws SQLException {
        if (p.getId() != null) {
            return connection.updatePerson(p);
        } else {
            return connection.insertPerson(p);
        }
    }

    /**
     * Delete a Person
     *
     * @param p Person to delete
     * @throws SQLException If not able to write changes
     */
    public void deletePerson(Person p) throws SQLException {
        connection.deletePerson(p);
    }

    /**
     * Get a list of people
     *
     * @param amount Amount of people to be returned
     * @param offset Offset of which to count people
     * @return Returns a list of people
     */
    public List<Person> getPeople(int amount, int offset) {
        return connection.getPeople(amount, offset);
    }

    /**
     * Get a signle person
     *
     * @param id ID of the person
     * @return Return the requested person if it exists
     */
    public Person getPerson(Long id) {
        return connection.getPerson(id);
    }

    /**
     * Counts all the pople in your data set
     *
     * @return Amount of people in data set
     */
    public long countPeople() {
        return connection.countPeople();
    }

    /**
     * Search for a Town
     *
     * @param plz PLZ of the town to search
     * @param name Name of the town to search
     * @return Returns a list of towns
     * @throws SQLException If not able to access the database
     */
    public List<Town> searchTown(int plz, String name) throws SQLException {
        return connection.searchTown(new Town(plz, name));
    }

    /**
     * Save a town.&nbsp; No matter if updating or creating
     *
     * @param o Town to save
     * @return Returns the id of the town
     * @throws SQLException If not able to write changes
     */
    public long saveTown(Town o) throws SQLException {
        if (o.getTid() != null) {
            return connection.updateTown(o);
        } else {
            return connection.insertTown(o);
        }
    }

    /**
     * Delete a town
     *
     * @param o Town to delete
     * @throws SQLException if not able to write changes
     */
    public void deleteTown(Town o) throws SQLException {
        connection.deleteTown(o);
    }

    /**
     * Get all towns
     *
     * @return Returns a list of towns
     */
    public List<Town> getTowns() {
        return connection.getTown();
    }

    /**
     * Gets a specific range of towns
     *
     * @param amount Amount of towns to get
     * @param offset Offset of wich to count on from
     * @return Returns a list of towns
     */
    public List<Town> getTowns(int amount, int offset) {
        return connection.getTown(amount, offset);
    }

    /**
     * Get a single town
     *
     * @param id ID of town to get
     * @return Returns to requested town
     */
    public Town getTown(Long id) {
        return connection.getTown(id);
    }

    /**
     * Counts all the towns in your data set
     *
     * @return Amount of towns in data set
     */
    public long countTowns() {
        return connection.countTown();
    }

    /**
     * Creates export of all the people and if possible towns
     *
     * @throws Exception If not possible to write the export to the chosen
     * location
     */  
}
