import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListeProduitsComponent } from './components/produits/liste-produits/liste-produits.component';
import { DetailProduitComponent } from './components/produits/detail-produit/detail-produit.component';
import { FormulaireCommandeComponent } from './components/commandes/formulaire-commande/formulaire-commande.component';

const routes: Routes = [
  { path: '', redirectTo: 'produits', pathMatch: 'full' },
  { path: 'produits', component: ListeProduitsComponent },
  { path: 'produits/:id', component: DetailProduitComponent },
  { path: 'commande', component: FormulaireCommandeComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
