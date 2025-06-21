package com.tp.APP1.dao;

import com.tp.APP1.models.User;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface définissant les opérations d'accès aux données pour les utilisateurs.
 * Cette interface suit le pattern DAO (Data Access Object) pour
 * séparer la logique d'accès aux données de la logique métier.
 */
public interface UserDAO {
    
    /**
     * Ajoute un nouvel utilisateur dans la base de données.
     * 
     * @param user L'utilisateur à ajouter
     * @return true si l'ajout a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean add(User user) throws SQLException;
    
    /**
     * Met à jour un utilisateur existant dans la base de données.
     * 
     * @param user L'utilisateur avec les nouvelles valeurs
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean update(User user) throws SQLException;
    
    /**
     * Supprime un utilisateur de la base de données.
     * 
     * @param id L'identifiant de l'utilisateur à supprimer
     * @return true si la suppression a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean delete(int id) throws SQLException;
    
    /**
     * Récupère un utilisateur par son identifiant.
     * 
     * @param id L'identifiant de l'utilisateur à récupérer
     * @return L'utilisateur correspondant ou null s'il n'existe pas
     * @throws SQLException En cas d'erreur SQL
     */
    User getById(int id) throws SQLException;
    
    /**
     * Récupère un utilisateur par son nom d'utilisateur.
     * 
     * @param username Le nom d'utilisateur à rechercher
     * @return L'utilisateur correspondant ou null s'il n'existe pas
     * @throws SQLException En cas d'erreur SQL
     */
    User getByUsername(String username) throws SQLException;
    
    /**
     * Récupère tous les utilisateurs de la base de données.
     * 
     * @return Une liste de tous les utilisateurs
     * @throws SQLException En cas d'erreur SQL
     */
    List<User> getAll() throws SQLException;
    
    /**
     * Authentifie un utilisateur avec son nom d'utilisateur et son mot de passe.
     * 
     * @param username Le nom d'utilisateur
     * @param password Le mot de passe
     * @return L'utilisateur authentifié ou null si l'authentification échoue
     * @throws SQLException En cas d'erreur SQL
     */
    User authenticate(String username, String password) throws SQLException;
    
    /**
     * Vérifie si un nom d'utilisateur existe déjà dans la base de données.
     * 
     * @param username Le nom d'utilisateur à vérifier
     * @return true si le nom d'utilisateur existe, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean usernameExists(String username) throws SQLException;
}