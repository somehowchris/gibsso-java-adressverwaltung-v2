/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung.services;

import adressverwaltung.models.Town;
import adressverwaltung.models.Person;
import java.util.List;

/**
 * The interface of a Service with all the needed function to operate with the
 * IO
 *
 * @author Christof Weickhardt
 */
public interface Service {

    /**
     * @param id A id of a person to get
     * @return The requested person
     */
    public Person getPerson(Long id);

    /**
     * @param id A id of a town to get
     * @return The requested town
     */
    public Town getTown(Long id);

    /**
     * @param person A person to search for in your data set
     * @return A list of people found by searching in your data set
     */
    public List<Person> searchPerson(Person person);

    /**
     * @param Town A town to search for in your data set
     * @return A list of towns found by searching in your data set
     */
    public List<Town> searchTown(Town Town);

    /**
     * @param person The person which is going to be inserted into your dataset
     * @return The id of the inerted person
     */
    public Long insertPerson(Person person);

    /**
     * @param Town The town which is going to be inserted into your dataset
     * @return The id of the inerted town
     */
    public Long insertTown(Town Town);

    /**
     * @param person The person which is going to be updated in your dataset
     * @return The id of the updated person
     */
    public Long updatePerson(Person person);

    /**
     * @param Town The town which is going to be updated in your dataset
     * @return The id of the updated town
     */
    public Long updateTown(Town Town);

    /**
     * @param person The person which is going to be removed from your dataset
     */
    public void deletePerson(Person person);

    /**
     * @param Town The town which is going to be removed from your dataset
     */
    public void deleteTown(Town Town);

    /**
     * @param amount The amount if people requested
     * @param offset The amount if people skipped
     * @return A list of people found in the given range
     */
    public List<Person> getPeople(int amount, int offset);

    /**
     * @param amount The amount if towns requested
     * @param offset The amount if towns skipped
     * @return A list of tows found in the given range
     */
    public List<Town> getTown(int amount, int offset);

    /**
     * @return A list with all the towns stored in your data set
     */
    public List<Town> getTown();

    /**
     * @return The amount of people stored in your data set
     */
    public Long countPeople();

    /**
     * @return The amount of towns stored in your data set
     */
    public Long countTown();
}
