package fr.milleis.test.backend.model;

public enum ModePaiement {
    CARTE_BANCAIRE("CARTE_BANCAIRE"),
    VIREMENT("VIREMENT"),
    ESPECES("ESPECES"),
    CHEQUE("CHEQUE");

    private final String modePaiement;

    ModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }
}