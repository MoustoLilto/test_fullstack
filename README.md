# Test Technique Développeur Java/Angular

## Objectif
Créer une application de gestion des employés et congés composée d'un backend en Java 17 et d'un frontend en Angular 18.
Créer une branche à votre nom et prénom (ex : test/arthur-josseau ) et push sur ce repo. 
L'utilisation de l'intelligence artificielle est autorisée. 

## Livraison attendue

- Code source complet (backend et frontend)
- Que les projets compilent. Il n'est pas nécessaire qu'ils fonctionnent. (Inutile d'implémenter un H2 ou une base de donnée, inutile de tester si le projet démarre, possibilité de mock pour le backend ou une implémentation de data jpa repository, inutile de passer du temps sur l'intégration entre le back et le front, les contrats d'interfaces et les api suffisent. )
- Vous serez jugé sur la pertinence des implémentations au vu du temps impartie ( balance qualité / délai ) et sur la quantité de points validés ci-dessous. 
- Aucunes architectures spécifiques n'est imposée. 
- Aucunes bonnes pratiques spécifiques n'est imposée. 

## Technologies requises
- **Backend** : Java 17, Spring Boot
- **Frontend** : Angular 18, tailwind

## Architecture générale
L'application sera composée de deux parties distinctes :
- Un web service RESTful en Java 17 (dossier backend)
- Une application frontend en Angular 18 (dossier frontend)

## Implémentation actuelle
- Un projet backend vide est déjà a disposition avec Spring boot et la dependence starter web
- Un projet frontend vide est déjà a disposition avec Angular 18 et la librairie tailwind

## Spécifications fonctionnelles

### 1. Gestion des employés

#### Catégories de postes
- **Cadre** : Employé avec un statut cadre et des droits spécifiques
- **Non-cadre** : Employé avec un statut non-cadre

#### Structure de données d'un employé
| Champ         | Type        | Description                                                |
| ------------- | ----------- | ---------------------------------------------------------- |
| id            | number      | Identifiant unique de l'employé                           |
| nom           | String      | Nom de l'employé                                          |
| prenom        | String      | Prénom de l'employé                                       |
| categorie     | Enum        | CADRE ou NON_CADRE                                        |
| dateEmbauche  | Date        | Date d'embauche de l'employé                              |
| soldeConges   | Decimal     | Solde de jours de congés disponibles                      |
| soldeRTT      | Decimal     | Uniquement pour les cadres, solde de jours de RTT         |

### 2. Fonctionnalités à implémenter

#### Visualisation des employés
- **Liste des employés** : Afficher tous les employés, regrouper par catégorie (cadre/non-cadre)
- **Vue détaillée** : Page dédiée pour afficher les détails d'un employé spécifique

#### Gestion des congés
- **Sélection de l'employé** : 
  - Employé demandeur de congés
- **Paramètres du congé** : 
  - Date de début
  - Date de fin
  - Type de congé (congé payé, RTT, sans solde)
- **Validations** :
  - Vérification du solde disponible
  - Respect des règles d'attribution selon la catégorie
  - Impossibilité d'attribuer des RTT à un employé non-cadre

#### Gestion des erreurs
- **Format des erreurs** : 
  - Code d'erreur codifié (ex: ER012345)
  - Message descriptif
- **Types d'erreurs à gérer** :
  - Solde insuffisant
  - Droit non applicable (RTT pour non-cadre)
  - Dates invalides
  - Erreurs de validation des données (employé inexistant, chevauchement avec un congé existant)

## Spécifications techniques

### Backend (Java 17)

#### Exemple de structure de réponse pour les employés non-cadres
```json
[
  {
  "id": 12345678,
  "nom": "Dupont",
  "prenom": "Jean",
  "categorie": "NON_CADRE",
  "dateEmbauche": "2022-09-01",
  "soldeConges": 25.0
}
]
```

#### Exemple de structure de réponse pour les employés cadres
```json
[{
  "id": 98765432,
  "nom": "Martin",
  "prenom": "Sophie",
  "categorie": "CADRE",
  "dateEmbauche": "2021-03-15",
  "soldeConges": 25.0,
  "soldeRTT": 12.0
}]
```

#### Exemple de structure pour la requête de congé (les dates peuvent être des timestamps ou des dates )
```json
{
  "employeId": 12345678,
  "dateDebut": "2025-07-15",
  "dateFin": "2025-07-30",
  "typeConge": "CONGE_PAYE"
}
```

#### Structure des réponses d'erreur
```json
{
  "code": "ER012345",
  "message": "Solde de congés insuffisant pour cette demande"
}
```

### Frontend (Angular 18)

#### Pages à implémenter
1. **Liste des employés** - Affichage de tous les employés regroupés par catégorie
2. **Détail d'un employé** - Affichage détaillé des informations d'un employé
3. **Formulaire de demande de congé** - Interface pour effectuer une demande de congé

#### Fonctionnalités UI requises
- Navigation entre les pages
- Validation côté client
- Affichage des messages d'erreur
- Gestion des états de chargement (loader)


## Bonus (optionnel)

- Authentification des utilisateurs
- Historique des demandes de congés
- Filtres et recherche dans la liste des employés
- Tests unitaires
- Documentation API (ex : swagger)
