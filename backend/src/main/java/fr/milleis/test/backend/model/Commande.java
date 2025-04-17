package fr.milleis.test.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

/**
 * Represents an order in the system
 */
@Schema(description = "Order information")
public record Commande(
    @Schema(description = "Unique identifier of the order", example = "1")
    Integer id,

    @Schema(description = "ID of the product being ordered", example = "1")
    Integer produitId,

    @Schema(description = "Quantity of the product ordered", example = "2")
    Integer quantite,

    @Schema(description = "Expected delivery date", example = "2023-12-31")
    LocalDate dateLivraison,

    @Schema(description = "Payment method used for the order", example = "CARTE_BANCAIRE")
    ModePaiement modePaiement,

    @Schema(description = "Delivery time in days", example = "5")
    Integer delaiLivraison
) {
}
