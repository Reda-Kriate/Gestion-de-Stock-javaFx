package com.tp.APP1.dao;

import com.tp.APP1.models.Client;
import com.tp.APP1.models.Product;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface définissant les opérations CRUD pour les produits.
 * Cette interface suit le pattern DAO (Data Access Object) pour
 * séparer la logique d'accès aux données de la logique métier.
 */
public interface ProductDAO {
    
    /**
     * Ajoute un nouveau produit dans la base de données.
     * 
     * @param product Le produit à ajouter
     * @return true si l'ajout a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean add(Product product) throws SQLException;
    
    /**
     * Met à jour un produit existant dans la base de données.
     * 
     * @param oldName Le nom actuel du produit à mettre à jour
     * @param product Le produit avec les nouvelles valeurs
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean update(String oldName, Product product) throws SQLException;
    
    /**
     * Supprime un produit de la base de données.
     * 
     * @param name Le nom du produit à supprimer
     * @return true si la suppression a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean delete(String name) throws SQLException;
    
    /**
     * Récupère un produit par son nom.
     * 
     * @param name Le nom du produit à récupérer
     * @return Le produit correspondant ou null s'il n'existe pas
     * @throws SQLException En cas d'erreur SQL
     */
    Product getByName(String name) throws SQLException;
    
    /**
     * Récupère tous les produits de la base de données.
     * 
     * @return Une liste de tous les produits
     * @throws SQLException En cas d'erreur SQL
     */
    List<Product> getAll() throws SQLException;
    
    /**
     * Recherche des produits dont le nom contient la chaîne spécifiée.
     * 
     * @param searchTerm Le terme de recherche
     * @return Une liste des produits correspondants
     * @throws SQLException En cas d'erreur SQL
     */
    List<Product> searchByName(String searchTerm) throws SQLException;
    
    /**
     * Vérifie si un produit existe déjà dans la base de données.
     * 
     * @param name Le nom du produit à vérifier
     * @return true si le produit existe, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean exists(String name) throws SQLException;
    boolean updateStatus(String name, String status) throws SQLException ;
    List<Product> getByStatus(String status) throws SQLException ;

    List<Product> getAllValidatedProducts() throws SQLException;

    boolean updateQuantity(int productId, int quantityToSubtract) throws SQLException;

    boolean addPurchase(Client client, Product product, int quantity) throws SQLException;

    boolean validatePurchase(int achatId) throws SQLException;
}