package fr.milleis.test.backend.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.milleis.test.backend.dto.CreateLeaveRequestDTO;
import fr.milleis.test.backend.dto.LeaveRequestDTO;
import fr.milleis.test.backend.exceptions.EmployeeNotFoundException;
import fr.milleis.test.backend.exceptions.InsufficientLeaveBalanceException;
import fr.milleis.test.backend.exceptions.InvalidLeaveDatesException;
import fr.milleis.test.backend.exceptions.LeaveOverlapException;
import fr.milleis.test.backend.exceptions.LeaveTypeNotApplicableException;
import fr.milleis.test.backend.mapper.LeaveRequestMapper;
import fr.milleis.test.backend.models.Category;
import fr.milleis.test.backend.models.Employee;
import fr.milleis.test.backend.models.LeaveRequest;
import fr.milleis.test.backend.models.LeaveStatus;
import fr.milleis.test.backend.models.LeaveType;
import fr.milleis.test.backend.repositories.EmployeeRepository;
import fr.milleis.test.backend.repositories.LeaveRequestRepository;

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
                .orElseThrow(() -> new EmployeeNotFoundException("Employé non trouvé avec l'ID: " + createLeaveRequestDTO.getEmployeeId()));
        
        // Valider les dates
        validateDates(createLeaveRequestDTO);
        
        // Vérifier si le type de congé est applicable pour l'employé
        validateLeaveTypeForEmployee(employee, createLeaveRequestDTO.getLeaveType());
        
        // Vérifier les chevauchements avec d'autres congés
        checkForOverlaps(employee.getId(), createLeaveRequestDTO.getStartDate(), createLeaveRequestDTO.getEndDate());
        
        // Vérifier le solde de congés (à implémenter selon la logique métier)
        checkLeaveBalance(employee, createLeaveRequestDTO);
        
        // Convertir le DTO en entité
        LeaveRequest leaveRequest = leaveRequestMapper.toEntity(createLeaveRequestDTO, employee);
        
        // Sauvegarder la demande de congés
        LeaveRequest savedLeaveRequest = leaveRequestRepository.save(leaveRequest);
        
        // Retourner le DTO de la demande créée
        return leaveRequestMapper.toDTO(savedLeaveRequest);
    }
    
    private void validateDates(CreateLeaveRequestDTO dto) {
        LocalDate startDate = dto.getStartDate();
        LocalDate endDate = dto.getEndDate();
        
        // Vérifier que les dates ne sont pas nulles
        if (startDate == null || endDate == null) {
            throw new InvalidLeaveDatesException("Les dates de début et de fin doivent être spécifiées");
        }
        
        // Vérifier que la date de début n'est pas dans le passé
        if (startDate.isBefore(LocalDate.now())) {
            throw new InvalidLeaveDatesException("La date de début ne peut pas être dans le passé");
        }
        
        // Vérifier que la date de fin n'est pas avant la date de début
        if (endDate.isBefore(startDate)) {
            throw new InvalidLeaveDatesException("La date de fin ne peut pas être antérieure à la date de début");
        }
    }
    
    private void validateLeaveTypeForEmployee(Employee employee, LeaveType leaveType) {
        // Exemple: seulement les cadres peuvent prendre des RTT
        if (leaveType == LeaveType.RTT && employee.getCategory() != Category.CADRE) {
            throw new LeaveTypeNotApplicableException("Les congés RTT ne sont disponibles que pour les employés cadres");
        }
    }
    
    private void checkForOverlaps(Long employeeId, LocalDate startDate, LocalDate endDate) {
        List<LeaveRequest> existingLeaves = leaveRequestRepository.findByEmployeeIdAndStatusNot(employeeId, LeaveStatus.REJECTED);
        
        for (LeaveRequest existingLeave : existingLeaves) {
            // Vérifier s'il y a chevauchement
            if ((startDate.isBefore(existingLeave.getEndDate()) || startDate.isEqual(existingLeave.getEndDate())) && 
                (endDate.isAfter(existingLeave.getStartDate()) || endDate.isEqual(existingLeave.getStartDate()))) {
                throw new LeaveOverlapException("Cette demande chevauche une demande de congés existante");
            }
        }
    }
    
    private void checkLeaveBalance(Employee employee, CreateLeaveRequestDTO dto) {
        // Calculer le nombre de jours demandés
        long daysRequested = ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate()) + 1;
        
        // Pour démonstration: supposons que nous avons un solde fixe de 25 jours par an
        int availableBalance = 25; // À remplacer par la logique réelle de calcul du solde disponible
        
        if (daysRequested > availableBalance) {
            throw new InsufficientLeaveBalanceException("Solde de congés insuffisant: " + availableBalance + " jours disponibles, " + daysRequested + " jours demandés");
        }
    }
} 