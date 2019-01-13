/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressverwaltung.exports;

import adressverwaltung.models.Town;
import adressverwaltung.models.Person;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import adressverwaltung.services.Service;

/**
 *
 * @author Christof Weickhardt
 */
public class ExcelExport extends Export {

    // Create a Workbook
    Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

    /**
     * Constructor to export a list tf people
     *
     * @param connection Connection to get related data
     * @param people People to export
     */
    public ExcelExport(Service connection, List<Person> people) {
        super(connection, people);
    }

    /**
     * Constructor to export all
     *
     * @param connection Connection to get all the data
     */
    public ExcelExport(Service connection) {
        super(connection);
    }

    /**
     * Constructor to export from given data set
     *
     * @param connection Connection to get needed files
     * @param people People to export
     * @param towns Towns to export
     */
    public ExcelExport(Service connection, List<Person> people, List<Town> towns) {
        super(connection, people, towns);
    }

    /**
     * Custom excel render function
     */
    @Override
    public void render() {
        workbook = getTownDataSheet(workbook);
        workbook = getPeopleDataSheet(workbook);
    }

    /**
     * Custom excel write function
     */
    @Override
    public void write() {
        File f = new File(this.path);
        if (f.exists()) {
            f.delete();
        }
        try {
            //Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(f);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Couldnt write file: " + this.path);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Couldnt write file: " + this.path);
        }

    }

    /**
     * Write all the town data
     *
     * @param workbook Workbook to write on
     * @return Return to written workbook
     */
    public Workbook getTownDataSheet(Workbook workbook) {
        // Create a Sheet
        Sheet sheet = workbook.createSheet("Towns");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.DARK_BLUE.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for (int i = 0; i < this.townColumns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(townColumns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create Other rows and cells with employees data
        int rowNum = 1;
        for (Town o : this.towns) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(o.getTid());
            row.createCell(1).setCellValue(o.getName() != null ? o.getName() : "");
            row.createCell(2).setCellValue(!"".equals(o.getPlz() + "") ? o.getPlz() + "" : "");
        }
        // Resize all columns to fit the content size
        for (int i = 0; i < townColumns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        return workbook;
    }

    /**
     * Write all the people data
     *
     * @param workbook Workbook to write on
     * @return Return to written workbook
     */
    public Workbook getPeopleDataSheet(Workbook workbook) {
        // Create a Sheet
        Sheet sheet = workbook.createSheet("People");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.DARK_BLUE.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for (int i = 0; i < personColumns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(personColumns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create Other rows and cells with employees data
        int rowNum = 1;

        for (Person p : this.people) {
            Town o = null;
            if (p.getOid() != null) {
                o = this.connection.getTown(p.getOid());
            }

            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(p.getId());
            row.createCell(1).setCellValue(p.getLastName() != null ? p.getLastName() : "");
            row.createCell(2).setCellValue(p.getFirstName() != null ? p.getFirstName() : "");
            row.createCell(3).setCellValue(p.getAddress() != null ? p.getAddress() : "");
            row.createCell(4).setCellValue(o != null ? o.getName() : "");
            row.createCell(5).setCellValue(o != null ? o.getPlz() + "" : "");
            row.createCell(6).setCellValue(p.getPhone() != null ? p.getPhone() : "");
            row.createCell(7).setCellValue(p.getMobile() != null ? p.getMobile() : "");
            row.createCell(7).setCellValue(p.getEmail() != null ? p.getEmail() : "");
        }
        // Resize all columns to fit the content size
        for (int i = 0; i < personColumns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        return workbook;
    }
}
