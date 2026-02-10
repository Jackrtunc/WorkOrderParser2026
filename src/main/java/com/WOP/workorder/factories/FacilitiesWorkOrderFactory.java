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
        throw new WorkOrderException("Invalid reference number: input must be a number");
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
        throw new WorkOrderException("Invalid work order number: input must be a number");
      }

      // parse location from description
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
          location,
          description,
          null // facilities uploads don't have notes
          );
    } catch (IndexOutOfBoundsException e) {
      throw new WorkOrderException(
          "Row doesn't fit expected format: trailing fields might be empty");
    }
  }

  // joins building and room number (found in description) into a single location column
  private String parseLocation(String[] row) {
    String description = (row[config.description().offset()]);

    StringBuilder location = new StringBuilder((row[config.building().offset()]));
    location.append(" ");

    final String LOCATION_TAG = "Location: ";
    final String DEPARTMENT_TAG = "Department: ";

    // room number appears after "Location: "
    int roomNumberStart = description.lastIndexOf(LOCATION_TAG);

    if (roomNumberStart == -1) {
      location.append("???"); // if "Location: " doesn't appear in the description

    } else {
      roomNumberStart += LOCATION_TAG.length();

      int roomNumberEnd =
          description.indexOf(
              DEPARTMENT_TAG, roomNumberStart); // room number appears before "Department: "

      if (roomNumberEnd == -1) {
        // if the description doesn't fully contain "Department: " just read until the end of the
        // description
        roomNumberEnd = description.length();
      }

      location.append(description.substring(roomNumberStart, roomNumberEnd).trim());
    }

    return location.toString();
  }

  public FacilitiesConfig getConfig() {
    return config;
  }
}
