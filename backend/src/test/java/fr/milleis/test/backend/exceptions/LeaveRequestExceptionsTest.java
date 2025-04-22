package fr.milleis.test.backend.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class LeaveRequestExceptionsTest {

    @Test
    @DisplayName("Test de la création d'une exception EmployeeNotFoundException")
    void testEmployeeNotFoundException() {
        String message = "Employé non trouvé avec l'ID: 1";
        EmployeeNotFoundException exception = new EmployeeNotFoundException(message);
        
        assertEquals(message, exception.getMessage());
    }
    
    @Test
    @DisplayName("Test de la création d'une exception InsufficientLeaveBalanceException")
    void testInsufficientLeaveBalanceException() {
        String message = "Solde de congés insuffisant";
        InsufficientLeaveBalanceException exception = new InsufficientLeaveBalanceException(message);
        
        assertEquals(message, exception.getMessage());
    }
    
    @Test
    @DisplayName("Test de la création d'une exception InvalidLeaveDatesException")
    void testInvalidLeaveDatesException() {
        String message = "Les dates de congés sont invalides";
        InvalidLeaveDatesException exception = new InvalidLeaveDatesException(message);
        
        assertEquals(message, exception.getMessage());
    }
    
    @Test
    @DisplayName("Test de la création d'une exception LeaveOverlapException")
    void testLeaveOverlapException() {
        String message = "Chevauchement avec un congé existant";
        LeaveOverlapException exception = new LeaveOverlapException(message);
        
        assertEquals(message, exception.getMessage());
    }
    
    @Test
    @DisplayName("Test de la création d'une exception LeaveTypeNotApplicableException")
    void testLeaveTypeNotApplicableException() {
        String message = "Type de congé non applicable";
        LeaveTypeNotApplicableException exception = new LeaveTypeNotApplicableException(message);
        
        assertEquals(message, exception.getMessage());
    }
    
    @Test
    @DisplayName("Test du gestionnaire global d'exceptions - EmployeeNotFoundException")
    void testGlobalExceptionHandler_EmployeeNotFoundException() throws Exception {
        // Setup
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(exceptionHandler)
                .build();
        
        // Vérifier que l'exception est bien mappée avec le bon status HTTP
        MockHttpServletResponse response = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/test/employee-not-found"))
                .andReturn().getResponse();
        
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertNotNull(response.getContentAsString());
    }
    
    @Test
    @DisplayName("Test du gestionnaire global d'exceptions - InvalidLeaveDatesException")
    void testGlobalExceptionHandler_InvalidLeaveDatesException() throws Exception {
        // Setup
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(exceptionHandler)
                .build();
        
        // Vérifier que l'exception est bien mappée avec le bon status HTTP
        MockHttpServletResponse response = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/test/invalid-dates"))
                .andReturn().getResponse();
        
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertNotNull(response.getContentAsString());
    }
    
    // Contrôleur de test qui lance des exceptions pour les tests
    private static class TestController {
        
        @org.springframework.web.bind.annotation.GetMapping("/test/employee-not-found")
        public void throwEmployeeNotFoundException() {
            throw new EmployeeNotFoundException("Employé non trouvé avec l'ID: 1");
        }
        
        @org.springframework.web.bind.annotation.GetMapping("/test/invalid-dates")
        public void throwInvalidLeaveDatesException() {
            throw new InvalidLeaveDatesException("Les dates de congés sont invalides");
        }
    }
} 