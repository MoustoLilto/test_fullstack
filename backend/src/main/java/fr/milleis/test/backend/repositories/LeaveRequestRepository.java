package fr.milleis.test.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.milleis.test.backend.models.Employee;
import fr.milleis.test.backend.models.LeaveRequest;
import fr.milleis.test.backend.models.LeaveStatus;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployee(Employee employee);
    List<LeaveRequest> findByEmployeeId(Long employeeId);
    List<LeaveRequest> findByEmployeeIdAndStatusNot(Long employeeId, LeaveStatus status);
} 