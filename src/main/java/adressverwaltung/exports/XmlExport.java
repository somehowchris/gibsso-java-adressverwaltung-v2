/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung.exports;

import adressverwaltung.enums.PersonColumnEnum;
import adressverwaltung.models.Town;
import adressverwaltung.models.Person;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import adressverwaltung.services.Service;

/**
 *
 * @author Christof Weickhardt
 */
public class XmlExport extends Export {

    DOMSource xmlSource;

    /**
     * Constructor to export a list of people
     *
     * @param connection Connection to get related data
     * @param people People to export
     */
    public XmlExport(Service connection, List<Person> people) {
        super(connection, people);
    }

    /**
     * Constructor to export all
     *
     * @param connection Connection to get all the data
     */
    public XmlExport(Service connection) {
        super(connection);
    }

    /**
     * Constructor to export from given data set
     *
     * @param connection Connection to get needed files
     * @param people People to export
     * @param towns Towns to export
     */
    public XmlExport(Service connection, List<Person> people, List<Town> towns) {
        super(connection, people, towns);
    }

    /**
     * Custom xml render function
     */
    @Override
    public void render() {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("people");
            doc.appendChild(rootElement);

            this.people.forEach((p) -> {
                Town o = null;
                if (p.getOid() != null) {
                    o = this.connection.getTown(p.getOid());
                }

                // staff elements
                Element person = doc.createElement("person");
                rootElement.appendChild(person);

                // set attribute to staff element
                Attr attr = doc.createAttribute(PersonColumnEnum.ID.get());
                attr.setValue(p.getId() + "");
                person.setAttributeNode(attr);

                // firstname elements
                Element firstname = doc.createElement(PersonColumnEnum.FIRST_NAME.get().toLowerCase());
                firstname.appendChild(doc.createTextNode(p.getLastName() == null ? "" : p.getLastName()));
                person.appendChild(firstname);

                // lastname elements
                Element lastname = doc.createElement(PersonColumnEnum.LAST_NAME.get().toLowerCase());
                lastname.appendChild(doc.createTextNode(p.getFirstName() == null ? "" : p.getFirstName()));
                person.appendChild(lastname);

                // street elements
                Element street = doc.createElement(PersonColumnEnum.STREET.get().toLowerCase());
                street.appendChild(doc.createTextNode(p.getAddress() == null ? "" : p.getAddress()));
                person.appendChild(street);

                // town elements
                Element town = doc.createElement("town");
                person.appendChild(town);

                int plzNr = 0;
                if (o != null) {
                    plzNr = o.getPlz();
                }
                // plz elements
                Element plz = doc.createElement("plz");
                plz.appendChild(doc.createTextNode(plzNr > 0 ? plzNr + "" : ""));
                town.appendChild(plz);

                // name elements
                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(o == null ? "" : o.getName() == null ? "" : o.getName()));
                town.appendChild(name);

                // phone elements
                Element phone = doc.createElement(PersonColumnEnum.PHONE.get().toLowerCase());
                phone.appendChild(doc.createTextNode(p.getPhone() == null ? "" : p.getPhone()));
                person.appendChild(phone);

                // email elements
                Element email = doc.createElement(PersonColumnEnum.EMAIL.get().toLowerCase());
                email.appendChild(doc.createTextNode(p.getEmail() == null ? "" : p.getEmail()));
                person.appendChild(email);

                // mobile elements
                Element mobile = doc.createElement(PersonColumnEnum.MOBILE.get().toLowerCase());
                mobile.appendChild(doc.createTextNode(p.getMobile() == null ? "" : p.getMobile()));
                person.appendChild(mobile);
            });

            // write the content into xml file
            xmlSource = new DOMSource(doc);

        } catch (ParserConfigurationException pce) {
        }
    }

    /**
     * Custom xml write function
     */
    @Override
    public void write() {

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            StreamResult result = new StreamResult(new File(path));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);
            transformer.transform(xmlSource, result);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(XmlExport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(XmlExport.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
