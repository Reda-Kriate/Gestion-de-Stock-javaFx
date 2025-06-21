# Comment Exécuter l'Application de Gestion de Stock

Voici les instructions détaillées pour exécuter l'application de gestion de stock:

## Prérequis

- Java 21 ou supérieur
- Maven
- MySQL Server (version 5.7 ou supérieure) installé et en cours d'exécution sur le port 3307
  - Si votre MySQL est configuré sur un port différent (par défaut 3306), vous devrez modifier le fichier de connexion

## Étape 1: Configuration de la Base de Données

1. Assurez-vous que MySQL Server est installé et en cours d'exécution
2. Exécutez les scripts SQL suivants dans l'ordre:
   
   a. Script pour créer la base de données et la table des produits:
   ```bash
   mysql -u root -p < src/main/resources/com/tp/APP1/database/create_database.sql
   ```
   
   b. Script pour créer la table des utilisateurs:
   ```bash
   mysql -u root -p < src/main/resources/com/tp/APP1/database/create_users_table.sql
   ```

   Alternativement, vous pouvez exécuter ces scripts via MySQL Workbench ou un autre client MySQL.

3. Vérifiez que la configuration de connexion à la base de données correspond à votre environnement:
   - Fichier: `src/main/java/com/tp/APP1/utils/DatabaseConnection.java`
   - URL par défaut: `jdbc:mysql://localhost:3307/catalogue?serverTimezone=Europe/Paris`
   - Utilisateur par défaut: `root`
   - Mot de passe par défaut: `123456`

   Si nécessaire, modifiez ces paramètres pour correspondre à votre configuration MySQL.

## Étape 2: Exécution de l'Application

### Méthode 1: Avec Maven (recommandée)

Ouvrez un terminal dans le répertoire racine du projet (où se trouve le fichier `pom.xml`) et exécutez:

```bash
# Compiler le projet
mvn clean compile

# Exécuter l'application
mvn javafx:run
```

### Méthode 2: Avec un IDE

1. Importez le projet dans votre IDE (IntelliJ IDEA, Eclipse, etc.)
2. Exécutez la classe principale: `com.tp.APP1.StockManagementApp`

## Étape 3: Connexion à l'Application

Une fois l'application démarrée, utilisez l'un des comptes suivants pour vous connecter:

- Administrateur:
  - Nom d'utilisateur: `admin`
  - Mot de passe: `admin123`

- Utilisateur standard:
  - Nom d'utilisateur: `user`
  - Mot de passe: `user123`

## Résolution des Problèmes Courants

1. **Erreur de connexion à la base de données**:
   - Vérifiez que MySQL est en cours d'exécution
   - Vérifiez que le port dans l'URL de connexion correspond à votre configuration MySQL
   - Vérifiez que les identifiants (utilisateur/mot de passe) sont corrects

2. **Erreur "JavaFX runtime components are missing"**:
   - Assurez-vous d'utiliser Maven pour exécuter l'application (`mvn javafx:run`)
   - Si vous utilisez un IDE, configurez correctement les VM arguments pour JavaFX