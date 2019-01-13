/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung.exports;

import adressverwaltung.enums.PersonColumnEnum;
import adressverwaltung.models.Town;
import adressverwaltung.models.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import adressverwaltung.services.Service;

/**
 *
 * @author Christof Weickhardt
 */
public class JsonExport extends Export {

    String jsonData;

    /**
     * Constructor to export a list of people
     *
     * @param connection Connection to get related data
     * @param people People to export
     */
    public JsonExport(Service connection, List<Person> people) {
        super(connection, people);
    }

    /**
     * Constructor to export all
     *
     * @param connection Connection to get all the data
     */
    public JsonExport(Service connection) {
        super(connection);
    }

    /**
     * Constructor to export from given data set
     *
     * @param connection Connection to get needed files
     * @param people People to export
     * @param towns Towns to export
     */
    public JsonExport(Service connection, List<Person> people, List<Town> towns) {
        super(connection, people, towns);
    }

    /**
     * Custom json render function
     */
    @Override
    public void render() {

        JSONArray jsonArray = new JSONArray();

        people.stream().map((p) -> {
            JSONObject obj = new JSONObject();
            Town o = new Town();
            if (p.getOid() != null) {
                o = this.connection.getTown(p.getOid());
            }
            obj.put(PersonColumnEnum.ID.get(), p.getId());
            obj.put(PersonColumnEnum.LAST_NAME.get(), p.getLastName() == null ? "" : p.getLastName());
            obj.put(PersonColumnEnum.FIRST_NAME.get(), p.getFirstName() == null ? "" : p.getFirstName());
            obj.put(PersonColumnEnum.STREET.get(), p.getAddress() == null ? "" : p.getAddress());
            obj.put(PersonColumnEnum.TOWN_NAME.get(), o == null ? "" : o.getName() == null ? "" : o.getName());
            obj.put(PersonColumnEnum.TOWN_PLZ.get(), o == null ? "" : o.getPlz() > 0 ? "" : o.getPlz());
            obj.put(PersonColumnEnum.PHONE.get(), p.getPhone() == null ? "" : p.getPhone());
            obj.put(PersonColumnEnum.MOBILE.get(), p.getMobile() == null ? "" : p.getMobile());
            obj.put(PersonColumnEnum.EMAIL.get(), p.getEmail() == null ? "" : p.getEmail());
            return obj;
        }).forEachOrdered((obj) -> {
            jsonArray.add(obj);
        });
        JsonParser parser = new JsonParser();
        JsonArray json = parser.parse(jsonArray.toJSONString()).getAsJsonArray();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        jsonData = gson.toJson(json);
    }

    /**
     * Custom json write function
     */
    @Override
    public void write() {
        try {
            PrintWriter pw = new PrintWriter(new File(this.path));
            pw.write(jsonData);
            pw.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Couldnt write file: " + this.path);
        }
    }

}
