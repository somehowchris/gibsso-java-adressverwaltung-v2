package TestingHelpers;

import java.awt.*;
import javax.swing.*;

/**
 * Diese Methoden geben Referenzen auf die gewünschten Komponenten eines GUI
 * zurück. Im einfsachsten Fall weisen die Komponenten zur Identifikation einen
 * Namen auf. Der Name kann in den Eigenschaften (Properties) der Komponente in
 * Netbeans gesetzt werden.
 *
 * @author Ichiro Suzuki is a physicist/computer engineer working in
 * experimental particle physics at Fermilab. He holds a Ph.D. in physics.
 */
public class UIUtils {

    static int counter;

    /**
     * Komonente auf Grund des Namens rekursiv suchen und zurückgeben.
     *
     * @param parent Vaterkomponente, meist das Fenster (JFrame)
     * @param name Name der Kompoentne
     * @return gewünschte Komponente oder null
     */
    public static Component getChildNamed(Component parent, String name) {

        // Debug line
        //System.out.println("Class: " + parent.getClass() +
        //		" Name: " + parent.getName());
        if (name.equals(parent.getName())) {
            return parent;
        }

        if (parent instanceof Container) {
            Component[] children = (parent instanceof JMenu)
                    ? ((JMenu) parent).getMenuComponents()
                    : ((Container) parent).getComponents();

            for (int i = 0; i < children.length; ++i) {
                Component child = getChildNamed(children[i], name);
                if (child != null) {
                    return child;
                }
            }
        }

        return null;
    }

    /**
     * Komonente aus einem Unterfenster zurückgeben.
     *
     * @param parent Vaterkomponente, meist JFrame
     * @param klass Klasse der gewünschten Komponente, z.B. JButton
     * @param index Index der Komponente (0, falls erste Komponente)
     * @return gewünschte Komponente
     */
    public static Component getChildIndexed(Component parent, String klass, int index) {
        counter = 0;

        // Step in only owned windows and ignore its components in JFrame
        if (parent instanceof Window) {
            Component[] children = ((Window) parent).getOwnedWindows();

            for (int i = 0; i < children.length; ++i) {
                // take only active windows
                if (children[i] instanceof Window
                        && !((Window) children[i]).isActive()) {
                    continue;
                }

                Component child = getChildIndexedInternal(
                        children[i], klass, index);
                if (child != null) {
                    return child;
                }
            }
        }

        return null;
    }

    /**
     * Wird von getChildIndexed() aufgerufen. Sucht nach der gewünschten
     * Komponente.
     *
     * @param parent Vaterkomponente
     * @param klass Klasse, z.B. JButton
     * @param index Index der gesuchten Komponente
     * @return gesuchte Komponente
     */
    private static Component getChildIndexedInternal(Component parent, String klass, int index) {

        // Debug line
        //System.out.println("Class: " + parent.getClass() +
        //		" Name: " + parent.getName());
        if (parent.getClass().toString().endsWith(klass)) {
            if (counter == index) {
                return parent;
            }
            ++counter;
        }

        if (parent instanceof Container) {
            Component[] children = (parent instanceof JMenu)
                    ? ((JMenu) parent).getMenuComponents()
                    : ((Container) parent).getComponents();

            for (int i = 0; i < children.length; ++i) {
                Component child = getChildIndexedInternal(
                        children[i], klass, index);
                if (child != null) {
                    return child;
                }
            }
        }

        return null;
    }
}
