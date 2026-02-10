package com.WOP.fileconverters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLSXConverter implements FileConverter {

  // Converts a .xslx file to .csv for internal use
  @Override
  public File convert(File file) throws IOException {
    String name = file.getName().substring(0, file.getName().lastIndexOf("."));
    File csvFile = File.createTempFile(name, ".csv");
    csvFile.deleteOnExit();

    try (Workbook workbook = new XSSFWorkbook(new FileInputStream(file));
        PrintWriter writer = new PrintWriter(csvFile)) {
      DataFormatter formatter = new DataFormatter();
      Sheet sheet = workbook.getSheetAt(0);

      for (Row row : sheet) {
        List<String> values = new ArrayList<>();
        for (Cell cell : row) {
          values.add(escapeCsv(formatter.formatCellValue(cell)));
        }
        writer.println(String.join(",", values));
      }
    }
    return csvFile;
  }

  // handle reserved characters
  private String escapeCsv(String value) {
    if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
      value = value.replace("\"", "\"\"");
      return "\"" + value + "\"";
    }
    return value;
  }
}
