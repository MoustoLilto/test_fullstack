export enum Category {
  CADRE = 'CADRE',
  NON_CADRE = 'NON_CADRE'
}

export interface Employee {
  id: number;
  lastName: string;
  firstName: string;
  category: string;
  hireDate: string;
  leaveBalance: number;
  rttBalance?: number;
}
