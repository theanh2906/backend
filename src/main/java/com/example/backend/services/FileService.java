package com.example.backend.services;

import com.example.backend.utils.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public List<Map<String, Object>> parseJson(MultipartFile multipartFile) {
        List<Map<String, Object>> listDocuments = new ArrayList<>();
        try {
            if (multipartFile == null) {
                return new ArrayList<>();
            }
            File file = FileUtils.convertFile(multipartFile);
            if (file == null) {
                return new ArrayList<>();
            }
            InputStream inputStream = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            List<String> headers = new ArrayList<>();
            for (Cell cell : sheet.getRow(0)) {
                headers.add(cell.getStringCellValue());
            }
            sheet.shiftRows(1, sheet.getLastRowNum(), -1);
            for (Row row : sheet) {
                Map<String, Object> rowMap = new HashMap<>();
                for (String header : headers) {
                    rowMap.put(header, row.getCell(headers.indexOf(header)).getStringCellValue());
                }
                listDocuments.add(rowMap);
            }

        } catch (IOException e) {
            LOG.error(e.getLocalizedMessage());
        }
        return listDocuments;
    }
}
