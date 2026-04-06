package com.WOP.workorder;

import com.WOP.workorder.factories.WorkOrderFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Comparator;

public class DefaultWorkOrder implements WorkOrder {
  private final int referenceNumber;
  private final int workOrderNumber;
  private final String reportingParty;
  private final String dayRequested;
  private final Status status;
  private final String building;
  private final String location;
  private final String description;
  private final String notes;

  public DefaultWorkOrder(
      int referenceNumber,
      int workOrderNumber,
      String reportingParty,
      String dayRequested,
      Status status,
      String building,
      String location,
      String description,
      String notes) {

    if (referenceNumber < 0)
      throw new WorkOrderException("Invalid reference number (reference number must be positive)");
    if (workOrderNumber < 0)
      throw new WorkOrderException("Invalid work order number (work order number must be positive)");
    if (reportingParty.isEmpty())
      throw new WorkOrderException("Invalid reporting party (reporting party must not be empty)");
    if (isInvalidDate(dayRequested))
      throw new WorkOrderException("Invalid day requested (date must be mm/dd/yyyy)");
    if (status == Status.UNKNOWN || status == null)
      throw new WorkOrderException("Invalid status (unknown status code)");
    if (building.isEmpty())
      throw new WorkOrderException("Invalid building (building cell must not be empty)");
    if (location.isEmpty())
      throw new WorkOrderException("Invalid location (location cell must not be empty)");
    if (description.isEmpty())
      throw new WorkOrderException("Invalid description (description cell must not be empty)");

    this.referenceNumber = referenceNumber;
    this.workOrderNumber = workOrderNumber;
    this.reportingParty = reportingParty;
    this.dayRequested = dayRequested;
    this.building = building;
    this.location = location;
    this.description = description;
    this.notes = notes;
    this.status = status;
  }

  @Override
  public int getReferenceNumber() {
    return referenceNumber;
  }

  @Override
  public int getWorkOrderNumber() {
    return workOrderNumber;
  }

  @Override
  public String getReportingParty() {
    return reportingParty;
  }

  @Override
  public String getDayRequested() {
    return dayRequested;
  }

  @Override
  public String getBuilding() {
    return building;
  }

  @Override
  public String getLocation() {
    return location;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public Status getStatus() {
    return status;
  }

  @Override
  public String getNotes() {
    return notes;
  }

  @Override
  public int compareTo(WorkOrder o) {
    return getReferenceNumber() - o.getReferenceNumber();
  }

  @Override
  public String[] toArray() {
    return new String[] {
      referenceNumber == 0
          ? "N/A"
          : String.valueOf(referenceNumber), // 0 represents the special accepted value 'N/A'
      workOrderNumber == 0 ? "N/A" : String.valueOf(workOrderNumber),
      reportingParty,
      dayRequested,
      status.toString(),
      building,
      location,
      description,
      notes
    };
  }

  @Override
  public String toString() {
    return String.join(", ", this.toArray());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || this.getClass() != obj.getClass()) {
      return false;
    }

    DefaultWorkOrder entry = (DefaultWorkOrder) obj;
    // equivalence based only on reference number
    return this.getReferenceNumber() == entry.getReferenceNumber();
  }

  private boolean isInvalidDate(String date) {
    if (date.equals(WorkOrderFactory.getAcceptString()))
      return false; // If the date is 'N/A' accept it

    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("M/d/yyyy");
    try {
      LocalDate.parse(date, formatter);
      return false;
    } catch (DateTimeParseException e) {
      System.out.println(date);
      return true;
    }
  }
}
