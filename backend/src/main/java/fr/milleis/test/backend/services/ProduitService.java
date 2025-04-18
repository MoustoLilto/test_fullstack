package fr.milleis.test.backend.service;

import fr.milleis.test.backend.exceptions.ProduitNotFoundException;
import fr.milleis.test.backend.model.Produit;
import fr.milleis.test.backend.repository.ProduitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitService {
    private final ProduitRepository produitRepository;

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public Produit getProduitById(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new ProduitNotFoundException("Produit avec l'ID " + id + " non trouvé"));
    }

    public Produit createProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    public Produit updateProduit(Long id, Produit produit) {
        Produit existingProduit = getProduitById(id);
        existingProduit.setNom(produit.getNom());
        existingProduit.setReference(produit.getReference());
        existingProduit.setCategorie(produit.getCategorie());
        existingProduit.setPrix(produit.getPrix());
        existingProduit.setStock(produit.getStock());
        existingProduit.setDelaiLivraison(produit.getDelaiLivraison());
        return produitRepository.save(existingProduit);
    }

    public void deleteProduit(Long id) {
        Produit produit = getProduitById(id);
        produitRepository.delete(produit);
    }
}