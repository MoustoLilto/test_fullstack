import { Component, OnInit } from '@angular/core';
import { Employee, Category } from '../../models/employee.model';
import { EmployeeService } from '../../services/employee.service';

@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.scss']
})
export class EmployeeListComponent implements OnInit {
  cadreEmployees: Employee[] = [];
  nonCadreEmployees: Employee[] = [];
  activeCategory: Category | 'ALL' = 'ALL';
  loading = false;
  error = '';
  Category = Category;

  constructor(private employeeService: EmployeeService) { }

  ngOnInit(): void {
    this.loadAllEmployees();
  }

  loadAllEmployees(): void {
    this.loading = true;
    this.activeCategory = 'ALL';
    this.employeeService.getAllEmployees().subscribe({
      next: (employees) => {
        this.filterEmployees(employees);
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des employés';
        this.loading = false;
        console.error(err);
      }
    });
  }

  loadEmployeesByCategory(category: Category): void {
    this.loading = true;
    this.activeCategory = category;
    this.employeeService.getEmployeesByCategory(category).subscribe({
      next: (employees) => {
        if (category === Category.CADRE) {
          this.cadreEmployees = employees;
          this.nonCadreEmployees = [];
        } else {
          this.cadreEmployees = [];
          this.nonCadreEmployees = employees;
        }
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des employés';
        this.loading = false;
        console.error(err);
      }
    });
  }

  private filterEmployees(employees: Employee[]): void {
    this.cadreEmployees = employees.filter(employee => employee.category === Category.CADRE);
    this.nonCadreEmployees = employees.filter(employee => employee.category === Category.NON_CADRE);
  }
}
