package com.WOP.workorder.storage;

import com.WOP.workorder.WorkOrder;

public interface WorkOrderStorage extends Iterable<WorkOrder> {
  void add(WorkOrder workOrder);
}
