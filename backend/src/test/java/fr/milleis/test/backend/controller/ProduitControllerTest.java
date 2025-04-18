package fr.milleis.test.backend.controller;

import fr.milleis.test.backend.model.Categorie;
import fr.milleis.test.backend.model.Produit;
import fr.milleis.test.backend.service.ProduitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ProduitControllerTest {

    @Mock
    private ProduitService produitService;

    @InjectMocks
    private ProduitController produitController;

    private Produit produit1;
    private Produit produit2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        produit1 = new Produit(1, "Produit 1", "REF001", Categorie.STANDARD, 100.0f, 5, 10);
        produit2 = new Produit(2, "Produit 2", "REF002", Categorie.SUR_COMMANDE, 50.0f, 3, 20);
    }

    @Test
    void createProduit_ShouldReturnCreatedProduit() {
        // Arrange
        Produit inputProduit = new Produit(null, "Nouveau Produit", "REF003", Categorie.STANDARD, 75.0f, 2, 15);
        Produit createdProduit = new Produit(3, "Nouveau Produit", "REF003", Categorie.STANDARD, 75.0f, 2, 15);

        when(produitService.create(any(Produit.class))).thenReturn(createdProduit);

        // Act
        ResponseEntity<Produit> response = produitController.createProduit(inputProduit);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdProduit, response.getBody());
        verify(produitService, times(1)).create(inputProduit);
    }

    @Test
    void getAllProduits_ShouldReturnAllProduits() {
        // Arrange
        List<Produit> produits = Arrays.asList(produit1, produit2);
        when(produitService.findAll()).thenReturn(produits);

        // Act
        ResponseEntity<List<Produit>> response = produitController.getAllProduits();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(produits, response.getBody());
        verify(produitService, times(1)).findAll();
    }

    @Test
    void getProduitById_WhenProduitExists_ShouldReturnProduit() {
        // Arrange
        when(produitService.findById(1)).thenReturn(produit1);

        // Act
        ResponseEntity<Produit> response = produitController.getProduitById(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(produit1, response.getBody());
        verify(produitService, times(1)).findById(1);
    }

    @Test
    void getProduitById_WhenProduitDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        when(produitService.findById(999)).thenReturn(null);

        // Act
        ResponseEntity<Produit> response = produitController.getProduitById(999);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(produitService, times(1)).findById(999);
    }

    @Test
    void updateProduit_WhenProduitExists_ShouldReturnUpdatedProduit() {
        // Arrange
        Produit updatedProduit = new Produit(1, "Produit 1 Updated", "REF001", Categorie.STANDARD, 120.0f, 4, 8);
        when(produitService.update(eq(1), any(Produit.class))).thenReturn(updatedProduit);

        // Act
        ResponseEntity<Produit> response = produitController.updateProduit(1, updatedProduit);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedProduit, response.getBody());
        verify(produitService, times(1)).update(eq(1), any(Produit.class));
    }

    @Test
    void updateProduit_WhenProduitDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        Produit updatedProduit = new Produit(999, "Non-existent Produit", "REF999", Categorie.STANDARD, 200.0f, 2, 5);
        when(produitService.update(eq(999), any(Produit.class))).thenReturn(null);

        // Act
        ResponseEntity<Produit> response = produitController.updateProduit(999, updatedProduit);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(produitService, times(1)).update(eq(999), any(Produit.class));
    }

    @Test
    void deleteProduit_WhenProduitExists_ShouldReturnNoContent() {
        // Arrange
        when(produitService.delete(1)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = produitController.deleteProduit(1);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(produitService, times(1)).delete(1);
    }

    @Test
    void deleteProduit_WhenProduitDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        when(produitService.delete(999)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = produitController.deleteProduit(999);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(produitService, times(1)).delete(999);
    }

    @Test
    void getProduitsGroupedByCategorie_ShouldReturnGroupedProduits() {
        // Arrange
        Map<Categorie, List<Produit>> groupedProduits = new HashMap<>();
        groupedProduits.put(Categorie.STANDARD, List.of(produit1));
        groupedProduits.put(Categorie.SUR_COMMANDE, List.of(produit2));

        when(produitService.findAllGroupedByCategorie()).thenReturn(groupedProduits);

        // Act
        ResponseEntity<Map<Categorie, List<Produit>>> response = produitController.getProduitsGroupedByCategorie();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(groupedProduits, response.getBody());
        verify(produitService, times(1)).findAllGroupedByCategorie();
    }
}
