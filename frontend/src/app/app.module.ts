import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { ListeProduitsComponent } from './components/produits/liste-produits/liste-produits.component';
import { DetailProduitComponent } from './components/produits/detail-produit/detail-produit.component';
import { FormulaireCommandeComponent } from './components/commandes/formulaire-commande/formulaire-commande.component';

@NgModule({
  declarations: [
    AppComponent,
    ListeProduitsComponent,
    DetailProduitComponent,
    FormulaireCommandeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
