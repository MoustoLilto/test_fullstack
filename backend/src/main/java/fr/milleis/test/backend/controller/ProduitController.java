package fr.milleis.test.backend.controller;

import fr.milleis.test.backend.model.Categorie;
import fr.milleis.test.backend.model.Produit;
import fr.milleis.test.backend.service.ProduitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/produits")
@Tag(name = "Product", description = "Product management API")
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
    @Operation(summary = "Create a new product", description = "Creates a new product and returns the created product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produit.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Produit> createProduit(
            @Parameter(description = "Product to create", required = true, schema = @Schema(implementation = Produit.class))
            @RequestBody Produit produit) {
        Produit createdProduit = produitService.create(produit);
        return new ResponseEntity<>(createdProduit, HttpStatus.CREATED);
    }

    /**
     * Get all products
     * @return list of all products
     */
    @Operation(summary = "Get all products", description = "Returns a list of all products")
    @ApiResponse(responseCode = "200", description = "List of products retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produit.class)))
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
    @Operation(summary = "Get a product by ID", description = "Returns a product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produit.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(
            @Parameter(description = "ID of the product to retrieve", required = true, example = "1")
            @PathVariable Integer id) {
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
    @Operation(summary = "Update a product", description = "Updates an existing product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produit.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Produit> updateProduit(
            @Parameter(description = "ID of the product to update", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Updated product information", required = true, schema = @Schema(implementation = Produit.class))
            @RequestBody Produit produit) {
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
    @Operation(summary = "Delete a product", description = "Deletes a product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product deleted successfully", content = @Content),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(
            @Parameter(description = "ID of the product to delete", required = true, example = "1")
            @PathVariable Integer id) {
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
    @Operation(summary = "Get products grouped by category", 
               description = "Returns a map of products grouped by their category")
    @ApiResponse(responseCode = "200", description = "Products grouped by category retrieved successfully",
            content = @Content(mediaType = "application/json"))
    @GetMapping("/grouped-by-categorie")
    public ResponseEntity<Map<Categorie, List<Produit>>> getProduitsGroupedByCategorie() {
        Map<Categorie, List<Produit>> groupedProduits = produitService.findAllGroupedByCategorie();
        return ResponseEntity.ok(groupedProduits);
    }
}
