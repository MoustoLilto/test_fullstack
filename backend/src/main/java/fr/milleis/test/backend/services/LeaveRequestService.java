package fr.milleis.test.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.milleis.test.backend.dto.CreateLeaveRequestDTO;
import fr.milleis.test.backend.dto.LeaveRequestDTO;
import fr.milleis.test.backend.mapper.LeaveRequestMapper;
import fr.milleis.test.backend.models.Employee;
import fr.milleis.test.backend.models.LeaveRequest;
import fr.milleis.test.backend.repositories.EmployeeRepository;
import fr.milleis.test.backend.repositories.LeaveRequestRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private LeaveRequestMapper leaveRequestMapper;
    
    public LeaveRequestDTO createLeaveRequest(CreateLeaveRequestDTO createLeaveRequestDTO) {
        // Vérifier que l'employé existe
        Employee employee = employeeRepository.findById(createLeaveRequestDTO.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employé non trouvé avec l'ID: " + createLeaveRequestDTO.getEmployeeId()));
        
        // Convertir le DTO en entité
        LeaveRequest leaveRequest = leaveRequestMapper.toEntity(createLeaveRequestDTO, employee);
        
        // Sauvegarder la demande de congés
        LeaveRequest savedLeaveRequest = leaveRequestRepository.save(leaveRequest);
        
        // Retourner le DTO de la demande créée
        return leaveRequestMapper.toDTO(savedLeaveRequest);
    }
} 