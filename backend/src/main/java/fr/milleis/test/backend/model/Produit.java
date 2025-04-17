package fr.milleis.test.backend.model;

public record Produit(Integer id, String nom, String reference, Categorie categorie, Float prix, Integer delaiDeLivraison, Integer stock) {
}
