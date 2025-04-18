package fr.milleis.test.backend.controller;

import fr.milleis.test.backend.model.Commande;
import fr.milleis.test.backend.model.ModePaiement;
import fr.milleis.test.backend.service.CommandeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CommandeControllerTest {

    @Mock
    private CommandeService commandeService;

    @InjectMocks
    private CommandeController commandeController;

    private Commande commande1;
    private Commande commande2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        commande1 = new Commande(1, 1, 2, LocalDate.now().plusDays(5), ModePaiement.CARTE_BANCAIRE, 5);
        commande2 = new Commande(2, 2, 1, LocalDate.now().plusDays(3), ModePaiement.VIREMENT, 3);
    }

    @Test
    void createCommande_WhenCommandeIsValid_ShouldReturnCreatedCommande() {
        // Arrange
        Commande inputCommande = new Commande(null, 1, 2, LocalDate.now().plusDays(5), ModePaiement.CARTE_BANCAIRE, 5);
        Commande createdCommande = new Commande(3, 1, 2, LocalDate.now().plusDays(5), ModePaiement.CARTE_BANCAIRE, 5);

        when(commandeService.create(any(Commande.class))).thenReturn(createdCommande);

        // Act
        ResponseEntity<Commande> response = commandeController.createCommande(inputCommande);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdCommande, response.getBody());
        verify(commandeService, times(1)).create(inputCommande);
    }

    @Test
    void createCommande_WhenCommandeIsInvalid_ShouldReturnBadRequest() {
        // Arrange
        Commande inputCommande = new Commande(null, 999, 2, LocalDate.now().plusDays(5), ModePaiement.CARTE_BANCAIRE, 5);

        when(commandeService.create(any(Commande.class))).thenReturn(null);

        // Act
        ResponseEntity<Commande> response = commandeController.createCommande(inputCommande);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(commandeService, times(1)).create(inputCommande);
    }

    @Test
    void getAllCommandes_ShouldReturnAllCommandes() {
        // Arrange
        List<Commande> commandes = Arrays.asList(commande1, commande2);
        when(commandeService.findAll()).thenReturn(commandes);

        // Act
        ResponseEntity<List<Commande>> response = commandeController.getAllCommandes();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(commandes, response.getBody());
        verify(commandeService, times(1)).findAll();
    }

    @Test
    void getCommandeById_WhenCommandeExists_ShouldReturnCommande() {
        // Arrange
        when(commandeService.findById(1)).thenReturn(commande1);

        // Act
        ResponseEntity<Commande> response = commandeController.getCommandeById(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(commande1, response.getBody());
        verify(commandeService, times(1)).findById(1);
    }

    @Test
    void getCommandeById_WhenCommandeDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        when(commandeService.findById(999)).thenReturn(null);

        // Act
        ResponseEntity<Commande> response = commandeController.getCommandeById(999);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(commandeService, times(1)).findById(999);
    }

    @Test
    void updateCommande_WhenCommandeExistsAndUpdateIsValid_ShouldReturnUpdatedCommande() {
        // Arrange
        Commande updatedCommande = new Commande(1, 1, 3, LocalDate.now().plusDays(7), ModePaiement.VIREMENT, 7);
        when(commandeService.update(eq(1), any(Commande.class))).thenReturn(updatedCommande);

        // Act
        ResponseEntity<Commande> response = commandeController.updateCommande(1, updatedCommande);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCommande, response.getBody());
        verify(commandeService, times(1)).update(eq(1), any(Commande.class));
    }

    @Test
    void updateCommande_WhenCommandeDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        Commande updatedCommande = new Commande(999, 1, 3, LocalDate.now().plusDays(7), ModePaiement.VIREMENT, 7);
        when(commandeService.update(eq(999), any(Commande.class))).thenReturn(null);
        when(commandeService.findById(999)).thenReturn(null);

        // Act
        ResponseEntity<Commande> response = commandeController.updateCommande(999, updatedCommande);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(commandeService, times(1)).update(eq(999), any(Commande.class));
        verify(commandeService, times(1)).findById(999);
    }

    @Test
    void updateCommande_WhenCommandeExistsButUpdateIsInvalid_ShouldReturnBadRequest() {
        // Arrange
        Commande updatedCommande = new Commande(1, 999, 10, LocalDate.now().plusDays(7), ModePaiement.VIREMENT, 7);
        when(commandeService.update(eq(1), any(Commande.class))).thenReturn(null);
        when(commandeService.findById(1)).thenReturn(commande1);

        // Act
        ResponseEntity<Commande> response = commandeController.updateCommande(1, updatedCommande);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(commandeService, times(1)).update(eq(1), any(Commande.class));
        verify(commandeService, times(1)).findById(1);
    }

    @Test
    void deleteCommande_WhenCommandeExists_ShouldReturnNoContent() {
        // Arrange
        when(commandeService.delete(1)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = commandeController.deleteCommande(1);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(commandeService, times(1)).delete(1);
    }

    @Test
    void deleteCommande_WhenCommandeDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        when(commandeService.delete(999)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = commandeController.deleteCommande(999);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(commandeService, times(1)).delete(999);
    }

    @Test
    void getCommandesByProduitId_ShouldReturnCommandesForProduit() {
        // Arrange
        List<Commande> commandes = List.of(commande1);
        when(commandeService.findByProduitId(1)).thenReturn(commandes);

        // Act
        ResponseEntity<List<Commande>> response = commandeController.getCommandesByProduitId(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(commandes, response.getBody());
        verify(commandeService, times(1)).findByProduitId(1);
    }
}
