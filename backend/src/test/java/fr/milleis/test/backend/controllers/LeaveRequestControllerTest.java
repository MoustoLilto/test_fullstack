package fr.milleis.test.backend.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import fr.milleis.test.backend.dto.CreateLeaveRequestDTO;
import fr.milleis.test.backend.dto.LeaveRequestDTO;
import fr.milleis.test.backend.exceptions.EmployeeNotFoundException;
import fr.milleis.test.backend.exceptions.GlobalExceptionHandler;
import fr.milleis.test.backend.models.LeaveStatus;
import fr.milleis.test.backend.models.LeaveType;
import fr.milleis.test.backend.services.LeaveRequestService;

@ExtendWith(MockitoExtension.class)
public class LeaveRequestControllerTest {

    @Mock
    private LeaveRequestService leaveRequestService;

    @InjectMocks
    private LeaveRequestController leaveRequestController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Configurer le gestionnaire d'exceptions global
        mockMvc = MockMvcBuilders.standaloneSetup(leaveRequestController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        // Configurer l'ObjectMapper pour gérer les dates LocalDate
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Créer une demande de congés avec succès")
    void createLeaveRequest_Success() throws Exception {
        // Préparer le DTO de création
        CreateLeaveRequestDTO createDto = new CreateLeaveRequestDTO();
        createDto.setEmployeeId(1L);
        createDto.setLeaveType(LeaveType.CONGE_PAYE);
        createDto.setStartDate(LocalDate.now().plusDays(5));
        createDto.setEndDate(LocalDate.now().plusDays(10));

        // Préparer le DTO de réponse
        LeaveRequestDTO responseDto = new LeaveRequestDTO();
        responseDto.setId(1L);
        responseDto.setEmployeeId(1L);
        responseDto.setLeaveType(LeaveType.CONGE_PAYE);
        responseDto.setStartDate(LocalDate.now().plusDays(5));
        responseDto.setEndDate(LocalDate.now().plusDays(10));
        responseDto.setStatus(LeaveStatus.PENDING);

        // Configurer le mock du service
        when(leaveRequestService.createLeaveRequest(any(CreateLeaveRequestDTO.class)))
                .thenReturn(responseDto);

        // Exécuter la requête et vérifier le résultat
        mockMvc.perform(post("/api/leave-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.employeeId").value(1))
                .andExpect(jsonPath("$.leaveType").value("CONGE_PAYE"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("Créer une demande de congés - Erreur employé non trouvé")
    void createLeaveRequest_EmployeeNotFound() throws Exception {
        // Préparer le DTO de création
        CreateLeaveRequestDTO createDto = new CreateLeaveRequestDTO();
        createDto.setEmployeeId(999L); // ID inexistant
        createDto.setLeaveType(LeaveType.CONGE_PAYE);
        createDto.setStartDate(LocalDate.now().plusDays(5));
        createDto.setEndDate(LocalDate.now().plusDays(10));

        // Configurer le mock du service pour lancer une exception
        when(leaveRequestService.createLeaveRequest(any(CreateLeaveRequestDTO.class)))
                .thenThrow(new EmployeeNotFoundException("Employé non trouvé avec l'ID: 999"));

        // Exécuter la requête et vérifier le résultat
        mockMvc.perform(post("/api/leave-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employé non trouvé avec l'ID: 999"));
    }
} 