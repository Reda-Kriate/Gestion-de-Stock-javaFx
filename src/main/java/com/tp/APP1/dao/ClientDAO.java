package com.tp.APP1.dao;

import com.tp.APP1.models.Client;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface définissant les opérations d'accès aux données pour les clients.
 * Cette interface suit le pattern DAO (Data Access Object) pour
 * séparer la logique d'accès aux données de la logique métier.
 */
public interface ClientDAO {
    
    /**
     * Ajoute un nouveau client dans la base de données.
     * 
     * @param client Le client à ajouter
     * @return true si l'ajout a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean add(Client client) throws SQLException;
    
    /**
     * Met à jour un client existant dans la base de données.
     * 
     * @param client Le client avec les nouvelles valeurs
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean update(Client client) throws SQLException;
    
    /**
     * Supprime un client de la base de données.
     * 
     * @param id L'identifiant du client à supprimer
     * @return true si la suppression a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean delete(int id) throws SQLException;
    
    /**
     * Récupère un client par son identifiant.
     * 
     * @param id L'identifiant du client à récupérer
     * @return Le client correspondant ou null s'il n'existe pas
     * @throws SQLException En cas d'erreur SQL
     */
    Client getById(int id) throws SQLException;
    
    /**
     * Récupère un client par son nom.
     * 
     * @param name Le nom du client à rechercher
     * @return Le client correspondant ou null s'il n'existe pas
     * @throws SQLException En cas d'erreur SQL
     */
    Client getByName(String name) throws SQLException;
    
    /**
     * Récupère tous les clients de la base de données.
     * 
     * @return Une liste de tous les clients
     * @throws SQLException En cas d'erreur SQL
     */
    List<Client> getAll() throws SQLException;
    
    /**
     * Recherche des clients par nom.
     * 
     * @param name Le nom ou partie du nom à rechercher
     * @return Une liste des clients correspondant à la recherche
     * @throws SQLException En cas d'erreur SQL
     */
    List<Client> searchByName(String name) throws SQLException;
    Client getClientByEmailAndPassword(String email, String password) throws SQLException;
}