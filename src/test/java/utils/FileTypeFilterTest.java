/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import adressverwaltung.enums.FileTypeEnum;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import adressverwaltung.utils.FileTypeFilter;
import java.io.File;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 *
 * @author Christof Weickhardt
 */
public class FileTypeFilterTest {

    HashMap<FileTypeFilter, FileTypeEnum> fileTypes;

    /**
     * Empty Constructor
     */
    public FileTypeFilterTest() {

    }

    /**
     * Setup class
     */
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     * Tear down class
     */
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Setup before test
     */
    @Before
    public void setUp() {
        fileTypes = new HashMap<>();
        for (FileTypeEnum fileType : FileTypeEnum.values()) {
            fileTypes.put(new FileTypeFilter(fileType), fileType);
        }
    }

    /**
     * Tear down after test
     */
    @After
    public void tearDown() {
    }

    /**
     * Checking on descriptions
     */
    @Test
    public void descriptionExists() {
        fileTypes.keySet().forEach((key) -> {
            assertNotNull(key.getDescription());
        });
    }

    /**
     * Checking on extensions
     */
    @Test
    public void extensionsCorrect() {
        fileTypes.entrySet().forEach((entry) -> {
            assertEquals(entry.getKey().getExtension(), entry.getValue().getExtension());
        });
    }

    /**
     * CHecking on File Type returns
     */
    @Test
    public void returnsCorrectFileType() {
        fileTypes.entrySet().forEach((entry) -> {
            assertEquals(entry.getKey().getFileType(), entry.getValue());
        });
    }

    /**
     * Checking on file type acceptnions
     */
    @Test
    public void acceptsFileTypes() {
        fileTypes.entrySet().forEach((entry) -> {
            File f = new File("hello" + entry.getKey().getExtension());
            assertEquals(entry.getKey().accept(f), true);
        });
    }

    /**
     * Checking on file type rejections
     */
    @Test
    public void rejectsWrongFileTypes() {
        fileTypes.entrySet().forEach((entry) -> {
            File f = new File("None.OfYourBusiness");
            assertEquals(entry.getKey().accept(f), false);
        });
    }

}
