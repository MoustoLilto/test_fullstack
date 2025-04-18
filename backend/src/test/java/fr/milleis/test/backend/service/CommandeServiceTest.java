package fr.milleis.test.backend.service;

import fr.milleis.test.backend.model.Categorie;
import fr.milleis.test.backend.model.Commande;
import fr.milleis.test.backend.model.ModePaiement;
import fr.milleis.test.backend.model.Produit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CommandeServiceTest {

    @Mock
    private ProduitService produitService;

    private CommandeService commandeService;
    private Produit produit1;
    private Produit produit2;
    private Commande commande1;
    private Commande commande2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commandeService = new CommandeService(produitService);

        // Create test products
        produit1 = new Produit(1, "Produit 1", "REF001", Categorie.STANDARD, 100.0f, 5, 10);
        produit2 = new Produit(2, "Produit 2", "REF002", Categorie.SUR_COMMANDE, 50.0f, 3, 5);

        // Mock produitService behavior
        when(produitService.findById(1)).thenReturn(produit1);
        when(produitService.findById(2)).thenReturn(produit2);

        // Create test commandes
        commande1 = new Commande(null, 1, 2, LocalDate.now().plusDays(5), ModePaiement.CARTE_BANCAIRE, 5);
        commande2 = new Commande(null, 2, 1, LocalDate.now().plusDays(3), ModePaiement.VIREMENT, 3);

        // Mock updateStock behavior for successful creation
        when(produitService.updateStock(eq(1), eq(-2))).thenReturn(
            new Produit(1, "Produit 1", "REF001", Categorie.STANDARD, 100.0f, 5, 8)
        );
        when(produitService.updateStock(eq(2), eq(-1))).thenReturn(
            new Produit(2, "Produit 2", "REF002", Categorie.SUR_COMMANDE, 50.0f, 3, 4)
        );

        // Create commandes in service
        commande1 = commandeService.create(commande1);
        commande2 = commandeService.create(commande2);
    }

    @Test
    void create_WhenProductExistsAndHasEnoughStock_ShouldCreateCommande() {
        // Arrange
        Commande newCommande = new Commande(null, 1, 3, LocalDate.now().plusDays(4), ModePaiement.CHEQUE, 4);
        when(produitService.updateStock(eq(1), eq(-3))).thenReturn(
            new Produit(1, "Produit 1", "REF001", Categorie.STANDARD, 100.0f, 5, 5)
        );

        // Act
        Commande createdCommande = commandeService.create(newCommande);

        // Assert
        assertNotNull(createdCommande);
        assertNotNull(createdCommande.id());
        assertEquals(1, createdCommande.produitId());
        assertEquals(3, createdCommande.quantite());
        assertEquals(ModePaiement.CHEQUE, createdCommande.modePaiement());

        // Verify product stock was updated
        verify(produitService, times(1)).updateStock(1, -3);
    }

    @Test
    void create_WhenProductDoesNotExist_ShouldReturnNull() {
        // Arrange
        reset(produitService); // Reset mock to clear previous interactions
        Commande newCommande = new Commande(null, 999, 2, LocalDate.now().plusDays(5), ModePaiement.CARTE_BANCAIRE, 5);
        when(produitService.findById(999)).thenReturn(null);

        // Act
        Commande createdCommande = commandeService.create(newCommande);

        // Assert
        assertNull(createdCommande);
        verify(produitService, never()).updateStock(any(), any());
    }

    @Test
    void create_WhenProductHasInsufficientStock_ShouldReturnNull() {
        // Arrange
        reset(produitService); // Reset mock to clear previous interactions
        when(produitService.findById(1)).thenReturn(produit1); // Restore needed behavior
        Commande newCommande = new Commande(null, 1, 20, LocalDate.now().plusDays(5), ModePaiement.CARTE_BANCAIRE, 5);

        // Act
        Commande createdCommande = commandeService.create(newCommande);

        // Assert
        assertNull(createdCommande);
        verify(produitService, never()).updateStock(any(), any());
    }

    @Test
    void create_WhenStockUpdateFails_ShouldReturnNull() {
        // Arrange
        Commande newCommande = new Commande(null, 1, 5, LocalDate.now().plusDays(5), ModePaiement.CARTE_BANCAIRE, 5);
        when(produitService.updateStock(eq(1), eq(-5))).thenReturn(null);

        // Act
        Commande createdCommande = commandeService.create(newCommande);

        // Assert
        assertNull(createdCommande);
        verify(produitService, times(1)).updateStock(1, -5);
    }

    @Test
    void findAll_ShouldReturnAllCommandes() {
        // Act
        List<Commande> commandes = commandeService.findAll();

        // Assert
        assertEquals(2, commandes.size());
        assertTrue(commandes.contains(commande1));
        assertTrue(commandes.contains(commande2));
    }

    @Test
    void findById_WhenCommandeExists_ShouldReturnCommande() {
        // Act
        Commande foundCommande = commandeService.findById(commande1.id());

        // Assert
        assertNotNull(foundCommande);
        assertEquals(commande1, foundCommande);
    }

    @Test
    void findById_WhenCommandeDoesNotExist_ShouldReturnNull() {
        // Act
        Commande foundCommande = commandeService.findById(999);

        // Assert
        assertNull(foundCommande);
    }

    @Test
    void update_WhenCommandeExistsAndProductExistsAndHasEnoughStock_ShouldUpdateCommande() {
        // Arrange
        Commande updatedCommande = new Commande(commande1.id(), 1, 3, LocalDate.now().plusDays(7), ModePaiement.VIREMENT, 7);

        // Mock for stock adjustment (return 2 to original product, take 3 from same product)
        when(produitService.updateStock(eq(1), eq(2 - 3))).thenReturn(
            new Produit(1, "Produit 1", "REF001", Categorie.STANDARD, 100.0f, 5, 9)
        );

        // Act
        Commande result = commandeService.update(commande1.id(), updatedCommande);

        // Assert
        assertNotNull(result);
        assertEquals(commande1.id(), result.id());
        assertEquals(1, result.produitId());
        assertEquals(3, result.quantite());
        assertEquals(ModePaiement.VIREMENT, result.modePaiement());

        // Verify stock was adjusted
        verify(produitService, times(1)).updateStock(1, -1); // Net change of -1
    }

    @Test
    void update_WhenCommandeExistsButProductChanges_ShouldUpdateCommandeAndAdjustStocks() {
        // Arrange
        Commande updatedCommande = new Commande(commande1.id(), 2, 2, LocalDate.now().plusDays(7), ModePaiement.VIREMENT, 7);

        // Mock for returning stock to original product
        when(produitService.updateStock(eq(1), eq(2))).thenReturn(
            new Produit(1, "Produit 1", "REF001", Categorie.STANDARD, 100.0f, 5, 12)
        );

        // Mock for taking stock from new product
        when(produitService.updateStock(eq(2), eq(-2))).thenReturn(
            new Produit(2, "Produit 2", "REF002", Categorie.SUR_COMMANDE, 50.0f, 3, 3)
        );

        // Act
        Commande result = commandeService.update(commande1.id(), updatedCommande);

        // Assert
        assertNotNull(result);
        assertEquals(commande1.id(), result.id());
        assertEquals(2, result.produitId());
        assertEquals(2, result.quantite());

        // Verify stocks were adjusted
        verify(produitService, times(1)).updateStock(1, 2); // Return to original
        verify(produitService, times(1)).updateStock(2, -2); // Take from new
    }

    @Test
    void update_WhenCommandeDoesNotExist_ShouldReturnNull() {
        // Arrange
        reset(produitService); // Reset mock to clear previous interactions
        Commande updatedCommande = new Commande(999, 1, 3, LocalDate.now().plusDays(7), ModePaiement.VIREMENT, 7);

        // Act
        Commande result = commandeService.update(999, updatedCommande);

        // Assert
        assertNull(result);
        verify(produitService, never()).updateStock(any(), any());
    }

    @Test
    void update_WhenProductDoesNotExist_ShouldReturnNull() {
        // Arrange
        reset(produitService); // Reset mock to clear previous interactions
        when(produitService.findById(999)).thenReturn(null);
        when(produitService.findById(1)).thenReturn(produit1);
        Commande updatedCommande = new Commande(commande1.id(), 999, 3, LocalDate.now().plusDays(7), ModePaiement.VIREMENT, 7);

        // Act
        Commande result = commandeService.update(commande1.id(), updatedCommande);

        // Assert
        assertNull(result);
        verify(produitService, never()).updateStock(any(), any());
    }

    @Test
    void update_WhenStockUpdateFails_ShouldReturnNullAndRevertChanges() {
        // Arrange
        reset(produitService); // Reset mock to clear previous interactions

        // Set up necessary mocks
        when(produitService.findById(1)).thenReturn(produit1);
        when(produitService.findById(2)).thenReturn(produit2);

        // Create a commande directly in the test
        Commande testCommande = new Commande(1, 1, 2, LocalDate.now().plusDays(5), ModePaiement.CARTE_BANCAIRE, 5);

        // Create updated commande
        Commande updatedCommande = new Commande(1, 2, 10, LocalDate.now().plusDays(7), ModePaiement.VIREMENT, 7);

        // Mock for returning stock to original product
        when(produitService.updateStock(eq(1), eq(2))).thenReturn(
            new Produit(1, "Produit 1", "REF001", Categorie.STANDARD, 100.0f, 5, 12)
        );

        // Mock for failing to take stock from new product
        when(produitService.updateStock(eq(2), eq(-10))).thenReturn(null);

        // Mock for reverting stock change
        when(produitService.updateStock(eq(1), eq(-2))).thenReturn(
            new Produit(1, "Produit 1", "REF001", Categorie.STANDARD, 100.0f, 5, 10)
        );

        // Use reflection to add the commande directly to the service's internal map
        try {
            java.lang.reflect.Field commandesField = CommandeService.class.getDeclaredField("commandes");
            commandesField.setAccessible(true);
            Map<Integer, Commande> commandes = (Map<Integer, Commande>) commandesField.get(commandeService);
            commandes.put(Integer.valueOf(1), testCommande);
        } catch (Exception e) {
            fail("Failed to set up test: " + e.getMessage());
        }

        // Act
        Commande result = commandeService.update(1, updatedCommande);

        // Assert
        assertNull(result);

        // Verify attempt to revert changes
        verify(produitService, times(1)).updateStock(1, 2); // Return to original
        verify(produitService, times(1)).updateStock(2, -10); // Fail to take from new
        verify(produitService, times(1)).updateStock(1, -2); // Revert return to original
    }

    @Test
    void delete_WhenCommandeExists_ShouldDeleteCommandeAndReturnStockAndReturnTrue() {
        // Arrange
        when(produitService.updateStock(eq(1), eq(2))).thenReturn(
            new Produit(1, "Produit 1", "REF001", Categorie.STANDARD, 100.0f, 5, 12)
        );

        // Act
        boolean deleted = commandeService.delete(commande1.id());

        // Assert
        assertTrue(deleted);
        assertNull(commandeService.findById(commande1.id()));

        // Verify stock was returned
        verify(produitService, times(1)).updateStock(1, 2);
    }

    @Test
    void delete_WhenCommandeDoesNotExist_ShouldReturnFalse() {
        // Arrange
        reset(produitService); // Reset mock to clear previous interactions

        // Act
        boolean deleted = commandeService.delete(999);

        // Assert
        assertFalse(deleted);
        verify(produitService, never()).updateStock(any(), any());
    }

    @Test
    void findByProduitId_ShouldReturnCommandesForProduit() {
        // Act
        List<Commande> commandes = commandeService.findByProduitId(1);

        // Assert
        assertEquals(1, commandes.size());
        assertEquals(commande1, commandes.get(0));
    }
}
