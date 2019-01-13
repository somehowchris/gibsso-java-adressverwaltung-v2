/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung.exports;

import adressverwaltung.enums.FileTypeEnum;
import adressverwaltung.enums.TownColumnEnum;
import adressverwaltung.enums.PersonColumnEnum;
import adressverwaltung.enums.SystemPropertyEnum;
import adressverwaltung.models.Town;
import adressverwaltung.models.Person;
import adressverwaltung.utils.FileTypeFilter;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.commons.io.FilenameUtils;
import adressverwaltung.services.Service;

/**
 * Basic functions for an export
 *
 * @author Christof Weickhardt
 */
public class Export {

    Service connection;
    List<Person> people;
    List<Town> towns;
    String[] personColumns = PersonColumnEnum.getValues();
    String[] townColumns = TownColumnEnum.getValues();
    String path;

    /**
     * Constructor to export a list of people
     *
     * @param connection Connection to get related data
     * @param people People to export
     */
    public Export(Service connection, List<Person> people) {
        this.connection = connection;
        this.people = people;
        this.towns = getTowns();
    }

    /**
     * Constructor to export from given data set
     *
     * @param connection Connection to get needed files
     * @param people People to export
     * @param towns Towns to export
     */
    public Export(Service connection, List<Person> people, List<Town> towns) {
        this.connection = connection;
        this.people = people;
        this.towns = towns;
    }

    /**
     * Constructor to export all
     *
     * @param connection Connection to get all the data
     */
    public Export(Service connection) {
        this.connection = connection;
        this.towns = this.connection.getTown();
        this.people = this.connection.getPeople(new Integer(this.connection.countPeople() + ""), 0);
    }

    /**
     * Configuration to export a file with a chosen name and export type
     *
     * @return Returns a configured Export
     */
    public Export configure() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(SystemPropertyEnum.USER_DIR.get()));
        chooser.setDialogTitle("Please choose a location to export your file to");
        for (FileTypeEnum fileType : FileTypeEnum.values()) {
            chooser.addChoosableFileFilter(new FileTypeFilter(fileType));
        }
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            final String extension = FilenameUtils.getExtension(chooser.getSelectedFile().getName());
            if ("".equals(extension)) {
                path = chooser.getSelectedFile().getAbsolutePath() + ((FileTypeFilter) chooser.getFileFilter()).getExtension();
                return createExportConfiguration((FileTypeFilter) chooser.getFileFilter());
            } else if (Arrays.asList(FileTypeEnum.values()).stream().filter(el -> el.getExtension().equalsIgnoreCase("." + extension)).count() == 1) {
                Export e = null;
                if (("." + extension.toLowerCase()).equalsIgnoreCase(FileTypeEnum.CSV.getExtension())) {
                    e = new ExcelExport(connection, people, towns);
                } else if (("." + extension.toLowerCase()).equalsIgnoreCase(FileTypeEnum.JSON.getExtension())) {
                    e = new ExcelExport(connection, people, towns);
                } else if (("." + extension.toLowerCase()).equalsIgnoreCase(FileTypeEnum.XLSX.getExtension())) {
                    e = new ExcelExport(connection, people, towns);
                } else if (("." + extension.toLowerCase()).equalsIgnoreCase(FileTypeEnum.XML.getExtension())) {
                    e = new XmlExport(connection, people, towns);
                }
                e.setPath(chooser.getSelectedFile().getAbsolutePath());
                return e;
            } else {
                if (extension.length() <= 4) {
                    path = chooser.getSelectedFile().getAbsolutePath().replaceAll("." + extension, "") + ((FileTypeFilter) chooser.getFileFilter()).getExtension();
                }
                if (extension.length() > 4) {
                    path = chooser.getSelectedFile().getAbsolutePath() + ((FileTypeFilter) chooser.getFileFilter()).getExtension();
                }
                return createExportConfiguration((FileTypeFilter) chooser.getFileFilter());
            }
        } else {
            return null;
        }
    }

    /**
     * Custom render function
     */
    public void render() {
    }

    /**
     * Custom write function
     */
    public void write() {
    }

    /**
     * Methode to open the direcotry of the file exported
     */
    public void open() {
        try {
            openDirectoryOfFile(new File(path));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Couldnt open file: " + path);
        }
    }

    /**
     * Opens the parent folder of a given file
     *
     * @param dir File to open
     */
    private void openDirectoryOfFile(File dir) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.open(dir.getParentFile());
    }

    /**
     * Checks if town already has been loaded
     *
     * @param towns list of towns to check from
     * @param town ID of the town to check
     */
    private boolean containsTown(ArrayList<Town> towns, Long town) {
        if (town != null) {
            if (towns.stream().anyMatch((o) -> (Objects.equals(o.getTid(), town)))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Getter of the related town
     */
    private ArrayList<Town> getTowns() {
        ArrayList<Town> towns = new ArrayList<>();
        people.stream().filter((p) -> (containsTown(towns, p.getOid()))).forEachOrdered((p) -> {
            towns.add(connection.getTown(p.getId()));
        });
        return towns;
    }

    /**
     * Setter of the path to export to
     *
     * @param path Path to export to
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Creates a new Export object by a file type filter
     *
     * @param filter FileTypeFilter to create from
     */
    private Export createExportConfiguration(FileTypeFilter filter) {
        Export e = null;
        switch (filter.getFileType()) {
            case CSV:
                e = new CsvExport(connection, people, towns);
                break;
            case JSON:
                e = new JsonExport(connection, people, towns);
                break;
            case XLSX:
                e = new ExcelExport(connection, people, towns);
                break;
            case XML:
                e = new XmlExport(connection, people, towns);
                break;
        }
        e.setPath(path);
        return e;
    }

    /**
     * Getter of the path to export to
     *
     * @return Path of file
     */
    public String getPath() {
        return path;
    }

}
