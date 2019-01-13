/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung.services;

import adressverwaltung.enums.DotEnvEnum;
import adressverwaltung.errors.CanNotConnectToDatabaseError;
import adressverwaltung.errors.WrongSchemaError;
import adressverwaltung.models.Person;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import adressverwaltung.models.Town;
import adressverwaltung.utils.MySQLConnection;
import java.util.Arrays;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 * A Database controller using Hibernate to communicate with the database
 *
 * @author Christof Weickhardt
 */
public class DatabaseService implements Service {

    EntityManager em;
    EntityManagerFactory emf;

    /**
     *
     * @param connectionValues Connection Values to use
     * @throws adressverwaltung.errors.WrongSchemaError If database schmea is corrupted
     * @throws adressverwaltung.errors.CanNotConnectToDatabaseError If not able to connect to database
     */
    public DatabaseService(HashMap<String, String> connectionValues) throws WrongSchemaError, CanNotConnectToDatabaseError {
        Map properties = new HashMap();
        properties.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
        properties.put("javax.persistence.jdbc.url", "jdbc:mysql://" + connectionValues.get("DATABASE_HOST") + ":" + connectionValues.get("DATABASE_PORT") + "/" + connectionValues.get("DATABASE_NAME") + "?zeroDateTimeBehavior=convertToNull");
        properties.put("javax.persistence.jdbc.user", connectionValues.get("DATABASE_USER"));
        properties.put("javax.persistence.jdbc.password", connectionValues.get("DATABASE_PASSWORD"));
        try {
            emf = Persistence.createEntityManagerFactory("AdressverwaltungPU", properties);
        } catch (Exception e) {
        }
        try{
            em = (EntityManager) emf.createEntityManager();
            Person p = new Person("test", "test");
            long pid = insertPerson(p);
            deletePerson(p);
            Town t = new Town(999, "test");
            long tid = insertTown(t);
            deleteTown(t);
        }catch(Exception e){
            MySQLConnection con = new MySQLConnection(connectionValues.get(DotEnvEnum.HOST.get()), connectionValues.get(DotEnvEnum.PASSWORD.get()), connectionValues.get(DotEnvEnum.TABLE_NAME.get()), connectionValues.get(DotEnvEnum.PORT.get()), connectionValues.get(DotEnvEnum.USER.get()), true, "mysql");
            con.dropDatabase();
            con.createDatabase();
            throw new WrongSchemaError();
        }
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Person getPerson(Long id) {
        return em.find(Person.class, id);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Town getTown(Long id) {
        return em.find(Town.class, id);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public List<Person> searchPerson(Person person) {
        try {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.lastName LIKE '%" + person.getLastName() + "%' AND p.firstName LIKE '%" + person.getFirstName() + "%'", Person.class);
            return query.getResultList();
        } catch (Exception e) {
            List<Person> p = Arrays.asList();
            return p;
        }
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public List<Town> searchTown(Town Town) {
        try {
            TypedQuery<Town> query = em.createQuery("SELECT o FROM Town o WHERE o.plz LIKE '%" + Town.getPlz() + "%' AND o.name LIKE '%" + Town.getName() + "%'", Town.class);
            return query.getResultList();
        } catch (Exception e) {
            List<Town> t = Arrays.asList();
            return t;
        }
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Long insertPerson(Person person) {
        em.getTransaction().begin();
        em.persist(person);
        em.getTransaction().commit();
        return person.getId();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Long insertTown(Town Town) {
        em.getTransaction().begin();
        em.persist(Town);
        em.getTransaction().commit();
        return Town.getTid();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Long updatePerson(Person person) {
        em.getTransaction().begin();
        em.merge(person);
        em.getTransaction().commit();
        return person.getId();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Long updateTown(Town Town) {
        em.getTransaction().begin();
        em.merge(Town);
        em.getTransaction().commit();
        return Town.getTid();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void deletePerson(Person person) {
        em.getTransaction().begin();
        em.remove(person);
        em.getTransaction().commit();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void deleteTown(Town Town) throws Error {
        em.getTransaction().begin();
        String sql = "SELECT COUNT(p.pid) FROM Person p WHERE p.tid='" + Town.getTid() + "'";
        Query q = em.createQuery(sql);
        long count = (long) q.getSingleResult();
        em.getTransaction().commit();

        if (count == 0) {
            em.getTransaction().begin();
            em.remove(Town);
            em.getTransaction().commit();
        } else {
            throw new Error("References on this place still exist " + Town.getTid());
        }

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public List<Person> getPeople(int amount, int offset) {
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
        query.setMaxResults(amount);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public List<Town> getTown(int amount, int offset) {
        TypedQuery<Town> query = em.createQuery("SELECT o FROM Town o", Town.class);
        query.setMaxResults(amount);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public List<Town> getTown() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Town> cq = cb.createQuery(Town.class);
        Root<Town> rootEntry = cq.from(Town.class);
        CriteriaQuery<Town> all = cq.select(rootEntry);
        TypedQuery<Town> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Long countPeople() {
        String sql = "SELECT COUNT(p.pid) FROM Person p";
        Query q = em.createQuery(sql);
        return (long) q.getSingleResult();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Long countTown() {
        String sql = "SELECT COUNT(o.plz) FROM Town o";
        Query q = em.createQuery(sql);
        return (long) q.getSingleResult();
    }
}
