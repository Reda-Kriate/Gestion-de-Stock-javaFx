package com.tp.APP1.models;

/**
 * Classe modèle représentant un utilisateur dans le système d'authentification.
 * Cette classe encapsule les données d'un utilisateur et fournit des méthodes
 * pour accéder et modifier ces données.
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String role;

    /**
     * Constructeur par défaut
     */
    public User() {
    }

    /**
     * Constructeur avec tous les attributs sauf l'id
     * 
     * @param username Le nom d'utilisateur
     * @param password Le mot de passe (devrait être hashé avant stockage)
     * @param role     Le rôle de l'utilisateur (ex: "admin", "user")
     */
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Constructeur avec tous les attributs
     * 
     * @param id       L'identifiant unique de l'utilisateur
     * @param username Le nom d'utilisateur
     * @param password Le mot de passe (devrait être hashé avant stockage)
     * @param role     Le rôle de l'utilisateur (ex: "admin", "user")
     */
    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Obtient l'identifiant de l'utilisateur
     * 
     * @return L'identifiant de l'utilisateur
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant de l'utilisateur
     * 
     * @param id Le nouvel identifiant de l'utilisateur
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtient le nom d'utilisateur
     * 
     * @return Le nom d'utilisateur
     */
    public String getUsername() {
        return username;
    }

    /**
     * Définit le nom d'utilisateur
     * 
     * @param username Le nouveau nom d'utilisateur
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtient le mot de passe
     * 
     * @return Le mot de passe
     */
    public String getPassword() {
        return password;
    }

    /**
     * Définit le mot de passe
     * 
     * @param password Le nouveau mot de passe
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtient le rôle de l'utilisateur
     * 
     * @return Le rôle de l'utilisateur
     */
    public String getRole() {
        return role;
    }

    /**
     * Définit le rôle de l'utilisateur
     * 
     * @param role Le nouveau rôle de l'utilisateur
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Retourne une représentation textuelle de l'utilisateur
     * 
     * @return Une chaîne de caractères représentant l'utilisateur
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}