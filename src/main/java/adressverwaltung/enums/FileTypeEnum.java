/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung.enums;

import java.util.ArrayList;

/**
 *
 * @author Christof Weickhardt
 */
public enum FileTypeEnum {

    /**
     * CSV file type
     */
    CSV("CSV", ".csv"),
    /**
     * XML file type
     */
    XML("XML", ".xml"),
    /**
     * Excel file type
     */
    XLSX("Excel", ".xlsx"),
    /**
     * Json file type
     */
    JSON("Json", ".json");

    String description;
    String ext;

    FileTypeEnum(String description, String ext) {
        this.description = description;
        this.ext = ext;
    }

    /**
     * Get the value of the enum
     *
     * @return Key as String
     */
    public String get() {
        return description;
    }

    /**
     * Get the extension of the selected file type
     *
     * @return Extension as String
     */
    public String getExtension() {
        return ext;
    }

    /**
     * Get the description of the selected file type
     *
     * @deprecated
     * @return Description as String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get alls values of the enum type
     *
     * @return String array of all values
     */
    public static String[] getExtensions() {
        ArrayList<String> values = new ArrayList<>();
        for (FileTypeEnum enumType : FileTypeEnum.values()) {
            values.add(enumType.getExtension());
        }
        return values.stream().toArray(String[]::new);
    }
}
