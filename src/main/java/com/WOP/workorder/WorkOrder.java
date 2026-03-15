package com.WOP.workorder;

public interface WorkOrder extends Comparable<WorkOrder> {

  int getReferenceNumber();

  int getWorkOrderNumber();

  String getReportingParty();

  String getDayRequested();

  String getBuilding();

  String getLocation();

  String getDescription();

  Status getStatus();

  String getNotes();

  int compareTo(WorkOrder workOrder);

  String toString();

  String[] toArray();
}
