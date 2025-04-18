package fr.milleis.test.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents the payment methods available for orders
 */
@Schema(description = "Payment method")
public enum ModePaiement {
    /**
     * Payment by credit/debit card
     */
    @Schema(description = "Credit/debit card payment")
    CARTE_BANCAIRE("CARTE_BANCAIRE"),

    /**
     * Payment by bank transfer
     */
    @Schema(description = "Bank transfer payment")
    VIREMENT("VIREMENT"),

    /**
     * Payment in cash
     */
    @Schema(description = "Cash payment")
    ESPECES("ESPECES"),

    /**
     * Payment by check
     */
    @Schema(description = "Check payment")
    CHEQUE("CHEQUE");

    private final String modePaiement;

    ModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }
}
