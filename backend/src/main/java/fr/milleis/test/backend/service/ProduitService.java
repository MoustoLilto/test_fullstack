package fr.milleis.test.backend.service;

import fr.milleis.test.backend.model.Categorie;
import fr.milleis.test.backend.model.Produit;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ProduitService {

    private final Map<Integer, Produit> produits = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    /**
     * Create a new product
     * @param produit the product to create (id will be generated)
     * @return the created product with generated id
     */
    public Produit create(Produit produit) {
        // Generate a new ID if not provided
        Integer id = produit.id() != null ? produit.id() : idGenerator.getAndIncrement();

        // Create a new Produit with the generated ID
        Produit newProduit = new Produit(
            id,
            produit.nom(),
            produit.reference(),
            produit.categorie(),
            produit.prix(),
            produit.delaiDeLivraison(),
            produit.stock()
        );

        produits.put(id, newProduit);
        return newProduit;
    }

    /**
     * Get all products
     * @return list of all products
     */
    public List<Produit> findAll() {
        return new ArrayList<>(produits.values());
    }

    /**
     * Get a product by its ID
     * @param id the product ID
     * @return the product if found, null otherwise
     */
    public Produit findById(Integer id) {
        return produits.get(id);
    }

    /**
     * Update an existing product
     * @param id the ID of the product to update
     * @param produit the updated product data
     * @return the updated product if found, null otherwise
     */
    public Produit update(Integer id, Produit produit) {
        if (!produits.containsKey(id)) {
            return null;
        }

        Produit updatedProduit = new Produit(
            id,
            produit.nom(),
            produit.reference(),
            produit.categorie(),
            produit.prix(),
            produit.delaiDeLivraison(),
            produit.stock()
        );

        produits.put(id, updatedProduit);
        return updatedProduit;
    }

    /**
     * Delete a product by its ID
     * @param id the ID of the product to delete
     * @return true if the product was deleted, false otherwise
     */
    public boolean delete(Integer id) {
        return produits.remove(id) != null;
    }

    /**
     * Get all products grouped by category
     * @return map of products grouped by category
     */
    public Map<Categorie, List<Produit>> findAllGroupedByCategorie() {
        return produits.values().stream()
                .collect(Collectors.groupingBy(Produit::categorie));
    }
}
