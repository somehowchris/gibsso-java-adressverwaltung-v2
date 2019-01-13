/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung.exports;

import adressverwaltung.models.Person;
import adressverwaltung.models.Town;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import adressverwaltung.services.Service;

/**
 *
 * @author Christof Weickhardt
 */
public class CsvExport extends Export {

    String csvData;

    /**
     * Constructor to export a list of people
     *
     * @param connection Connection to get related data
     * @param people People to export
     */
    public CsvExport(Service connection, List<Person> people) {
        super(connection, people);
    }

    /**
     * Constructor to export all
     *
     * @param connection Connection to get all the data
     */
    public CsvExport(Service connection) {
        super(connection);
    }

    /**
     * Constructor to export from given data set
     *
     * @param connection Connection to get needed files
     * @param people People to export
     * @param towns Towns to export
     */
    public CsvExport(Service connection, List<Person> people, List<Town> towns) {
        super(connection, people, towns);
    }

    /**
     * Custom csv render function
     */
    @Override
    public void render() {
        ArrayList<String> data = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(";", this.personColumns));
        sb.append("\n");
        this.people.forEach((p) -> {
            String line = "";
            Town o = new Town();
            if (p.getOid() != null) {
                o = this.connection.getTown(p.getOid());
            }
            line += p.getId() + ";";
            line += (p.getLastName() == null ? "" : p.getLastName()) + ";";
            line += (p.getFirstName() == null ? "" : p.getFirstName()) + ";";
            line += (p.getAddress() == null ? "" : p.getAddress());
            line += (o == null ? "" : o.getName() == null ? "" : o.getName()) + ";";
            line += (o == null ? "" : o.getPlz() > 0 ? "" : o.getPlz()) + ";";
            line += (p.getPhone() == null ? "" : p.getPhone()) + ";";
            line += (p.getMobile() == null ? "" : p.getMobile()) + ";";
            line += (p.getEmail() == null ? "" : p.getEmail());
            data.add(line);
        });

        csvData = String.join("\n", data);

        csvData = sb.toString() + csvData;
    }

    /**
     * Custom csv write function
     */
    @Override
    public void write() {
        try {
            PrintWriter pw = new PrintWriter(new File(this.path));
            pw.write(csvData);
            pw.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Couldnt write file: " + this.path);
        }
    }

}
