package Utils;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static void writeExcel(String filePath, String sheetName, List<Map<String, String>> dataList) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);

            if (dataList.isEmpty()) return;

            // Create header row
            Row header = sheet.createRow(0);
            List<String> keys = new ArrayList<>(dataList.get(0).keySet());
            for (int i = 0; i < keys.size(); i++) {
                header.createCell(i).setCellValue(keys.get(i));
            }

            // Fill data rows
            for (int i = 0; i < dataList.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Map<String, String> rowData = dataList.get(i);
                for (int j = 0; j < keys.size(); j++) {
                    row.createCell(j).setCellValue(rowData.getOrDefault(keys.get(j), ""));
                }
            }

            // ✅ Add timestamp to avoid duplicate filenames
            String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            String resultFilePath = filePath.replace(".xlsx", "_Result_" + timestamp + ".xlsx");

            try (FileOutputStream out = new FileOutputStream(resultFilePath)) {
                workbook.write(out);
            }

            System.out.println("📊 Results written to Excel: " + resultFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
