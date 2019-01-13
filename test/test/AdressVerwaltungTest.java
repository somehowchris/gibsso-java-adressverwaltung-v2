package test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//import adressverwaltung.model.PersonData;
import adressverwaltung.AdressverwaltungForm;
import test.TestUtils;
import static test.TestUtils.getChildNamed;
import java.awt.Component;
import java.awt.Container;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.*;
/**
 *
 * @author Dominik
 */
public class AdressVerwaltungTest {
    
    static AdressverwaltungForm av;
    
    
    public AdressVerwaltungTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.av = new AdressverwaltungForm();
        this.av.setVisible(true);
        assertNotNull(this.av);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class KontoVerwaltung.
     */
    /*
    @Test
    public void testInputJTex() throws InterruptedException {
        
        String testString = "testing";

        // instantiated?
        
	JTextField kontonummer = (JTextField)TestUtils.getChildNamed(this.kv, "kontonummer");
        JTextField name = (JTextField)TestUtils.getChildNamed(this.kv, "nachname");
        JTextField vorname = (JTextField)TestUtils.getChildNamed(this.kv, "vorname");
        
        JTextField betrag = (JTextField)TestUtils.getChildNamed(this.kv, "betrag");
        
	assertNotNull(kontonummer);
        kontonummer.setText(testString);
	kontonummer.postActionEvent();  // type in a test message + ENTER
	assertEquals(testString, kontonummer.getText());
        
        assertNotNull(name); 
        name.setText(testString);
	name.postActionEvent();  // type in a test message + ENTER
	assertEquals(testString, name.getText());
        
        assertNotNull(vorname); 
        vorname.setText(testString);
	vorname.postActionEvent();  // type in a test message + ENTER
	assertEquals(testString, vorname.getText());
        
        assertNotNull(betrag); 
        betrag.setText(testString);
	betrag.postActionEvent();  // type in a test message + ENTER
	assertEquals(testString, betrag.getText());
	
        
        sleep(3000);
        
        
        // TODO review the generated test code and remove the default call to fail.
        
    }
*/
    /**
     *
     * @throws InterruptedException
     */
    
    @Test
    public void testOeffnen() throws InterruptedException {
        final JButton save = (JButton) TestUtils.getChildNamed(av, "search");
        assertNotNull(save);
        final JTextField id = (JTextField) TestUtils.getChildNamed(av, "idName");
        assertNotNull(id);
        id.setText("1");
        
        save.doClick();

    }
    
    /*@Test
    public void testNeu() throws InterruptedException {
        String testString = "testing";
        
         // instantiated?
        
        final JButton neu = (JButton)TestUtils.getChildNamed(this.av, "neu");
	JTextField kontonummer = (JTextField)TestUtils.getChildNamed(this.av, "kontonummer");
        JTextField name = (JTextField)TestUtils.getChildNamed(this.av, "nachname");
        JTextField vorname = (JTextField)TestUtils.getChildNamed(this.av, "vorname");
        
        JTextField betrag = (JTextField)TestUtils.getChildNamed(this.av, "betrag");
        
	assertNotNull(kontonummer);
        kontonummer.setText(testString);
	kontonummer.postActionEvent();  // type in a test message + ENTER
	assertEquals(testString, kontonummer.getText());
        
        assertNotNull(name); 
        name.setText(testString);
	name.postActionEvent();  // type in a test message + ENTER
	assertEquals(testString, name.getText());
        
        assertNotNull(vorname); 
        vorname.setText(testString);
	vorname.postActionEvent();  // type in a test message + ENTER
	assertEquals(testString, vorname.getText());
        
        assertNotNull(betrag); 
        betrag.setText(testString);
	betrag.postActionEvent();  // type in a test message + ENTER
	assertEquals(testString, betrag.getText());
	
        //sleep(1500);
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
		neu.doClick();
            
            }
	});
        
        sleep(1000);
        assertEquals("", vorname.getText());  
        assertEquals("", kontonummer.getText());  
        assertEquals("", name.getText());  
        assertEquals("", betrag.getText());  
    }
    
    @Test
    public void testSpeichern() throws InterruptedException {
        final JButton oeffnen = (JButton)TestUtils.getChildNamed(this.av, "oeffnen");
        final JButton neu = (JButton)TestUtils.getChildNamed(this.av, "neu");
        final JButton speichern = (JButton)TestUtils.getChildNamed(this.av, "speichern");
        JTextField kontonummer = (JTextField)TestUtils.getChildNamed(this.av, "kontonummer");
        JTextField vorname = (JTextField)TestUtils.getChildNamed(this.av, "vorname");
        JTextField nachname = (JTextField)TestUtils.getChildNamed(this.av, "nachname");
        assertNotNull(vorname);
        
        String result = "test";
        
        assertNotNull(neu);
        assertNotNull(vorname);
	assertNotNull(speichern);
        assertNotNull(kontonummer);
        
        
        kontonummer.setText("2");
	kontonummer.postActionEvent();  // type in a test message + ENTER
	
        vorname.setText(result);
        vorname.postActionEvent();
        
        nachname.setText(result);
        nachname.postActionEvent();
                
        sleep(1500);
	SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
		speichern.doClick();
            
            }
	});
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
		neu.doClick();
            
            }
	});
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
		oeffnen.doClick();
            
            }
	});
        sleep(1000);
        kontonummer.setText("2");
	kontonummer.postActionEvent();
        sleep(1500);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
		oeffnen.doClick();
            
            }
	});
        
        sleep(1000);
        assertEquals(result, vorname.getText()); 
        assertEquals(result, nachname.getText());
        
        
        
        
        
        
    }
    
    /*@Test
    public void testClear() throws InterruptedException {
        
    }*/
    
    @Test
    public void testTransfer() throws InterruptedException {
        
    }
    @Test
    public void testStory() throws InterruptedException {
        
    }
    

}
