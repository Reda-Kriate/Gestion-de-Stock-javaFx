# Gestion de Stock - Application JavaFX

Cette application de gestion de stock permet de gérer un inventaire de produits avec les opérations CRUD complètes (Create, Read, Update, Delete).

## Fonctionnalités

- Affichage de la liste des produits
- Ajout de nouveaux produits
- Modification des produits existants
- Suppression de produits
- Recherche de produits par nom

## Prérequis

- Java 21 ou supérieur
- Maven
- MySQL Server (version 5.7 ou supérieure)

## Configuration de la base de données

1. Assurez-vous que MySQL Server est installé et en cours d'exécution
2. Exécutez le script SQL situé dans `src/main/resources/com/tp/APP1/database/create_database.sql` pour créer la base de données et la table nécessaire
3. Si nécessaire, modifiez les paramètres de connexion à la base de données dans la classe `com.tp.APP1.utils.DatabaseConnection`

## Compilation et exécution

### Avec Maven

```bash
# Compiler le projet
mvn clean compile

# Exécuter l'application
mvn javafx:run
```

### Avec un IDE

1. Importez le projet dans votre IDE (IntelliJ IDEA, Eclipse, etc.)
2. Exécutez la classe `com.tp.APP1.StockManagementApp`

## Structure du projet

Le projet suit l'architecture MVC (Modèle-Vue-Contrôleur) :

- **Modèle** : Classes représentant les données (`com.tp.APP1.models`)
- **Vue** : Fichiers FXML définissant l'interface utilisateur (`com.tp.APP1.views`)
- **Contrôleur** : Classes gérant les interactions entre la vue et le modèle (`com.tp.APP1.controllers`)

Autres packages :
- **DAO** : Classes d'accès aux données (`com.tp.APP1.dao`)
- **Utils** : Classes utilitaires (`com.tp.APP1.utils`)

## Captures d'écran

![Interface principale](docs/screenshot.png)

## Technologies utilisées

- JavaFX pour l'interface graphique
- JDBC pour la connexion à la base de données
- CSS pour le style de l'interface
- Maven pour la gestion des dépendances et la construction du projet

## Auteur

[Votre nom]

## Licence

Ce projet est sous licence MIT.