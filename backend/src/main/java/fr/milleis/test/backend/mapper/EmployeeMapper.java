package fr.milleis.test.backend.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import fr.milleis.test.backend.dto.EmployeeDTO;
import fr.milleis.test.backend.models.Employee;

@Component
public class EmployeeMapper {
    
    public EmployeeDTO toDto(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setCategory(employee.getCategory().name());
        dto.setHireDate(employee.getHireDate());
        dto.setLeaveBalance(employee.getLeaveBalance());
        dto.setRttBalance(employee.getRttBalance());
        return dto;
    }
    
    public List<EmployeeDTO> toDtoList(List<Employee> employees) {
        return employees.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
} 