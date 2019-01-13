/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung.utils;

import adressverwaltung.enums.DotEnvEnum;
import adressverwaltung.enums.SystemPropertyEnum;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A small class to store and update values of a .env file in the users home
 * directory
 *
 * @author Christof Weickhardt
 */
public class DotEnv {

    /**
     * Local variable to save the file seperator of the current platform running
     * on
     */
    static String sep = SystemPropertyEnum.FILE_SEPERATOR.get();

    /**
     * Local variable to save the home of the current user running this
     * application
     */
    static String dir = SystemPropertyEnum.USER_HOME.get();

    /**
     * Loacl list of keys to look out for
     *
     * @see DotEnvEnum
     */
    static List<String> keys = Arrays.asList(DotEnvEnum.getValues());

    /**
     * Public function to get the values needed from a .env file of the users
     * root if this exists
     *
     * @return A list of key and values paires found in the .env file
     * @see BufferedReader
     * @see HashMap
     */
    public static HashMap<String, String> getDotEnv() {
        HashMap<String, String> values = new HashMap<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(dir + sep + ".env"));

            String line = reader.readLine();
            while (line != null) {
                if (keys.contains(line.split("=")[0])) {
                    values.put(line.split("=")[0], line.split("=").length > 1 ? line.split("=")[1] : "");
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
        }
        return values;
    }

    /**
     * Function to check on all needed values to a database
     *
     * @param dotEnv A key values pair list to be validated
     * @return The result of the key check
     */
    public static boolean containsAllKeys(HashMap<String, String> dotEnv) {
        return keys.stream().noneMatch((key) -> (!dotEnv.containsKey(key)));
    }

    /**
     * A function to store data which the user put
     *
     * @throws FileNotFoundException In the event of not having a .env file
     * @throws IOException In the event of having to the needed rights on the
     * file
     * @param dotEnv A key values pair list to be set in the .env file of the
     * current users home directory
     */
    public static void setDotEnv(HashMap<String, String> dotEnv) throws FileNotFoundException, IOException {
        if (!new File(dir).canRead()) {
            dir = SystemPropertyEnum.USER_DIR.get();
        }
        File f = new File(dir + sep + ".env");
        String data = "";
        if (f.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(f));

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.contains("=")) {
                    if (!dotEnv.containsKey(currentLine.split("=")[0])) {
                        data += currentLine + System.getProperty("line.separator");
                    }
                } else {
                    data += currentLine + System.getProperty("line.separator");
                }
            }
        }

        data = dotEnv.keySet().stream().map((key) -> key + "=" + dotEnv.get(key) + System.getProperty("line.separator")).reduce(data, String::concat);

        BufferedWriter br = null;
        FileWriter fr = null;

        try {
            fr = new FileWriter(f);
            br = new BufferedWriter(fr);
            br.append(data);
        } catch (IOException e) {
        } finally {
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
            }
        }
    }
}
