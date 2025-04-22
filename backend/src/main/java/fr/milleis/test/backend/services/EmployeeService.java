package fr.milleis.test.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.milleis.test.backend.dto.EmployeeDTO;
import fr.milleis.test.backend.mapper.EmployeeMapper;
import fr.milleis.test.backend.models.Category;
import fr.milleis.test.backend.models.Employee;
import fr.milleis.test.backend.repositories.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private EmployeeMapper employeeMapper;
    
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employeeMapper.toDtoList(employees);
    }

    public List<EmployeeDTO> getEmployeesByCategory(Category category) {
        List<Employee> employees = employeeRepository.findByCategory(category);
        return employeeMapper.toDtoList(employees);
    }

    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.map(employeeMapper::toDto);
    }
}
