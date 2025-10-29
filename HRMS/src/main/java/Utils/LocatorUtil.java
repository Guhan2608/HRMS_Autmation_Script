package Utils;


import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.util.*;

public class LocatorUtil {
    public static class LocatorInfo {
        public String tabName;
        public String locatorType;
        public String locatorValue;
        public String fieldType;
    }

    public static Map<String, LocatorInfo> getAllLocators(String path) {
        Map<String, LocatorInfo> locatorMap = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(path)) {
            Workbook wb = WorkbookFactory.create(fis);
            Sheet sheet = wb.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                LocatorInfo info = new LocatorInfo();
                String fieldName = row.getCell(0).getStringCellValue().trim();
                info.tabName = row.getCell(1).getStringCellValue().trim();
                info.locatorType = row.getCell(2).getStringCellValue().trim();
                info.locatorValue = row.getCell(3).getStringCellValue().trim();
                info.fieldType = row.getCell(4).getStringCellValue().trim();
                locatorMap.put(fieldName, info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locatorMap;
    }
}

