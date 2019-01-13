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
public enum SystemPropertyEnum {

    /**
     * System property user home
     */
    USER_HOME(System.getProperty("user.home")),
    /**
     * System property working directory
     */
    USER_DIR(System.getProperty("user.dir")),
    /**
     * System property file separator
     */
    FILE_SEPERATOR(System.getProperty("file.separator")),
    /**
     * System property java home
     */
    JAVA_HOME(System.getProperty("java.home")),
    /**
     * System property user name
     */
    USER_NAME(System.getProperty("user.name")),
    /**
     * System property line separator
     */
    LINE_SEPERATOR(System.getProperty("line.separator"));

    String value;

    SystemPropertyEnum(String values) {
        this.value = values;
    }

    /**
     * Get the value of the enum
     *
     * @return Key as String
     */
    public String get() {
        return value;
    }

    /**
     * Get alls values of the enum type
     *
     * @return String array of all values
     */
    public static String[] getValues() {
        ArrayList<String> values = new ArrayList<>();
        for (SystemPropertyEnum enumType : SystemPropertyEnum.values()) {
            values.add(enumType.get());
        }
        return values.stream().toArray(String[]::new);
    }
}
