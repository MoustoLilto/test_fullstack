package fr.milleis.test.backend.controller;

import fr.milleis.test.backend.model.Categorie;
import fr.milleis.test.backend.model.Produit;
import fr.milleis.test.backend.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    private final ProduitService produitService;

    @Autowired
    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    /**
     * Create a new product
     * @param produit the product to create
     * @return the created product with status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Produit> createProduit(@RequestBody Produit produit) {
        Produit createdProduit = produitService.create(produit);
        return new ResponseEntity<>(createdProduit, HttpStatus.CREATED);
    }

    /**
     * Get all products
     * @return list of all products
     */
    @GetMapping
    public ResponseEntity<List<Produit>> getAllProduits() {
        List<Produit> produits = produitService.findAll();
        return ResponseEntity.ok(produits);
    }

    /**
     * Get a product by its ID
     * @param id the product ID
     * @return the product if found, 404 otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Integer id) {
        Produit produit = produitService.findById(id);
        if (produit == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(produit);
    }

    /**
     * Update an existing product
     * @param id the ID of the product to update
     * @param produit the updated product data
     * @return the updated product if found, 404 otherwise
     */
    @PutMapping("/{id}")
    public ResponseEntity<Produit> updateProduit(@PathVariable Integer id, @RequestBody Produit produit) {
        Produit updatedProduit = produitService.update(id, produit);
        if (updatedProduit == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedProduit);
    }

    /**
     * Delete a product by its ID
     * @param id the ID of the product to delete
     * @return 204 if deleted, 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Integer id) {
        boolean deleted = produitService.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all products grouped by category
     * @return map of products grouped by category
     */
    @GetMapping("/grouped-by-categorie")
    public ResponseEntity<Map<Categorie, List<Produit>>> getProduitsGroupedByCategorie() {
        Map<Categorie, List<Produit>> groupedProduits = produitService.findAllGroupedByCategorie();
        return ResponseEntity.ok(groupedProduits);
    }
}
