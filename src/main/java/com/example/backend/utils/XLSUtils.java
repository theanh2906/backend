package com.example.backend.utils;

import com.example.backend.models.TableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;

public class XLSUtils {
    public static Workbook writeExcel(List<? extends TableModel> listObjects, Class<? extends TableModel> clazz) {
        try {
            Workbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet(clazz.getSimpleName());

            Row header = sheet.createRow(0);
            writeHeaderRow(header, clazz);

            CellStyle style = workbook.createCellStyle();

            listObjects.forEach(each -> {
                int curIndx = listObjects.indexOf(each);
                Row row = sheet.createRow(curIndx + 1);
                List<String> headers = each.getHeader();
                Map<String, Object> objectMap = each.getMap();
                headers.forEach(headerTitle -> {
                    int curHeaderIdx = headers.indexOf(headerTitle);
                    sheet.setColumnWidth(curHeaderIdx, 4000);
                    Cell cell = row.createCell(curHeaderIdx);
                    cell.setCellValue(String.valueOf(objectMap.get(headerTitle)));
                    cell.setCellStyle(style);
                });
            });
            return workbook;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeHeaderRow(Row headerRow, Class<? extends TableModel> clazz) throws Exception {
        List<String> headerList = clazz.getConstructor().newInstance().getHeader();
        headerList.forEach(each -> {
            headerRow.createCell(headerList.indexOf(each)).setCellValue(each);
        });
    }
}
