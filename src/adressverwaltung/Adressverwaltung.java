/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author Christof Weickhardt
 * @author Nicola Temporal
 */
public class Adressverwaltung {
    private int anr;
    private String name;
    private String vorname;
    private String strasse;
    private String plz;
    private String telefon;
    private String handy;
    private String email;

    public Adressverwaltung() {

    }

    public Adressverwaltung(int anr) throws FileNotFoundException, IOException {
        this.anr = anr;
        BufferedReader f = new BufferedReader(new FileReader(Integer.toString(anr) + ".person"));
        this.name = f.readLine();
        this.vorname = f.readLine();
        this.strasse = f.readLine();
        this.plz = f.readLine();
        this.telefon = f.readLine();
        this.handy = f.readLine();
        this.email = f.readLine();
        f.close();
    }

    public void setAnr(int anr) {
        this.anr = anr;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getVorname() {
        return this.vorname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHandy() {
        return handy;
    }

    public void setHandy(String handy) {
        this.handy = handy;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public void Speichern() throws IOException {
        FileOutputStream outx = new FileOutputStream(Integer.toString(anr) + ".person", false);
        PrintStream out = new PrintStream(outx);
        out.println(name);
        out.println(vorname);
        out.println(strasse);
        out.println(plz);
        out.println(telefon);
        out.println(handy);
        out.println(email);

        out.close();
    }
}
