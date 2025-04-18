package fr.milleis.test.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents the categories of products
 */
@Schema(description = "Product category")
public enum Categorie {
    /**
     * Standard products that are usually in stock
     */
    @Schema(description = "Standard product")
    STANDARD("STANDARD"),

    /**
     * Products that are made to order
     */
    @Schema(description = "Made-to-order product")
    SUR_COMMANDE("SUR_COMMANDE");

    private final String categorie;

    Categorie(String categorie) {
        this.categorie = categorie;
    }
}
