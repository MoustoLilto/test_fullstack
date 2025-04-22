package fr.milleis.test.backend.services;

import fr.milleis.test.backend.dto.EmployeeDTO;
import fr.milleis.test.backend.mapper.EmployeeMapper;
import fr.milleis.test.backend.models.Category;
import fr.milleis.test.backend.models.Employee;
import fr.milleis.test.backend.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee1;
    private Employee employee2;
    private EmployeeDTO employeeDTO1;
    private EmployeeDTO employeeDTO2;
    private List<Employee> employeeList;
    private List<EmployeeDTO> employeeDTOList;

    @BeforeEach
    void setUp() {
        // Initialisation des données de test
        employee1 = new Employee(1L, "Dupont", "Jean", Category.CADRE, "2020-01-01", 25.0, 10.0);
        employee2 = new Employee(2L, "Martin", "Sophie", Category.NON_CADRE, "2021-05-15", 20.0, 8.0);
        employeeList = Arrays.asList(employee1, employee2);

        employeeDTO1 = new EmployeeDTO(1L, "Dupont", "Jean", "CADRE", "2020-01-01", 25.0, 10.0);
        employeeDTO2 = new EmployeeDTO(2L, "Martin", "Sophie", "NON_CADRE", "2021-05-15", 20.0, 8.0);
        employeeDTOList = Arrays.asList(employeeDTO1, employeeDTO2);
    }

    @Test
    void getAllEmployees_shouldReturnAllEmployees() {
        // Configuration
        when(employeeRepository.findAll()).thenReturn(employeeList);
        when(employeeMapper.toDtoList(employeeList)).thenReturn(employeeDTOList);

        // Exécution
        List<EmployeeDTO> result = employeeService.getAllEmployees();

        // Vérification
        assertEquals(2, result.size());
        assertEquals(employeeDTOList, result);
        verify(employeeRepository, times(1)).findAll();
        verify(employeeMapper, times(1)).toDtoList(employeeList);
    }

    @Test
    void getEmployeesByCategory_shouldReturnFilteredEmployees() {
        // Configuration
        when(employeeRepository.findByCategory(Category.CADRE)).thenReturn(Arrays.asList(employee1));
        when(employeeMapper.toDtoList(Arrays.asList(employee1))).thenReturn(Arrays.asList(employeeDTO1));

        // Exécution
        List<EmployeeDTO> result = employeeService.getEmployeesByCategory(Category.CADRE);

        // Vérification
        assertEquals(1, result.size());
        assertEquals(employeeDTO1.getId(), result.get(0).getId());
        verify(employeeRepository, times(1)).findByCategory(Category.CADRE);
    }

    @Test
    void getEmployeeById_shouldReturnEmployee_whenEmployeeExists() {
        // Configuration
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        when(employeeMapper.toDto(employee1)).thenReturn(employeeDTO1);

        // Exécution
        Optional<EmployeeDTO> result = employeeService.getEmployeeById(1L);

        // Vérification
        assertTrue(result.isPresent());
        assertEquals(employeeDTO1, result.get());
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeMapper, times(1)).toDto(employee1);
    }

    @Test
    void getEmployeeById_shouldReturnEmptyOptional_whenEmployeeDoesNotExist() {
        // Configuration
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        // Exécution
        Optional<EmployeeDTO> result = employeeService.getEmployeeById(99L);

        // Vérification
        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findById(99L);
        verify(employeeMapper, never()).toDto(any());
    }
} 