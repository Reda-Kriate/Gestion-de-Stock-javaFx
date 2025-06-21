-- Script SQL pour créer la base de données et la table des produits
-- Ce script doit être exécuté pour initialiser la base de données avant de lancer l'application

/*
Instructions pour exécuter ce script:
1. Ouvrez MySQL Workbench ou un autre client MySQL
2. Connectez-vous à votre serveur MySQL
3. Exécutez ce script

Ou depuis la ligne de commande:
mysql -u root -p < create_database.sql
*/

-- Création de la base de données
CREATE DATABASE IF NOT EXISTS catalogue;

-- Utilisation de la base de données
USE catalogue;

-- Création de la table des produits
CREATE TABLE IF NOT EXISTS produits (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    prix DECIMAL(10, 2) NOT NULL,
    quantite INT NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insertion de données d'exemple
INSERT INTO produits (name, prix, quantite) VALUES ('Ordinateur portable', 899.99, 15);
INSERT INTO produits (name, prix, quantite) VALUES ('Smartphone', 499.99, 30);
INSERT INTO produits (name, prix, quantite) VALUES ('Tablette', 299.99, 20);
INSERT INTO produits (name, prix, quantite) VALUES ('Écran 24"', 199.99, 10);
INSERT INTO produits (name, prix, quantite) VALUES ('Clavier sans fil', 49.99, 25);
INSERT INTO produits (name, prix, quantite) VALUES ('Souris optique', 19.99, 40);
INSERT INTO produits (name, prix, quantite) VALUES ('Casque audio', 79.99, 18);
INSERT INTO produits (name, prix, quantite) VALUES ('Imprimante laser', 249.99, 8);
INSERT INTO produits (name, prix, quantite) VALUES ('Disque dur externe 1TB', 89.99, 22);
INSERT INTO produits (name, prix, quantite) VALUES ('Webcam HD', 59.99, 12);
