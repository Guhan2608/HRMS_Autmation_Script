package Utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Excel {
    private static Workbook workbook; //create instance for workbook
    private static Sheet sheet;// create instance for Sheet

    // Load the Excel file
    public static void loadExcel(String fileName, String sheetName) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);//get the sheet name

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches the value of a cell from an Excel sheet.
     *
     * @param rowNum
     *        The row number of the Excel sheet.
     * @param colNum
     *        The column number of the Excel sheet.
     * @return
     *        The value present in the specified cell.
     */

    public static String cellValue(int rowNum, int colNum) {
        Row row = sheet.getRow(rowNum);
        if (row == null) return null;
        Cell cell = row.getCell(colNum);
        if (cell == null) return null;
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
    }

    /**
     * In loaded excel
     *
     * @return Number of Rows present in the Column
     */

    public static int getRowCount() {
        return sheet.getPhysicalNumberOfRows();
    }


    /**
     * Get number of columns in a specific row
     *
     * @param rowNum - row index (0-based)
     * @return column count
     */
    public static int getColCount(int rowNum) {
        Row row = sheet.getRow(rowNum);
        return (row != null) ? row.getPhysicalNumberOfCells() : 0;
    }

    /**
     * Used to close the Workbook.
     *
     * @throws IOException if close fails
     *
     */

    public static void closeExcel() {
        if (workbook != null) {
            try {
                workbook.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Writes/updates a value into the Excel sheet.
     *
     * @param fileName - Path of the Excel file
     * @param rowNum   - Row index (0-based)
     * @param colNum   - Column index (0-based)
     * @param value    - Value to be written
     */
    public static void setCellValue(String fileName, int rowNum, int colNum, String value) {
        try {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                row = sheet.createRow(rowNum); // create row if it doesn't exist
            }

            Cell cell = row.getCell(colNum);
            if (cell == null) {
                cell = row.createCell(colNum); // create cell if it doesn't exist
            }else {
                cell.setBlank();
            }

            cell.setCellValue(value);

            // Write back to the file
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                workbook.write(fos);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void saveExcel(String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            workbook.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}