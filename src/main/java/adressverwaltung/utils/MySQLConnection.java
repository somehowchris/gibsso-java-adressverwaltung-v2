/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung.utils;

import adressverwaltung.errors.CanNotConnectToDatabaseError;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Helper class to connect to a database
 *
 * @author Christof Weickhardt
 */
public class MySQLConnection {

    String host;
    String password;
    String name;
    String port;
    String username;
    boolean force;
    String dialect;

    /**
     * @param host Host of the database
     * @param name Name of the database
     * @param port Port of the database
     * @param username Username to access the database
     * @param password Password to access the database
     * @param force Use the force to create and delete
     * @param dialect  Dialect of the service to use
     */
    public MySQLConnection(String host, String password, String name, String port, String username, boolean force, String dialect) {
        this.host = host;
        this.password = password;
        this.name = name;
        this.port = port;
        this.username = username;
        this.force = force;
        this.dialect = dialect;
    }

    /**
     * Function to check on a connection to a database
     *
     * @return Returns true if the connection has been established
     * @throws CanNotConnectToDatabaseError If not possible to connect to the
     * database
     */
    public boolean verify() throws CanNotConnectToDatabaseError {
        String url = "jdbc:" + dialect + "://" + host + ":" + port + "/" + name;
        System.out.println("Connecting database...");
        try {
            DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");
            return true;
        } catch (SQLException e) {
            throw new CanNotConnectToDatabaseError();
        }
    }

    /**
     * @deprecated
     * @return Boolean if the Database has been created
     * @throws adressverwaltung.errors.CanNotConnectToDatabaseError If not able to connect to database
     */
    public boolean createDatabase() throws CanNotConnectToDatabaseError {
        String url = "jdbc:" + dialect + "://" + host + ":" + port + "/";
        System.out.println("Connecting database...");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            try (Statement st = connection.createStatement()) {
                if (force) {
                    dropDatabase();
                }
                st.executeUpdate("CREATE DATABASE " + name);
            }
            System.out.println("Created database!");
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            
           throw new CanNotConnectToDatabaseError();
        }
    }

    /**
     *
     * @return Boolean if the database has been dropped
     * @throws adressverwaltung.errors.CanNotConnectToDatabaseError If not able to connect to the database
     */
    public boolean dropDatabase() throws CanNotConnectToDatabaseError {
        String url = "jdbc:" + dialect + "://" + host + ":" + port + "/";
        System.out.println("Connecting database...");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            try (Statement st = connection.createStatement()) {
                st.executeUpdate("DROP DATABASE IF EXISTS " + name);
            }
            System.out.println("Droped database!");
            return true;
        } catch (SQLException e) {
            throw new CanNotConnectToDatabaseError();
        } catch (ClassNotFoundException ex) {
           throw new CanNotConnectToDatabaseError();
        }
    }
}
