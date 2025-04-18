import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProduitService } from '../../../services/produit.service';
import { Produit } from '../../../models/produit.model';

@Component({
  selector: 'app-detail-produit',
  templateUrl: './detail-produit.component.html',
})
export class DetailProduitComponent implements OnInit {
  produit: Produit | null = null;

  constructor(
    private route: ActivatedRoute,
    private produitService: ProduitService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.produitService.getProduitById(id).subscribe((data) => {
      this.produit = data;
    });
  }
}
