import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Commande } from '../../../models/commande.model';

@Component({
  selector: 'app-formulaire-commande',
  templateUrl: './formulaire-commande.component.html',
})
export class FormulaireCommandeComponent {
  commandeForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.commandeForm = this.fb.group({
      produitId: ['', Validators.required],
      quantite: [1, [Validators.required, Validators.min(1)]],
      dateLivraisonSouhaitee: ['', Validators.required],
      adresseLivraison: ['', Validators.required],
      modePaiement: ['COMPTANT', Validators.required],
    });
  }

  onSubmit(): void {
    if (this.commandeForm.valid) {
      const commande: Commande = this.commandeForm.value;
      console.log('Commande soumise:', commande);
    }
  }
}
