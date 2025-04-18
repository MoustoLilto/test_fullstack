export enum CategorieProduit {
  STANDARD = 'STANDARD',
  SUR_COMMANDE = 'SUR_COMMANDE',
}

export interface Produit {
  id: number;
  nom: string;
  reference: string;
  categorie: CategorieProduit;
  prix: number;
  stock?: number; // Uniquement pour STANDARD
  delaiLivraison?: number; // Uniquement pour SUR_COMMANDE
}
