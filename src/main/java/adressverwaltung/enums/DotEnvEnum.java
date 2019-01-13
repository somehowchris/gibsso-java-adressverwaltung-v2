/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung.enums;

import java.util.ArrayList;

/**
 * Enum for all keys needed for DotEnv
 *
 * @author Christof Weickhardt
 */
public enum DotEnvEnum {

    /**
     * Dot env user key
     */
    USER("DATABASE_USER"),
    /**
     * Dot env password key
     */
    PASSWORD("DATABASE_PASSWORD"),
    /**
     * Dot env host key
     */
    HOST("DATABASE_HOST"),
    /**
     * Dot env port key
     */
    PORT("DATABASE_PORT"),
    /**
     * Dot env table name key
     */
    TABLE_NAME("DATABASE_NAME"),
    /**
     * Dot env database usage key
     */
    DB_USE("DATABASE_USE");

    String value;

    DotEnvEnum(String values) {
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
        for (DotEnvEnum enumType : DotEnvEnum.values()) {
            values.add(enumType.get());
        }
        return values.stream().toArray(String[]::new);
    }
}
