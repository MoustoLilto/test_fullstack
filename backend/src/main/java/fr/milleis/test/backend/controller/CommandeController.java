package fr.milleis.test.backend.controller;

import fr.milleis.test.backend.model.Commande;
import fr.milleis.test.backend.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes")
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
    @PostMapping
    public ResponseEntity<Commande> createCommande(@RequestBody Commande commande) {
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
    @GetMapping("/{id}")
    public ResponseEntity<Commande> getCommandeById(@PathVariable Integer id) {
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
    @PutMapping("/{id}")
    public ResponseEntity<Commande> updateCommande(@PathVariable Integer id, @RequestBody Commande commande) {
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Integer id) {
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
    @GetMapping("/by-produit/{produitId}")
    public ResponseEntity<List<Commande>> getCommandesByProduitId(@PathVariable Integer produitId) {
        List<Commande> commandes = commandeService.findByProduitId(produitId);
        return ResponseEntity.ok(commandes);
    }
}