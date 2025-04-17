package fr.milleis.test.backend.service;

import fr.milleis.test.backend.model.Categorie;
import fr.milleis.test.backend.model.Produit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProduitServiceTest {

    private ProduitService produitService;
    private Produit produit1;
    private Produit produit2;

    @BeforeEach
    void setUp() {
        produitService = new ProduitService();
        
        // Create test products
        produit1 = new Produit(null, "Produit 1", "REF001", Categorie.STANDARD, 100.0f, 5, 10);
        produit2 = new Produit(null, "Produit 2", "REF002", Categorie.SUR_COMMANDE, 50.0f, 3, 20);
        
        // Add products to service
        produit1 = produitService.create(produit1);
        produit2 = produitService.create(produit2);
    }

    @Test
    void create_ShouldCreateProduitWithGeneratedId() {
        // Arrange
        Produit newProduit = new Produit(null, "Nouveau Produit", "REF003", Categorie.STANDARD, 75.0f, 2, 15);
        
        // Act
        Produit createdProduit = produitService.create(newProduit);
        
        // Assert
        assertNotNull(createdProduit.id());
        assertEquals("Nouveau Produit", createdProduit.nom());
        assertEquals("REF003", createdProduit.reference());
        assertEquals(Categorie.STANDARD, createdProduit.categorie());
        assertEquals(75.0f, createdProduit.prix());
        assertEquals(2, createdProduit.delaiDeLivraison());
        assertEquals(15, createdProduit.stock());
    }

    @Test
    void create_WithProvidedId_ShouldUseProvidedId() {
        // Arrange
        Produit newProduit = new Produit(100, "Produit avec ID", "REF100", Categorie.STANDARD, 150.0f, 4, 25);
        
        // Act
        Produit createdProduit = produitService.create(newProduit);
        
        // Assert
        assertEquals(100, createdProduit.id());
    }

    @Test
    void findAll_ShouldReturnAllProduits() {
        // Act
        List<Produit> produits = produitService.findAll();
        
        // Assert
        assertEquals(2, produits.size());
        assertTrue(produits.contains(produit1));
        assertTrue(produits.contains(produit2));
    }

    @Test
    void findById_WhenProduitExists_ShouldReturnProduit() {
        // Act
        Produit foundProduit = produitService.findById(produit1.id());
        
        // Assert
        assertNotNull(foundProduit);
        assertEquals(produit1, foundProduit);
    }

    @Test
    void findById_WhenProduitDoesNotExist_ShouldReturnNull() {
        // Act
        Produit foundProduit = produitService.findById(999);
        
        // Assert
        assertNull(foundProduit);
    }

    @Test
    void update_WhenProduitExists_ShouldUpdateProduit() {
        // Arrange
        Produit updatedProduit = new Produit(produit1.id(), "Produit 1 Updated", "REF001-UPD", Categorie.SUR_COMMANDE, 120.0f, 4, 8);
        
        // Act
        Produit result = produitService.update(produit1.id(), updatedProduit);
        
        // Assert
        assertNotNull(result);
        assertEquals(produit1.id(), result.id());
        assertEquals("Produit 1 Updated", result.nom());
        assertEquals("REF001-UPD", result.reference());
        assertEquals(Categorie.SUR_COMMANDE, result.categorie());
        assertEquals(120.0f, result.prix());
        assertEquals(4, result.delaiDeLivraison());
        assertEquals(8, result.stock());
        
        // Verify the product was actually updated in the service
        assertEquals(result, produitService.findById(produit1.id()));
    }

    @Test
    void update_WhenProduitDoesNotExist_ShouldReturnNull() {
        // Arrange
        Produit updatedProduit = new Produit(999, "Non-existent Produit", "REF999", Categorie.STANDARD, 200.0f, 2, 5);
        
        // Act
        Produit result = produitService.update(999, updatedProduit);
        
        // Assert
        assertNull(result);
    }

    @Test
    void delete_WhenProduitExists_ShouldDeleteProduitAndReturnTrue() {
        // Act
        boolean deleted = produitService.delete(produit1.id());
        
        // Assert
        assertTrue(deleted);
        assertNull(produitService.findById(produit1.id()));
    }

    @Test
    void delete_WhenProduitDoesNotExist_ShouldReturnFalse() {
        // Act
        boolean deleted = produitService.delete(999);
        
        // Assert
        assertFalse(deleted);
    }

    @Test
    void findAllGroupedByCategorie_ShouldReturnProduitsGroupedByCategorie() {
        // Act
        Map<Categorie, List<Produit>> groupedProduits = produitService.findAllGroupedByCategorie();
        
        // Assert
        assertEquals(2, groupedProduits.size());
        assertTrue(groupedProduits.containsKey(Categorie.STANDARD));
        assertTrue(groupedProduits.containsKey(Categorie.SUR_COMMANDE));
        assertEquals(1, groupedProduits.get(Categorie.STANDARD).size());
        assertEquals(1, groupedProduits.get(Categorie.SUR_COMMANDE).size());
        assertEquals(produit1, groupedProduits.get(Categorie.STANDARD).get(0));
        assertEquals(produit2, groupedProduits.get(Categorie.SUR_COMMANDE).get(0));
    }

    @Test
    void updateStock_WhenProduitExistsAndStockSufficient_ShouldUpdateStock() {
        // Act - Decrease stock
        Produit result1 = produitService.updateStock(produit1.id(), -3);
        
        // Assert
        assertNotNull(result1);
        assertEquals(7, result1.stock());
        
        // Act - Increase stock
        Produit result2 = produitService.updateStock(produit1.id(), 5);
        
        // Assert
        assertNotNull(result2);
        assertEquals(12, result2.stock());
    }

    @Test
    void updateStock_WhenProduitDoesNotExist_ShouldReturnNull() {
        // Act
        Produit result = produitService.updateStock(999, -1);
        
        // Assert
        assertNull(result);
    }

    @Test
    void updateStock_WhenStockInsufficientForDecrease_ShouldReturnNull() {
        // Act
        Produit result = produitService.updateStock(produit1.id(), -20);
        
        // Assert
        assertNull(result);
        
        // Verify the stock wasn't changed
        assertEquals(10, produitService.findById(produit1.id()).stock());
    }
}