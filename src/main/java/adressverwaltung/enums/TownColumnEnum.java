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
public enum TownColumnEnum {

    /**
     * Column key of id
     */
    ID("ID"),
    /**
     * Column key of name
     */
    NAME("Name"),
    /**
     * Column key of plz
     */
    PLZ("Plz");

    String value;

    TownColumnEnum(String values) {
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
        for (TownColumnEnum enumType : TownColumnEnum.values()) {
            values.add(enumType.get());
        }
        return values.stream().toArray(String[]::new);
    }
}
