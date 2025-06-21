-- Script SQL pour créer la table des utilisateurs
-- Ce script doit être exécuté pour ajouter la fonctionnalité d'authentification

/*
Instructions pour exécuter ce script:
1. Ouvrez MySQL Workbench ou un autre client MySQL
2. Connectez-vous à votre serveur MySQL
3. Exécutez ce script

Ou depuis la ligne de commande:
mysql -u root -p < create_users_table.sql
*/

-- Utilisation de la base de données
USE catalogue;

-- Création de la table des utilisateurs
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'user',
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertion d'utilisateurs par défaut
-- Note: Dans une application réelle, les mots de passe devraient être hashés
INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'admin');
INSERT INTO users (username, password, role) VALUES ('user', 'user123', 'user');