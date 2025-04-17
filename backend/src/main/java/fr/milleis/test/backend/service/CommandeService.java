package fr.milleis.test.backend.service;

import fr.milleis.test.backend.model.Commande;
import fr.milleis.test.backend.model.Produit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CommandeService {

    private final Map<Integer, Commande> commandes = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);
    private final ProduitService produitService;

    @Autowired
    public CommandeService(ProduitService produitService) {
        this.produitService = produitService;
    }

    /**
     * Create a new order
     * @param commande the order to create (id will be generated)
     * @return the created order with generated id, or null if the product doesn't exist or is out of stock
     */
    public Commande create(Commande commande) {
        // Check if product exists and has enough stock
        Produit produit = produitService.findById(commande.produitId());
        if (produit == null || produit.stock() < commande.quantite()) {
            return null;
        }

        // Generate a new ID if not provided
        Integer id = commande.id() != null ? commande.id() : idGenerator.getAndIncrement();

        // Create a new Commande with the generated ID
        Commande newCommande = new Commande(
            id,
            commande.produitId(),
            commande.quantite(),
            commande.dateLivraison(),
            commande.modePaiement(),
            commande.delaiLivraison()
        );

        // Update product stock (decrease by order quantity)
        Produit updatedProduit = produitService.updateStock(commande.produitId(), -commande.quantite());
        if (updatedProduit == null) {
            return null;
        }

        commandes.put(id, newCommande);
        return newCommande;
    }

    /**
     * Get all orders
     * @return list of all orders
     */
    public List<Commande> findAll() {
        return new ArrayList<>(commandes.values());
    }

    /**
     * Get an order by its ID
     * @param id the order ID
     * @return the order if found, null otherwise
     */
    public Commande findById(Integer id) {
        return commandes.get(id);
    }

    /**
     * Update an existing order
     * @param id the ID of the order to update
     * @param commande the updated order data
     * @return the updated order if found, null otherwise
     */
    public Commande update(Integer id, Commande commande) {
        Commande existingCommande = commandes.get(id);
        if (existingCommande == null) {
            return null;
        }

        // Check if product exists
        Produit produit = produitService.findById(commande.produitId());
        if (produit == null) {
            return null;
        }

        // Calculate stock change
        int stockChange = 0;

        // If the product ID is the same, just adjust for quantity difference
        if (existingCommande.produitId().equals(commande.produitId())) {
            stockChange = existingCommande.quantite() - commande.quantite();
        } else {
            // If product changed, return stock to original product and take from new product
            // Return stock to original product
            produitService.updateStock(existingCommande.produitId(), existingCommande.quantite());
            // Take from new product
            stockChange = -commande.quantite();
        }

        // Update stock of the product
        Produit updatedProduit = produitService.updateStock(commande.produitId(), stockChange);
        if (updatedProduit == null) {
            // If stock update failed, revert any changes and return null
            if (!existingCommande.produitId().equals(commande.produitId())) {
                // Revert stock return to original product
                produitService.updateStock(existingCommande.produitId(), -existingCommande.quantite());
            }
            return null;
        }

        Commande updatedCommande = new Commande(
            id,
            commande.produitId(),
            commande.quantite(),
            commande.dateLivraison(),
            commande.modePaiement(),
            commande.delaiLivraison()
        );

        commandes.put(id, updatedCommande);
        return updatedCommande;
    }

    /**
     * Delete an order by its ID
     * @param id the ID of the order to delete
     * @return true if the order was deleted, false otherwise
     */
    public boolean delete(Integer id) {
        Commande commande = commandes.get(id);
        if (commande == null) {
            return false;
        }

        // Return the quantity to the product's stock
        produitService.updateStock(commande.produitId(), commande.quantite());

        // Remove the order
        commandes.remove(id);
        return true;
    }

    /**
     * Get all orders for a specific product
     * @param produitId the product ID
     * @return list of orders for the specified product
     */
    public List<Commande> findByProduitId(Integer produitId) {
        return commandes.values().stream()
                .filter(commande -> commande.produitId().equals(produitId))
                .toList();
    }
}
