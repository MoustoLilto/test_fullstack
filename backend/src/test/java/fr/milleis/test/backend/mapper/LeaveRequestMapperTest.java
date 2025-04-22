package fr.milleis.test.backend.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.milleis.test.backend.dto.CreateLeaveRequestDTO;
import fr.milleis.test.backend.dto.LeaveRequestDTO;
import fr.milleis.test.backend.models.Category;
import fr.milleis.test.backend.models.Employee;
import fr.milleis.test.backend.models.LeaveRequest;
import fr.milleis.test.backend.models.LeaveStatus;
import fr.milleis.test.backend.models.LeaveType;

public class LeaveRequestMapperTest {
    
    private LeaveRequestMapper mapper = new LeaveRequestMapper();
    
    @Test
    @DisplayName("Mapper une entité LeaveRequest vers un DTO")
    void toDTO_Success() {
        // Préparer les données
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Jean");
        employee.setLastName("Dupont");
        employee.setCategory(Category.CADRE);
        
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setId(1L);
        leaveRequest.setEmployee(employee);
        leaveRequest.setLeaveType(LeaveType.CONGE_PAYE);
        leaveRequest.setStartDate(LocalDate.of(2023, 7, 1));
        leaveRequest.setEndDate(LocalDate.of(2023, 7, 10));
        leaveRequest.setStatus(LeaveStatus.PENDING);
        
        // Exécuter le mapping
        LeaveRequestDTO dto = mapper.toDTO(leaveRequest);
        
        // Vérifier les résultats
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getEmployeeId());
        assertEquals(LeaveType.CONGE_PAYE, dto.getLeaveType());
        assertEquals(LocalDate.of(2023, 7, 1), dto.getStartDate());
        assertEquals(LocalDate.of(2023, 7, 10), dto.getEndDate());
        assertEquals(LeaveStatus.PENDING, dto.getStatus());
    }
    
    @Test
    @DisplayName("Mapper un DTO de création vers une entité LeaveRequest")
    void toEntity_Success() {
        // Préparer les données
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Jean");
        employee.setLastName("Dupont");
        employee.setCategory(Category.CADRE);
        
        CreateLeaveRequestDTO dto = new CreateLeaveRequestDTO();
        dto.setEmployeeId(1L);
        dto.setLeaveType(LeaveType.CONGE_PAYE);
        dto.setStartDate(LocalDate.of(2023, 7, 1));
        dto.setEndDate(LocalDate.of(2023, 7, 10));
        
        // Exécuter le mapping
        LeaveRequest leaveRequest = mapper.toEntity(dto, employee);
        
        // Vérifier les résultats
        assertNotNull(leaveRequest);
        assertEquals(employee, leaveRequest.getEmployee());
        assertEquals(LeaveType.CONGE_PAYE, leaveRequest.getLeaveType());
        assertEquals(LocalDate.of(2023, 7, 1), leaveRequest.getStartDate());
        assertEquals(LocalDate.of(2023, 7, 10), leaveRequest.getEndDate());
        assertEquals(LeaveStatus.PENDING, leaveRequest.getStatus());
    }
} 