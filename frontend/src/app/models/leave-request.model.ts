export enum LeaveType {
  CONGE_PAYE = 'CONGE_PAYE',
  RTT = 'RTT',
  CONGE_SANS_SOLDE = 'CONGE_SANS_SOLDE'
}

export interface CreateLeaveRequest {
  employeeId: number;
  leaveType: LeaveType;
  startDate: string;
  endDate: string;
}

export interface LeaveRequest extends CreateLeaveRequest {
  id: number;
  status: string;
}
