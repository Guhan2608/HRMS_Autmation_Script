package Utils;



import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.util.*;

public class ExcelUtil {

    public static List<Map<String, String>> readExcel(String filePath, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) return dataList;

            int lastRowNum = sheet.getLastRowNum();
            int totalCells = headerRow.getLastCellNum();

            for (int i = 1; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue; // skip blank row

                Map<String, String> rowData = new HashMap<>();
                for (int j = 0; j < totalCells; j++) {
                    Cell headerCell = headerRow.getCell(j);
                    Cell valueCell = row.getCell(j);

                    if (headerCell == null) continue;

                    String header = headerCell.getStringCellValue();
                    String value = (valueCell == null) ? "" : valueCell.toString().trim();

                    rowData.put(header, value);
                }

                if (!rowData.isEmpty())
                    dataList.add(rowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

}
