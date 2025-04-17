package fr.milleis.test.backend.model;

import java.time.LocalDate;

public record Commande(
    Integer id,
    Integer produitId,
    Integer quantite,
    LocalDate dateLivraison,
    ModePaiement modePaiement,
    Integer delaiLivraison
) {
}