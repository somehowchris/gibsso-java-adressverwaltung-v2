/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung.utils;

import adressverwaltung.enums.FileTypeEnum;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.io.FilenameUtils;

/**
 * Helper class to check if files are supported by this application
 *
 * @author Christof Weickhardt
 */
public class FileTypeFilter extends FileFilter {

    /**
     * FileType from custom enum of supported data types
     *
     * @see FileTypeEnum
     */
    FileTypeEnum fileType;

    /**
     * Constructor given a custom supported file type
     *
     * @param fileType Representive file type
     */
    public FileTypeFilter(FileTypeEnum fileType) {
        this.fileType = fileType;
    }

    /**
     * Custom check if file would be accepted or not
     *
     * @param f File to check
     * @return Returns if the file would have been accepted
     */
    @Override
    public boolean accept(File f) {
        return FilenameUtils.getExtension(f.getName()).equalsIgnoreCase(fileType.getExtension().replace(".", ""));
    }

    /**
     * Custom getter of the file type description
     *
     * @return Returns the description of the given file type
     */
    @Override
    public String getDescription() {
        return fileType.getDescription() + " file (*" + getExtension() + ")";
    }

    /**
     * Getter to get the file extension of the file type
     *
     * @return e.g. .json, .xslx
     */
    public String getExtension() {
        return fileType.getExtension();
    }

    /**
     * Getter of the set filetype
     *
     * @return Returns set FileTypeEnum
     */
    public FileTypeEnum getFileType() {
        return fileType;
    }

}
