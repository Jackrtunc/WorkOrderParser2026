package com.WOP.workorder.storage;

import com.WOP.workorder.WorkOrder;

public interface WorkOrderStorage extends Iterable<WorkOrder> {
  String add(WorkOrder workOrder);
}
