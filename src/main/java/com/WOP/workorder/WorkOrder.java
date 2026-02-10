package com.WOP.workorder;

public interface WorkOrder extends Comparable<WorkOrder> {

  int getIdentifier();

  String getNotes();

  Status getStatus();

  int compareTo(WorkOrder workOrder);

  String toString();

  String[] toArray();

  WorkOrder merge(WorkOrder other);
}
