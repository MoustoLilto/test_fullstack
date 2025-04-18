package fr.milleis.test.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents a product in the system
 */
@Schema(description = "Product information")
public record Produit(
    @Schema(description = "Unique identifier of the product", example = "1")
    Integer id,

    @Schema(description = "Name of the product", example = "Laptop")
    String nom,

    @Schema(description = "Reference code of the product", example = "REF001")
    String reference,

    @Schema(description = "Category of the product", example = "STANDARD")
    Categorie categorie,

    @Schema(description = "Price of the product", example = "999.99")
    Float prix,

    @Schema(description = "Delivery time in days", example = "5")
    Integer delaiDeLivraison,

    @Schema(description = "Available stock quantity", example = "10")
    Integer stock
) {
}
