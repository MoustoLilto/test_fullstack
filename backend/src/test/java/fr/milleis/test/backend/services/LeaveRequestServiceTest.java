package fr.milleis.test.backend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
public class LeaveRequestServiceTest {

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private LeaveRequestMapper leaveRequestMapper;

    @InjectMocks
    private LeaveRequestService leaveRequestService;

    private Employee employee;
    private CreateLeaveRequestDTO createLeaveRequestDTO;
    private LeaveRequest leaveRequest;
    private LeaveRequestDTO leaveRequestDTO;

    @BeforeEach
    void setUp() {
        // Initialiser un employé cadre
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Jean");
        employee.setLastName("Dupont");
        employee.setCategory(Category.CADRE);

        // Initialiser un DTO de création de demande de congés valide
        createLeaveRequestDTO = new CreateLeaveRequestDTO();
        createLeaveRequestDTO.setEmployeeId(1L);
        createLeaveRequestDTO.setLeaveType(LeaveType.CONGE_PAYE);
        createLeaveRequestDTO.setStartDate(LocalDate.now().plusDays(5));
        createLeaveRequestDTO.setEndDate(LocalDate.now().plusDays(10));

        // Initialiser une demande de congés
        leaveRequest = new LeaveRequest();
        leaveRequest.setId(1L);
        leaveRequest.setEmployee(employee);
        leaveRequest.setLeaveType(LeaveType.CONGE_PAYE);
        leaveRequest.setStartDate(LocalDate.now().plusDays(5));
        leaveRequest.setEndDate(LocalDate.now().plusDays(10));
        leaveRequest.setStatus(LeaveStatus.PENDING);

        // Initialiser un DTO de demande de congés
        leaveRequestDTO = new LeaveRequestDTO();
        leaveRequestDTO.setId(1L);
        leaveRequestDTO.setEmployeeId(1L);
        leaveRequestDTO.setLeaveType(LeaveType.CONGE_PAYE);
        leaveRequestDTO.setStartDate(LocalDate.now().plusDays(5));
        leaveRequestDTO.setEndDate(LocalDate.now().plusDays(10));
        leaveRequestDTO.setStatus(LeaveStatus.PENDING);
    }

    @Test
    @DisplayName("Créer une demande de congés avec succès")
    void createLeaveRequest_Success() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(leaveRequestRepository.findByEmployeeIdAndStatusNot(anyLong(), any(LeaveStatus.class))).thenReturn(new ArrayList<>());
        when(leaveRequestMapper.toEntity(any(CreateLeaveRequestDTO.class), any(Employee.class))).thenReturn(leaveRequest);
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenReturn(leaveRequest);
        when(leaveRequestMapper.toDTO(any(LeaveRequest.class))).thenReturn(leaveRequestDTO);

        // Act
        LeaveRequestDTO result = leaveRequestService.createLeaveRequest(createLeaveRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(LeaveType.CONGE_PAYE, result.getLeaveType());
        assertEquals(createLeaveRequestDTO.getStartDate(), result.getStartDate());
        assertEquals(createLeaveRequestDTO.getEndDate(), result.getEndDate());
        assertEquals(LeaveStatus.PENDING, result.getStatus());

        verify(employeeRepository).findById(1L);
        verify(leaveRequestRepository).findByEmployeeIdAndStatusNot(anyLong(), any(LeaveStatus.class));
        verify(leaveRequestMapper).toEntity(any(CreateLeaveRequestDTO.class), any(Employee.class));
        verify(leaveRequestRepository).save(any(LeaveRequest.class));
        verify(leaveRequestMapper).toDTO(any(LeaveRequest.class));
    }

    @Test
    @DisplayName("Créer une demande de congés - Employé non trouvé")
    void createLeaveRequest_EmployeeNotFound() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmployeeNotFoundException.class, () -> {
            leaveRequestService.createLeaveRequest(createLeaveRequestDTO);
        });

        verify(employeeRepository).findById(1L);
        verify(leaveRequestRepository, never()).save(any(LeaveRequest.class));
    }

    @Test
    @DisplayName("Créer une demande de congés - Dates invalides (null)")
    void createLeaveRequest_InvalidDates_Null() {
        // Arrange
        CreateLeaveRequestDTO dto = new CreateLeaveRequestDTO();
        dto.setEmployeeId(1L);
        dto.setLeaveType(LeaveType.CONGE_PAYE);
        dto.setStartDate(null);
        dto.setEndDate(null);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Act & Assert
        assertThrows(InvalidLeaveDatesException.class, () -> {
            leaveRequestService.createLeaveRequest(dto);
        });

        verify(employeeRepository).findById(1L);
        verify(leaveRequestRepository, never()).save(any(LeaveRequest.class));
    }

    @Test
    @DisplayName("Créer une demande de congés - Date de début dans le passé")
    void createLeaveRequest_InvalidDates_StartInPast() {
        // Arrange
        CreateLeaveRequestDTO dto = new CreateLeaveRequestDTO();
        dto.setEmployeeId(1L);
        dto.setLeaveType(LeaveType.CONGE_PAYE);
        dto.setStartDate(LocalDate.now().minusDays(5));
        dto.setEndDate(LocalDate.now().plusDays(5));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Act & Assert
        assertThrows(InvalidLeaveDatesException.class, () -> {
            leaveRequestService.createLeaveRequest(dto);
        });

        verify(employeeRepository).findById(1L);
        verify(leaveRequestRepository, never()).save(any(LeaveRequest.class));
    }

    @Test
    @DisplayName("Créer une demande de congés - Date de fin avant date de début")
    void createLeaveRequest_InvalidDates_EndBeforeStart() {
        // Arrange
        CreateLeaveRequestDTO dto = new CreateLeaveRequestDTO();
        dto.setEmployeeId(1L);
        dto.setLeaveType(LeaveType.CONGE_PAYE);
        dto.setStartDate(LocalDate.now().plusDays(10));
        dto.setEndDate(LocalDate.now().plusDays(5));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Act & Assert
        assertThrows(InvalidLeaveDatesException.class, () -> {
            leaveRequestService.createLeaveRequest(dto);
        });

        verify(employeeRepository).findById(1L);
        verify(leaveRequestRepository, never()).save(any(LeaveRequest.class));
    }

    @Test
    @DisplayName("Créer une demande de congés - Type de congé non applicable (RTT pour non-cadre)")
    void createLeaveRequest_LeaveTypeNotApplicable() {
        // Arrange
        Employee nonCadreEmployee = new Employee();
        nonCadreEmployee.setId(2L);
        nonCadreEmployee.setCategory(Category.NON_CADRE);

        CreateLeaveRequestDTO dto = new CreateLeaveRequestDTO();
        dto.setEmployeeId(2L);
        dto.setLeaveType(LeaveType.RTT);
        dto.setStartDate(LocalDate.now().plusDays(5));
        dto.setEndDate(LocalDate.now().plusDays(10));

        when(employeeRepository.findById(2L)).thenReturn(Optional.of(nonCadreEmployee));

        // Act & Assert
        assertThrows(LeaveTypeNotApplicableException.class, () -> {
            leaveRequestService.createLeaveRequest(dto);
        });

        verify(employeeRepository).findById(2L);
        verify(leaveRequestRepository, never()).save(any(LeaveRequest.class));
    }

    @Test
    @DisplayName("Créer une demande de congés - Chevauchement avec congé existant")
    void createLeaveRequest_LeaveOverlap() {
        // Arrange
        List<LeaveRequest> existingLeaves = new ArrayList<>();
        LeaveRequest existingLeave = new LeaveRequest();
        existingLeave.setId(2L);
        existingLeave.setEmployee(employee);
        existingLeave.setLeaveType(LeaveType.CONGE_PAYE);
        existingLeave.setStartDate(LocalDate.now().plusDays(7));
        existingLeave.setEndDate(LocalDate.now().plusDays(12));
        existingLeave.setStatus(LeaveStatus.APPROVED);
        existingLeaves.add(existingLeave);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(leaveRequestRepository.findByEmployeeIdAndStatusNot(anyLong(), any(LeaveStatus.class))).thenReturn(existingLeaves);

        // Act & Assert
        assertThrows(LeaveOverlapException.class, () -> {
            leaveRequestService.createLeaveRequest(createLeaveRequestDTO);
        });

        verify(employeeRepository).findById(1L);
        verify(leaveRequestRepository).findByEmployeeIdAndStatusNot(anyLong(), any(LeaveStatus.class));
        verify(leaveRequestRepository, never()).save(any(LeaveRequest.class));
    }

    @Test
    @DisplayName("Créer une demande de congés - Solde insuffisant")
    void createLeaveRequest_InsufficientBalance() {
        // Arrange
        CreateLeaveRequestDTO dto = new CreateLeaveRequestDTO();
        dto.setEmployeeId(1L);
        dto.setLeaveType(LeaveType.CONGE_PAYE);
        dto.setStartDate(LocalDate.now().plusDays(5));
        dto.setEndDate(LocalDate.now().plusDays(35)); // 31 jours, mais le solde max est 25

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(leaveRequestRepository.findByEmployeeIdAndStatusNot(anyLong(), any(LeaveStatus.class))).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(InsufficientLeaveBalanceException.class, () -> {
            leaveRequestService.createLeaveRequest(dto);
        });

        verify(employeeRepository).findById(1L);
        verify(leaveRequestRepository).findByEmployeeIdAndStatusNot(anyLong(), any(LeaveStatus.class));
        verify(leaveRequestRepository, never()).save(any(LeaveRequest.class));
    }
} 