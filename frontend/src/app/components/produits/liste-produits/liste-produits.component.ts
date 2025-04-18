import { Component, OnInit } from '@angular/core';
import { ProduitService } from '../../services/produit.service';
import { Produit } from '../../models/produit.model';

@Component({
  selector: 'app-liste-produits',
  templateUrl: './liste-produits.component.html',
})
export class ListeProduitsComponent implements OnInit {
  produitsStandard: Produit[] = [];
  produitsSurCommande: Produit[] = [];

  constructor(private produitService: ProduitService) {}

  ngOnInit(): void {
    this.produitService.getProduits().subscribe((produits) => {
      this.produitsStandard = produits.filter(p => p.categorie === 'STANDARD');
      this.produitsSurCommande = produits.filter(p => p.categorie === 'SUR_COMMANDE');
    });
  }
}
