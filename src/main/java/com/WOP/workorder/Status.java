package com.WOP.workorder;

import com.WOP.workorder.factories.WorkOrderFactory;

public enum Status implements Comparable<Status> {
  ENTERED("00-ENTERED", 0),
  PENDING_APPROVAL("Pending Approval", 1),
  OPEN("00-OPEN", 2),
  RESERV("00-RESERV", 3),
  ADMIN("01-ADMIN", 3),
  ROUTED("10-ROUTED", 3),
  FUNDED("38-FUNDED", 4),
  IN_PROGRESS("50-IN PROG", 5),
  ON_HOLD("95-ON HOLD", 5),
  WORK_COMPLETED("60-WKCOMP", 6),
  REOPENED("62-REOPEN", 6),
  RESOLVED("65-RESOLVED", 6),
  CLOSED("65-CLOSED", 7),
  CANCEL("96-CANCEL", 7),
  CANCELED("Canceled", 7),
  UNKNOWN("Unknown", 8),
  FORCE_ACCEPT(WorkOrderFactory.getAcceptString(), 9); // If the status is 'N/A', accept it

  // Statuses with lower values should always appear before statuses with higher values in
  // the life cycle of a work order (these numbers were chosen somewhat arbitrarily as I
  // don't know exactly what each status means or if a work order could revert to a lower status)

  private final String name;
  private final int precedence;

  Status(String name, int precedence) {
    this.name = name;
    this.precedence = precedence;
  }

  public static Status fromString(String name) {
    for (Status status : Status.values()) {
      if (status.name.equals(name)) {
        return status;
      }
    }
    return UNKNOWN;
  }

  public int getPrecedence() {
    return this.precedence;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
