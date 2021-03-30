package com.example.demo.Helper;

import com.example.demo.Model.KPIDataInput;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    // read file excel and save it into object
    public static List<KPIDataInput> excelToDataObject(InputStream inputStream) {
        try {
            // create new workbook base on excel file input
            Workbook workbook = new XSSFWorkbook(inputStream);
            // read the first sheet
            Sheet sheet = workbook.getSheetAt(0);
            // loop through the rows
            Iterator<Row> rowIterator = sheet.rowIterator();
            // list data test
            List<KPIDataInput> list = new ArrayList<>();
            // number of rows
            int rowNumber = 0;
            // number of non empty rows
            int notNullRowCount = 0;
            // count number of non empty rows
            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
                        if (cell.getCellType() != Cell.CELL_TYPE_STRING ||
                                cell.getStringCellValue().length() > 0) {
                            notNullRowCount++;
                            // recognize empty row, break
                            break;
                        }
                    }
                }
            }
            // loop through rows
            while (rowIterator.hasNext()) {
                rowNumber += 1;
                // loop till the last non empty row, break
                if(rowNumber == (notNullRowCount+1)) {
                    break;
                }
                Row currentRow = rowIterator.next();
                if(currentRow.getRowNum() == 0){
                    continue;
                }
                // loop through the cell
                KPIDataInput dataTest = new KPIDataInput();
                dataTest.setKpi_id(currentRow.getCell(1).getStringCellValue());
                dataTest.setValue((long) currentRow.getCell(3).getNumericCellValue());
                list.add(dataTest);
                System.out.println(dataTest.toString());
            }
            workbook.close();
            return list;
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse Excel file: " + e.getMessage());
        }
    }

}
