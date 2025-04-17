package fr.milleis.test.backend.model;

public enum Categorie {
    STANDARD("STANDARD"),
    SUR_COMMANDE("SUR_COMMANDE");

    private final String categorie;

    Categorie(String categorie) {
        this.categorie = categorie;
    }
}
