import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateLeaveRequest, LeaveRequest } from '../models/leave-request.model';

@Injectable({
  providedIn: 'root'
})
export class LeaveRequestService {
  private apiUrl = '/api/leave-requests';

  constructor(private http: HttpClient) { }

  createLeaveRequest(request: CreateLeaveRequest): Observable<LeaveRequest> {
    return this.http.post<LeaveRequest>(this.apiUrl, request);
  }
}
