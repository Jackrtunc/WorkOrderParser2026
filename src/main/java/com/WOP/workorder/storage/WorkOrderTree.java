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
  public void add(WorkOrder workOrder) {
    if (workOrders.containsKey(workOrder.getIdentifier())) {

      // If a duplicate work order is added, merge it with the existing work order in the tree
      // This handles updating changes in work order status
      WorkOrder merged = workOrder.merge(workOrders.get(workOrder.getIdentifier()));
      workOrders.put(merged.getIdentifier(), merged);

    } else {
      workOrders.put(workOrder.getIdentifier(), workOrder);
    }
  }

  @Override
  public Iterator<WorkOrder> iterator() {
    return workOrders.descendingMap().values().iterator();
  }
}
