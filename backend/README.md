# Product API Documentation

This document provides information about the Product API endpoints and how to use them.

## Base URL

All API endpoints are prefixed with `/api/produits`

## Endpoints

### Get All Products

- **URL**: `/api/produits`
- **Method**: `GET`
- **Response**: List of all products
- **Status Codes**:
  - `200 OK`: Successfully retrieved the list of products

Example response:
```json
[
  {
    "id": 1,
    "nom": "Ordinateur portable",
    "reference": "ORD-001",
    "categorie": "STANDARD",
    "prix": 999.99,
    "delaiDeLivraison": 2,
    "stock": 50
  },
  {
    "id": 2,
    "nom": "Smartphone",
    "reference": "SMART-001",
    "categorie": "STANDARD",
    "prix": 599.99,
    "delaiDeLivraison": 1,
    "stock": 100
  }
]
```

### Get Product by ID

- **URL**: `/api/produits/{id}`
- **Method**: `GET`
- **URL Parameters**: `id=[integer]` where `id` is the ID of the product
- **Response**: Product with the specified ID
- **Status Codes**:
  - `200 OK`: Successfully retrieved the product
  - `404 Not Found`: Product with the specified ID was not found

Example response:
```json
{
  "id": 1,
  "nom": "Ordinateur portable",
  "reference": "ORD-001",
  "categorie": "STANDARD",
  "prix": 999.99,
  "delaiDeLivraison": 2,
  "stock": 50
}
```

### Create Product

- **URL**: `/api/produits`
- **Method**: `POST`
- **Request Body**: Product object (without ID or with null ID)
- **Response**: Created product with generated ID
- **Status Codes**:
  - `201 Created`: Successfully created the product

Example request:
```json
{
  "nom": "Tablette",
  "reference": "TAB-001",
  "categorie": "STANDARD",
  "prix": 399.99,
  "delaiDeLivraison": 3,
  "stock": 75
}
```

Example response:
```json
{
  "id": 4,
  "nom": "Tablette",
  "reference": "TAB-001",
  "categorie": "STANDARD",
  "prix": 399.99,
  "delaiDeLivraison": 3,
  "stock": 75
}
```

### Update Product

- **URL**: `/api/produits/{id}`
- **Method**: `PUT`
- **URL Parameters**: `id=[integer]` where `id` is the ID of the product to update
- **Request Body**: Updated product object
- **Response**: Updated product
- **Status Codes**:
  - `200 OK`: Successfully updated the product
  - `404 Not Found`: Product with the specified ID was not found

Example request:
```json
{
  "nom": "Tablette Pro",
  "reference": "TAB-001",
  "categorie": "STANDARD",
  "prix": 499.99,
  "delaiDeLivraison": 3,
  "stock": 50
}
```

Example response:
```json
{
  "id": 4,
  "nom": "Tablette Pro",
  "reference": "TAB-001",
  "categorie": "STANDARD",
  "prix": 499.99,
  "delaiDeLivraison": 3,
  "stock": 50
}
```

### Delete Product

- **URL**: `/api/produits/{id}`
- **Method**: `DELETE`
- **URL Parameters**: `id=[integer]` where `id` is the ID of the product to delete
- **Response**: No content
- **Status Codes**:
  - `204 No Content`: Successfully deleted the product
  - `404 Not Found`: Product with the specified ID was not found

### Get Products Grouped by Category

- **URL**: `/api/produits/grouped-by-categorie`
- **Method**: `GET`
- **Response**: Map of products grouped by category
- **Status Codes**:
  - `200 OK`: Successfully retrieved the products grouped by category

Example response:
```json
{
  "STANDARD": [
    {
      "id": 1,
      "nom": "Ordinateur portable",
      "reference": "ORD-001",
      "categorie": "STANDARD",
      "prix": 999.99,
      "delaiDeLivraison": 2,
      "stock": 50
    },
    {
      "id": 2,
      "nom": "Smartphone",
      "reference": "SMART-001",
      "categorie": "STANDARD",
      "prix": 599.99,
      "delaiDeLivraison": 1,
      "stock": 100
    }
  ],
  "SUR_COMMANDE": [
    {
      "id": 3,
      "nom": "Serveur personnalisé",
      "reference": "SERV-001",
      "categorie": "SUR_COMMANDE",
      "prix": 2999.99,
      "delaiDeLivraison": 15,
      "stock": 0
    }
  ]
}
```

## Data Model

The Product object has the following properties:

- `id` (Integer): Unique identifier for the product
- `nom` (String): Name of the product
- `reference` (String): Reference code of the product
- `categorie` (Enum): Category of the product, can be either `STANDARD` or `SUR_COMMANDE`
- `prix` (Float): Price of the product
- `delaiDeLivraison` (Integer): Delivery time in days
- `stock` (Integer): Available stock quantity

## Notes

- This API uses an in-memory storage, so all data will be lost when the application is restarted.
- The application initializes with some sample products for testing purposes.
