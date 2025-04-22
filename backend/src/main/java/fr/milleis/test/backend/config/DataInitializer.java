package fr.milleis.test.backend.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.milleis.test.backend.models.Category;
import fr.milleis.test.backend.models.Employee;
import fr.milleis.test.backend.models.LeaveRequest;
import fr.milleis.test.backend.models.LeaveStatus;
import fr.milleis.test.backend.models.LeaveType;
import fr.milleis.test.backend.repositories.EmployeeRepository;
import fr.milleis.test.backend.repositories.LeaveRequestRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(EmployeeRepository employeeRepository, 
                                      LeaveRequestRepository leaveRequestRepository) {
        return args -> {
            // Vider les données existantes
            leaveRequestRepository.deleteAll();
            employeeRepository.deleteAll();
            
            // Créer quelques employés
            Employee emp1 = new Employee();
            emp1.setFirstName("Jean");
            emp1.setLastName("Dupont");
            emp1.setCategory(Category.CADRE);
            emp1.setHireDate("2020-01-15");
            emp1.setLeaveBalance(25.0);
            emp1.setRttBalance(12.0);
            employeeRepository.save(emp1);
            
            Employee emp2 = new Employee();
            emp2.setFirstName("Marie");
            emp2.setLastName("Martin");
            emp2.setCategory(Category.NON_CADRE);
            emp2.setHireDate("2021-03-10");
            emp2.setLeaveBalance(25.0);
            emp2.setRttBalance(10.0);
            employeeRepository.save(emp2);
            
            // Créer quelques demandes de congés
            LeaveRequest leave1 = new LeaveRequest();
            leave1.setEmployee(emp1);
            leave1.setLeaveType(LeaveType.CONGE_PAYE);
            leave1.setStartDate(LocalDate.now().plusDays(10));
            leave1.setEndDate(LocalDate.now().plusDays(15));
            leave1.setStatus(LeaveStatus.PENDING);
            leaveRequestRepository.save(leave1);
            
            LeaveRequest leave2 = new LeaveRequest();
            leave2.setEmployee(emp2);
            leave2.setLeaveType(LeaveType.RTT);
            leave2.setStartDate(LocalDate.now().plusDays(5));
            leave2.setEndDate(LocalDate.now().plusDays(5));
            leave2.setStatus(LeaveStatus.APPROVED);
            leaveRequestRepository.save(leave2);
            
            System.out.println("Base de données initialisée avec des données de test!");
        };
    }
} 