package fr.milleis.test.backend.mapper;

import org.springframework.stereotype.Component;

import fr.milleis.test.backend.dto.CreateLeaveRequestDTO;
import fr.milleis.test.backend.dto.LeaveRequestDTO;
import fr.milleis.test.backend.models.Employee;
import fr.milleis.test.backend.models.LeaveRequest;
import fr.milleis.test.backend.models.LeaveStatus;

@Component
public class LeaveRequestMapper {

    public LeaveRequestDTO toDTO(LeaveRequest leaveRequest) {
        LeaveRequestDTO dto = new LeaveRequestDTO();
        dto.setId(leaveRequest.getId());
        dto.setEmployeeId(leaveRequest.getEmployee().getId());
        dto.setLeaveType(leaveRequest.getLeaveType());
        dto.setStartDate(leaveRequest.getStartDate());
        dto.setEndDate(leaveRequest.getEndDate());
        dto.setStatus(leaveRequest.getStatus());
        return dto;
    }

    public LeaveRequest toEntity(CreateLeaveRequestDTO dto, Employee employee) {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);
        leaveRequest.setLeaveType(dto.getLeaveType());
        leaveRequest.setStartDate(dto.getStartDate());
        leaveRequest.setEndDate(dto.getEndDate());
        leaveRequest.setStatus(LeaveStatus.PENDING);
        return leaveRequest;
    }
} 