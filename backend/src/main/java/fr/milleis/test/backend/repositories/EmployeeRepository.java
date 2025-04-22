package fr.milleis.test.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.milleis.test.backend.models.Category;
import fr.milleis.test.backend.models.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findById(Long id);
    List<Employee> findAll();
    List<Employee> findByCategory(Category category);
} 