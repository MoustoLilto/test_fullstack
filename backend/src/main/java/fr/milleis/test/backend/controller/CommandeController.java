package fr.milleis.test.backend.controller;

import fr.milleis.test.backend.model.Commande;
import fr.milleis.test.backend.service.CommandeService;
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

@RestController
@RequestMapping("/api/commandes")
@Tag(name = "Order", description = "Order management API")
public class CommandeController {

    private final CommandeService commandeService;

    @Autowired
    public CommandeController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }

    /**
     * Create a new order
     * @param commande the order to create
     * @return the created order with status 201 (Created) or 400 (Bad Request) if the product doesn't exist or is out of stock
     */
    @Operation(summary = "Create a new order", 
               description = "Creates a new order and returns the created order. Returns 400 if the product doesn't exist or is out of stock.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Commande.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input, product doesn't exist or is out of stock", 
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<Commande> createCommande(
            @Parameter(description = "Order to create", required = true, schema = @Schema(implementation = Commande.class))
            @RequestBody Commande commande) {
        Commande createdCommande = commandeService.create(commande);
        if (createdCommande == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(createdCommande, HttpStatus.CREATED);
    }

    /**
     * Get all orders
     * @return list of all orders
     */
    @Operation(summary = "Get all orders", description = "Returns a list of all orders")
    @ApiResponse(responseCode = "200", description = "List of orders retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Commande.class)))
    @GetMapping
    public ResponseEntity<List<Commande>> getAllCommandes() {
        List<Commande> commandes = commandeService.findAll();
        return ResponseEntity.ok(commandes);
    }

    /**
     * Get an order by its ID
     * @param id the order ID
     * @return the order if found, 404 otherwise
     */
    @Operation(summary = "Get an order by ID", description = "Returns an order by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order found",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Commande.class))),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Commande> getCommandeById(
            @Parameter(description = "ID of the order to retrieve", required = true, example = "1")
            @PathVariable Integer id) {
        Commande commande = commandeService.findById(id);
        if (commande == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(commande);
    }

    /**
     * Update an existing order
     * @param id the ID of the order to update
     * @param commande the updated order data
     * @return the updated order if found, 404 if not found, 400 if the product doesn't exist or is out of stock
     */
    @Operation(summary = "Update an order", 
               description = "Updates an existing order by its ID. Returns 404 if order not found, 400 if product doesn't exist or is out of stock.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order updated successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Commande.class))),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid input, product doesn't exist or is out of stock", 
                content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Commande> updateCommande(
            @Parameter(description = "ID of the order to update", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Updated order information", required = true, schema = @Schema(implementation = Commande.class))
            @RequestBody Commande commande) {
        Commande updatedCommande = commandeService.update(id, commande);
        if (updatedCommande == null) {
            // Check if the order exists
            if (commandeService.findById(id) == null) {
                return ResponseEntity.notFound().build();
            }
            // If the order exists but update failed, it's because of product validation
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updatedCommande);
    }

    /**
     * Delete an order by its ID
     * @param id the ID of the order to delete
     * @return 204 if deleted, 404 if not found
     */
    @Operation(summary = "Delete an order", description = "Deletes an order by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Order deleted successfully", content = @Content),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(
            @Parameter(description = "ID of the order to delete", required = true, example = "1")
            @PathVariable Integer id) {
        boolean deleted = commandeService.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all orders for a specific product
     * @param produitId the product ID
     * @return list of orders for the specified product
     */
    @Operation(summary = "Get orders by product ID", 
               description = "Returns a list of all orders for a specific product")
    @ApiResponse(responseCode = "200", description = "List of orders for the product retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Commande.class)))
    @GetMapping("/by-produit/{produitId}")
    public ResponseEntity<List<Commande>> getCommandesByProduitId(
            @Parameter(description = "ID of the product to get orders for", required = true, example = "1")
            @PathVariable Integer produitId) {
        List<Commande> commandes = commandeService.findByProduitId(produitId);
        return ResponseEntity.ok(commandes);
    }
}
