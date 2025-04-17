package fr.milleis.test.backend.config;

import fr.milleis.test.backend.model.Categorie;
import fr.milleis.test.backend.model.Produit;
import fr.milleis.test.backend.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(@Autowired ProduitService produitService) {
        return args -> {
            // Add some sample products
            produitService.create(new Produit(
                null, 
                "Ordinateur portable", 
                "ORD-001", 
                Categorie.STANDARD, 
                999.99f, 
                2, 
                50
            ));
            
            produitService.create(new Produit(
                null, 
                "Smartphone", 
                "SMART-001", 
                Categorie.STANDARD, 
                599.99f, 
                1, 
                100
            ));
            
            produitService.create(new Produit(
                null, 
                "Serveur personnalisé", 
                "SERV-001", 
                Categorie.SUR_COMMANDE, 
                2999.99f, 
                15, 
                0
            ));
            
            System.out.println("Sample products initialized!");
        };
    }
}