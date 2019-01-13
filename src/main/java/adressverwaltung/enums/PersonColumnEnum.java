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
public enum PersonColumnEnum {

    /**
     * Column key of id
     */
    ID("ID"),
    /**
     * Column key of name
     */
    LAST_NAME("Name"),
    /**
     * Column key of vorname
     */
    FIRST_NAME("Vorname"),
    /**
     * Column key of street
     */
    STREET("Strasse"),
    /**
     * Column key of twon name
     */
    TOWN_NAME("Ort_Name"),
    /**
     * Column key of town plz
     */
    TOWN_PLZ("Ort_Plz"),
    /**
     * Column key of phone
     */
    PHONE("Telefon"),
    /**
     * Column key of mobile
     */
    MOBILE("Handy"),
    /**
     * Column key of email
     */
    EMAIL("Email");

    String value;

    PersonColumnEnum(String values) {
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
        for (PersonColumnEnum enumType : PersonColumnEnum.values()) {
            values.add(enumType.get());
        }
        return values.stream().toArray(String[]::new);
    }
}
