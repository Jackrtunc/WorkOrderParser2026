package com.WOP.workorder.factories;

import com.WOP.workorder.*;
import com.WOP.workorder.config.DepartmentConfig;
import java.util.Arrays;

public class DepartmentWorkOrderFactory implements WorkOrderFactory {
  private final DepartmentConfig
      config; // Config specifies the row offsets different fields are found at

  public DepartmentWorkOrderFactory(DepartmentConfig config) {
    this.config = config;
  }

  // Converts rows from a department upload to work order objects
  public WorkOrder create(String[] row) {
    String acceptString = WorkOrderFactory.getAcceptString();
    // Remove whitespace from all cells
    row = Arrays.stream(row).map(WorkOrderFactory::removeWhitespace).toArray(String[]::new);

    try {
      int referenceNumber;
      try {
        referenceNumber =
            row[config.referenceNumber().offset()].equals(
                    acceptString) // If the cell is 'N/A' rather than a number, allow it
                ? 0 // Special allowed value
                : Integer.parseInt(row[config.referenceNumber().offset()]);
      } catch (NumberFormatException e) {
        throw new WorkOrderException("Invalid reference number: input must be a number");
      }

      Status status = Status.fromString(row[config.status().offset()]);

      int workOrderNumber;
      try {
        workOrderNumber =
            row[config.workOrderNumber().offset()].equals(acceptString)
                    || status
                        == Status.CANCELED // Canceled work orders don't have a work order number
                ? 0 // Special allowed value
                : Integer.parseInt(row[config.workOrderNumber().offset()]);
      } catch (NumberFormatException e) {
        throw new WorkOrderException("Invalid work order number: input must be a number");
      }

      String reportingParty = row[config.reportingParty().offset()];
      String dateRequested = row[config.dateRequested().offset()];
      String location = row[config.location().offset()];
      String description = row[config.description().offset()];
      String notes = row[config.notes().offset()];

      return new DefaultWorkOrder(
          referenceNumber,
          workOrderNumber,
          reportingParty,
          dateRequested,
          status,
          location,
          description,
          notes);

    } catch (IndexOutOfBoundsException e) {
      throw new WorkOrderException(
          "Row doesn't fit expected format: columns might be empty");
    }
  }

  public DepartmentConfig getConfig() {
    return config;
  }
}
