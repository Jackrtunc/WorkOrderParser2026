package com.WOP.workorder.storage;

import com.WOP.workorder.WorkOrder;
import java.util.Iterator;
import java.util.TreeMap;

public class WorkOrderTree implements WorkOrderStorage {
  private final TreeMap<Integer, WorkOrder>
      workOrders; // Keys are reference numbers, values are corresponding work order objects

  public WorkOrderTree() {
    workOrders = new TreeMap<>();
  }

  @Override
  public String add(WorkOrder added) {
    if (workOrders.containsKey(added.getIdentifier())) {
      // If a duplicate work order is added, merge it with the existing work order in the tree
      // This handles updating changes in work order status, notes, etc.
      WorkOrder existing = workOrders.get(added.getIdentifier());
      WorkOrder updated = added.merge(existing);
      workOrders.put(updated.getIdentifier(), updated);
      return String.format("Updated existing work order: %s", updated);
    }

    workOrders.put(added.getIdentifier(), added);
    return String.format("Added new work order: %s", added);
  }

  @Override
  public Iterator<WorkOrder> iterator() {
    return workOrders.descendingMap().values().iterator();
  }
}
