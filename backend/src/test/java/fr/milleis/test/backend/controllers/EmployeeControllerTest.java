package fr.milleis.test.backend.controllers;

import fr.milleis.test.backend.dto.EmployeeDTO;
import fr.milleis.test.backend.models.Category;
import fr.milleis.test.backend.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private EmployeeDTO employeeDTO1;
    private EmployeeDTO employeeDTO2;
    private List<EmployeeDTO> employeeDTOList;

    @BeforeEach
    void setUp() {
        // Initialisation des données de test
        employeeDTO1 = new EmployeeDTO(1L, "Dupont", "Jean", "CADRE", "2020-01-01", 25.0, 10.0);
        employeeDTO2 = new EmployeeDTO(2L, "Martin", "Sophie", "NON_CADRE", "2021-05-15", 20.0, 8.0);
        employeeDTOList = Arrays.asList(employeeDTO1, employeeDTO2);
    }

    @Test
    void getEmployees_shouldReturnAllEmployees_whenNoCategoryProvided() {
        // Configuration
        when(employeeService.getAllEmployees()).thenReturn(employeeDTOList);

        // Exécution
        ResponseEntity<List<EmployeeDTO>> response = employeeController.getEmployees(null);

        // Vérification
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(employeeService, times(1)).getAllEmployees();
        verify(employeeService, never()).getEmployeesByCategory(any());
    }

    @Test
    void getEmployees_shouldReturnFilteredEmployees_whenCategoryProvided() {
        // Configuration
        when(employeeService.getEmployeesByCategory(Category.CADRE)).thenReturn(Arrays.asList(employeeDTO1));

        // Exécution
        ResponseEntity<List<EmployeeDTO>> response = employeeController.getEmployees(Category.CADRE);

        // Vérification
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(employeeDTO1.getId(), response.getBody().get(0).getId());
        verify(employeeService, times(1)).getEmployeesByCategory(Category.CADRE);
        verify(employeeService, never()).getAllEmployees();
    }

    @Test
    void getEmployeeById_shouldReturnEmployee_whenEmployeeExists() {
        // Configuration
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employeeDTO1));

        // Exécution
        ResponseEntity<EmployeeDTO> response = employeeController.getEmployeeById(1L);

        // Vérification
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeDTO1, response.getBody());
        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    void getEmployeeById_shouldReturnNotFound_whenEmployeeDoesNotExist() {
        // Configuration
        when(employeeService.getEmployeeById(99L)).thenReturn(Optional.empty());

        // Exécution
        ResponseEntity<EmployeeDTO> response = employeeController.getEmployeeById(99L);

        // Vérification
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(employeeService, times(1)).getEmployeeById(99L);
    }
} 