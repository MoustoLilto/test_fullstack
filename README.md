# Test Technique Développeur Java/Angular

## Objectif
Créer une application de gestion de comptes bancaires composée d'un backend en Java 17 et d'un frontend en Angular 18.

__Il faudra le push sur un github public et donner le lien du repo.__

## Technologies requises
- **Backend** : Java 17, Spring Boot
- **Frontend** : Angular 18

## Architecture générale
L'application sera composée de deux parties distinctes :
- Un web service RESTful en Java 
- Une application frontend en Angular

## Spécifications fonctionnelles

### 1. Gestion des comptes utilisateur

#### Types de comptes
- **Compte courant** : Compte standard, débitable
- **Compte épargne** : Compte avec intérêts annuels, non débitable

#### Structure de données d'un compte
| Champ       | Type        | Description                                                       |
| ----------- | ----------- | ----------------------------------------------------------------- |
| id          | number | Identifiant unique du compte                                      |
| nom         | String      | Nom du compte                                                     |
| iban        | String      | IBAN du compte                                                    |
| type        | Enum        | COURANT ou EPARGNE                                                |
| montant     | Decimal     | Solde du compte                                                   |
| tauxInteret | Decimal     | Uniquement pour les comptes épargne, pourcentage d'intérêt annuel |

### 2. Fonctionnalités à implémenter

#### Visualisation des comptes
- **Liste des comptes** : Afficher tous les comptes d'un utilisateur,Regrouper les comptes par type (courant/épargne)
- **Vue détaillée** : Page dédiée pour afficher les détails d'un compte spécifique

#### Virements entre comptes
- **Sélection des comptes** : 
  - Compte source (débitable)
  - Compte destination (créditable)
- **Paramètres du virement** : 
  - Montant
  - Date d'exécution
  - Périodicité (mensuel, annuel), peut être null pour un virement ponctuel. 
- **Validations** :
  - Vérification de la provision suffisante
  - Impossibilité de débiter un compte épargne

#### Gestion des erreurs
- **Format des erreurs** : 
  - Code d'erreur codifié (ex: ER012345)
  - Message descriptif
- **Types d'erreurs à gérer** :
  - Provision insuffisante
  - Compte non débitable (compte épargne)
  - Montant invalide
  - Erreurs de validation des données ( compte inexistant pour ce client, mauvais compte débit ou crédit )

## Spécifications techniques

### Backend (Java 17)

#### Exemple de structure de réponse pour les comptes
```json
[
  {
  "id": 12345678,
  "nom": "Compte principal",
  "iban": "FR7630001007941234567890185",
  "type": "COURANT",
  "montant": 1250.75
}
]
```

#### Exemple de structure de réponse pour les comptes épargne
```json
[{
  "id": 98765432,
  "nom": "Livret A",
  "iban": "FR7630004000031234567890143",
  "type": "EPARGNE",
  "montant": 5000.00,
  "tauxInteret": 3.0
}]
```

#### Exemple de structure pour la requête de virement ponctuel
```json
{
  "compteSourceId": 12345678,
  "compteDestinationId": 98765432,
  "montant": 150.00,
  "dateExecution": "2025-04-15",
}
```
OU 
#### Exemple de structure pour la requête de virement périodique
```json
{
  "compteSourceId": 12345678,
  "compteDestinationId": 98765432,
  "montant": 150.00,
  "periodicite": "MENSUEL" // null, MENSUEL, ANNUEL
}
```

#### Structure des réponses d'erreur
```json
{
  "code": "ER012345",
  "message": "Provision insuffisante sur le compte source"
}
```

### Frontend (Angular 18)

#### Pages à implémenter
1. **Liste des comptes** - Affichage de tous les comptes regroupés par type
2. **Détail d'un compte** - Affichage détaillé des informations d'un compte
3. **Formulaire de virement** - Interface pour effectuer un virement

#### Fonctionnalités UI requises
- Navigation entre les pages
- Validation côté client
- Affichage des messages d'erreur
- Gestion des états de chargement (loader)

## Livraison attendue

- Code source complet (backend et frontend)

## Bonus (optionnel)

- Authentification des utilisateurs
- Historique des transactions
- Filtres et recherche dans la liste des comptes
- TDD
- documentation API (ex : swagger )
