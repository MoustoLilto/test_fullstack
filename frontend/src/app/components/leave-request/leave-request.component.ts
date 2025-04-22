import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Employee } from '../../models/employee.model';
import { LeaveType } from '../../models/leave-request.model';
import { EmployeeService } from '../../services/employee.service';
import { LeaveRequestService } from '../../services/leave-request.service';

@Component({
  selector: 'app-leave-request',
  templateUrl: './leave-request.component.html',
  styleUrls: ['./leave-request.component.scss']
})
export class LeaveRequestComponent implements OnInit {
  leaveForm!: FormGroup;
  employee?: Employee;
  loading = false;
  submitting = false;
  error = '';
  success = '';
  leaveTypes = Object.values(LeaveType);

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private employeeService: EmployeeService,
    private leaveRequestService: LeaveRequestService
  ) { }

  ngOnInit(): void {
    this.initForm();
    this.loading = true;

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadEmployee(+id);
    } else {
      this.error = 'ID d\'employé non trouvé';
      this.loading = false;
    }
  }

  initForm(): void {
    this.leaveForm = this.fb.group({
      leaveType: [LeaveType.CONGE_PAYE, Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required]
    });
  }

  loadEmployee(id: number): void {
    this.employeeService.getEmployeeById(id).subscribe({
      next: (employee) => {
        this.employee = employee;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des détails de l\'employé';
        this.loading = false;
        console.error(err);
      }
    });
  }

  onSubmit(): void {
    if (this.leaveForm.invalid || !this.employee) {
      return;
    }

    // Vérifier si l'employé non-cadre essaie de prendre des RTT
    if (this.employee.category === 'NON_CADRE' && this.leaveForm.value.leaveType === LeaveType.RTT) {
      this.error = 'Les employés non-cadres ne peuvent pas prendre de RTT';
      return;
    }

    this.submitting = true;
    this.error = '';
    this.success = '';

    const request = {
      employeeId: this.employee.id,
      leaveType: this.leaveForm.value.leaveType,
      startDate: this.leaveForm.value.startDate,
      endDate: this.leaveForm.value.endDate
    };

    this.leaveRequestService.createLeaveRequest(request).subscribe({
      next: (response) => {
        this.submitting = false;
        this.success = 'Demande de congé créée avec succès';
        this.leaveForm.reset({
          leaveType: LeaveType.CONGE_PAYE
        });
      },
      error: (err) => {
        this.submitting = false;
        if (err.error && err.error.message) {
          this.error = err.error.message;
        } else {
          this.error = 'Erreur lors de la création de la demande de congé';
        }
        console.error(err);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/employees', this.employee?.id]);
  }
}
