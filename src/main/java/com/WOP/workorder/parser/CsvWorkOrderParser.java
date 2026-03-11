package com.WOP.workorder.parser;

import com.WOP.model.ModelException;
import com.WOP.workorder.WorkOrder;
import com.WOP.workorder.WorkOrderException;
import com.WOP.workorder.factories.WorkOrderFactory;
import com.opencsv.CSVReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class CsvWorkOrderParser implements Parser {
  private final String fileName;
  private final CSVReader reader;
  private final WorkOrderFactory factory;

  public CsvWorkOrderParser(String fileName, CSVReader reader, WorkOrderFactory factory) {
    this.fileName = fileName;
    this.reader = reader;
    this.factory = factory;
  }

  @Override
  public Iterator<WorkOrder> iterator() {
    return new CsvWorkOrderIterator();
  }

  @Override
  public void close() throws IOException {
    reader.close();
  }

  private class CsvWorkOrderIterator implements Iterator<WorkOrder> {
    private final Iterator<String[]> rowIterator;

    private CsvWorkOrderIterator() {
      this.rowIterator = reader.iterator();

      String[] headers = rowIterator.next();
      if (!Arrays.equals(headers, factory.getConfig().getHeaderNames()))
        throw new ModelException(
            String.format(
                "Column names do not fit expected format: check for hidden empty columns\nError occurred in %s",
                fileName));
    }

    @Override
    public boolean hasNext() {
      return rowIterator.hasNext();
    }

    // iterates through rows of the file, converting them to work order objects on the fly
    @Override
    public WorkOrder next() {
      String[] row = rowIterator.next();
      try {
        return factory.create(row);
      } catch (WorkOrderException e) {
        throw new WorkOrderException(
            String.format( // Add context
                "%s\nError occurred in %s\nRow: %s",
                e.getMessage(), fileName, Arrays.toString(row)));
      }
    }
  }
}
