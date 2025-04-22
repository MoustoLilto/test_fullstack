package fr.milleis.test.backend.dto;

import java.time.LocalDate;

import fr.milleis.test.backend.models.LeaveStatus;
import fr.milleis.test.backend.models.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequestDTO {
    private Long id;
    private Long employeeId;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveStatus status;
} 