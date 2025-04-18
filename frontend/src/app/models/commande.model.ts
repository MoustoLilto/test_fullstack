export interface Commande {
  produitId: number;
  quantite: number;
  dateLivraisonSouhaitee: string; // Format ISO (ex: "2025-05-15")
  adresseLivraison: string;
  modePaiement: 'COMPTANT' | 'DIFFERE';
}
