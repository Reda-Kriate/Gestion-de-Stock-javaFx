-- Script SQL pour créer la table des clients
-- Ce script doit être exécuté pour ajouter la fonctionnalité de gestion des clients

/*
Instructions pour exécuter ce script:
1. Ouvrez MySQL Workbench ou un autre client MySQL
2. Connectez-vous à votre serveur MySQL
3. Exécutez ce script

Ou depuis la ligne de commande:
mysql -u root -p < create_clients_table.sql
*/

-- Utilisation de la base de données
USE catalogue;

-- Création de la table des clients
CREATE TABLE IF NOT EXISTS clients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    address VARCHAR(200),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertion de clients par défaut pour les tests
INSERT INTO clients (name, email, phone, address) VALUES ('Client Test 1', 'client1@example.com', '0123456789', '123 Rue Test, Ville Test');
INSERT INTO clients (name, email, phone, address) VALUES ('Client Test 2', 'client2@example.com', '9876543210', '456 Avenue Test, Ville Test');