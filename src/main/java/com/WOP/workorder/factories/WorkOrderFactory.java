package com.WOP.workorder.factories;

import com.WOP.workorder.WorkOrder;
import com.WOP.workorder.config.SpreadsheetConfig;

public interface WorkOrderFactory {
  WorkOrder create(String[] row);

  static String removeWhitespace(String input) {
    if (input == null) return null;
    return input.strip().replaceAll("\\s+", " ");
  }

  // if any cell has the value "N/A", the system will successfully parse that cell into a work order
  // object
  // This allows users to force rows to process by substituting 'N/A' for troublesome values if
  // necessary
  static String getAcceptString() {
    return "N/A";
  }

  SpreadsheetConfig getConfig();
}
