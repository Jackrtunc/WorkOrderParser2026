package com.WOP.workorder.storage;

import com.WOP.workorder.DefaultWorkOrder;
import com.WOP.workorder.Status;
import com.WOP.workorder.WorkOrder;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

public class WorkOrderTree implements WorkOrderStorage {
  private final TreeMap<Integer, WorkOrder>
      workOrders; // Keys are reference numbers, values are corresponding work order objects

  public WorkOrderTree() {
    workOrders = new TreeMap<>();
  }

  @Override
  public String add(WorkOrder adding) {
    if (workOrders.containsKey(adding.getReferenceNumber())) {
      // If a duplicate work order is added, update the existing work order with any new information
      // This handles changes in work order status, notes, etc.
      WorkOrder existing = workOrders.get(adding.getReferenceNumber());
      WorkOrder updated = updateWorkOrder(existing, adding);
      workOrders.put(updated.getReferenceNumber(), updated);
      return workOrderDifference(existing, updated);
    }

    workOrders.put(adding.getReferenceNumber(), adding);
    return String.format("Added new work order: %d", adding.getReferenceNumber());
  }

  @Override
  public Iterator<WorkOrder> iterator() {
    return workOrders.descendingMap().values().iterator();
  }

  private WorkOrder updateWorkOrder(WorkOrder existing, WorkOrder adding) {
    Comparator<Status> statusComparator = Comparator.comparingInt(Status::getPrecedence);
    Status status =
        statusComparator.compare(existing.getStatus(), adding.getStatus()) < 0
            ? adding.getStatus()
            : existing.getStatus();

    return new DefaultWorkOrder(
        existing.getReferenceNumber(),
        adding.getWorkOrderNumber(),
        existing.getReportingParty(),
        existing.getDayRequested(),
        status,
        existing.getBuilding(),
        existing.getLocation(),
        existing.getDescription(),
        existing.getNotes());
  }

  private String workOrderDifference(WorkOrder existing, WorkOrder updated) {
    StringBuilder difference = new StringBuilder();

    if (updated.getStatus() != existing.getStatus()) {
      difference.append(
          String.format(
              "Work order %d status updated: %s -> %s",
              existing.getReferenceNumber(),
              existing.getStatus().toString(),
              updated.getStatus().toString()));
    }

    if (updated.getWorkOrderNumber() != existing.getWorkOrderNumber()) {
      difference.append('\n');
      difference.append(
          String.format(
              "Work order %d work order number updated: %d -> %d",
              existing.getReferenceNumber(),
              existing.getWorkOrderNumber(),
              updated.getWorkOrderNumber()));
    }

    return difference.toString();
  }
}
