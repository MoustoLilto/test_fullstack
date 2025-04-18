package fr.milleis.test.backend.config;

import fr.milleis.test.backend.model.CategorieProduit;
import fr.milleis.test.backend.model.Produit;
import fr.milleis.test.backend.repository.ProduitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(ProduitRepository produitRepository) {
        return args -> {
            // Création de produits
            Produit produit1 = new Produit();
            produit1.setNom("Ordinateur Portable");
            produit1.setReference("REF123");
            produit1.setCategorie(CategorieProduit.STANDARD);
            produit1.setPrix(1200.50);
            produit1.setStock(10);
            produit1.setDelaiLivraison(3);

            Produit produit2 = new Produit();
            produit2.setNom("Chaise de Bureau");
            produit2.setReference("REF456");
            produit2.setCategorie(CategorieProduit.SUR_COMMANDE);
            produit2.setPrix(150.75);
            produit2.setStock(25);
            produit2.setDelaiLivraison(5);

            Produit produit3 = new Produit();
            produit3.setNom("Smartphone");
            produit3.setReference("REF789");
            produit3.setCategorie(CategorieProduit.STANDARD);
            produit3.setPrix(800.00);
            produit3.setStock(15);
            produit3.setDelaiLivraison(2);

            // Sauvegarde dans la base
            produitRepository.save(produit1);
            produitRepository.save(produit2);
            produitRepository.save(produit3);
        };
    }
}