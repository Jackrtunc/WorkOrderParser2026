package com.WOP.workorder.factories;

import com.WOP.workorder.*;
import com.WOP.workorder.config.FacilitiesConfig;
import java.util.Arrays;

public class FacilitiesWorkOrderFactory implements WorkOrderFactory {
  private final FacilitiesConfig config;

  public FacilitiesWorkOrderFactory(FacilitiesConfig config) {
    this.config = config;
  }

  // Converts rows from a facilities upload to work order objects
  public WorkOrder create(String[] row) {
    String acceptString = WorkOrderFactory.getAcceptString();
    row = Arrays.stream(row).map(WorkOrderFactory::removeWhitespace).toArray(String[]::new);

    try {
      int referenceNumber;
      try {
        referenceNumber =
            row[config.referenceNumber().offset()].equals(acceptString)
                ? 0
                : Integer.parseInt(row[config.referenceNumber().offset()]);
      } catch (NumberFormatException e) {
        throw new WorkOrderException("Invalid reference number (reference number must be an integer)");
      }

      Status workOrderStatus = Status.fromString(row[config.workOrderStatus().offset()]);

      int workOrderNumber;
      try {
        workOrderNumber =
            row[config.workOrderNumber().offset()].equals(acceptString)
                    || workOrderStatus == Status.CANCELED
                ? 0
                : Integer.parseInt(row[config.workOrderNumber().offset()]);
      } catch (NumberFormatException e) {
        throw new WorkOrderException("Invalid work order number (work order number must be an integer)");
      }

      // parse location from description
      String building = row[config.building().offset()];
      String location =
          row[config.description().offset()].equals(
                  acceptString) // If the description is 'N/A', make the location 'N/A' also
              ? acceptString
              : parseLocation(row);
      String contactEmailAddress = row[config.contactEmailAddress().offset()];
      String dateRequestEntered = row[config.dateRequestEntered().offset()];
      String description = row[config.description().offset()];

      return new DefaultWorkOrder(
          referenceNumber,
          workOrderNumber,
          contactEmailAddress,
          dateRequestEntered,
          workOrderStatus,
          building,
          location,
          description,
          null // facilities uploads don't have notes
          );
    } catch (IndexOutOfBoundsException e) {
      throw new WorkOrderException(
          "Row doesn't fit expected format (trailing columns might be empty)");
    }
  }

  // joins building and room number (found in description) into a single location column
  private String parseLocation(String[] row) {
    String description = (row[config.description().offset()]);
    StringBuilder location = new StringBuilder();

    final String LOCATION_TAG = "Location: ";
    final String DEPARTMENT_TAG = "Department: ";

    // room number appears after "Location: "
    int locationStartIndex = description.lastIndexOf(LOCATION_TAG);

    if (locationStartIndex == -1) {
      location.append("N/A"); // if "Location: " doesn't appear in the description

    } else {
      locationStartIndex += LOCATION_TAG.length();

      int locationEndIndex =
          description.indexOf(
              DEPARTMENT_TAG, locationStartIndex); // room number appears before "Department:"

      if (locationEndIndex == -1) {
        // if the description doesn't fully contain "Department: " just read until the end of the
        // description
        locationEndIndex = description.length();
      }

      location.append(description.substring(locationStartIndex, locationEndIndex).trim());
    }

    return location.toString();
  }

  public FacilitiesConfig getConfig() {
    return config;
  }
}
