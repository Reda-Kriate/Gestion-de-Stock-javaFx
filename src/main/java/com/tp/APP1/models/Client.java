package com.tp.APP1.models;

/**
 * Classe modèle représentant un client dans le système de gestion.
 * Cette classe encapsule les données d'un client et fournit des méthodes
 * pour accéder et modifier ces données.
 */
public class Client {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String password;

    /**
     * Constructeur par défaut
     */
    public Client() {
    }

    /**
     * Constructeur avec tous les attributs sauf l'id
     * 
     * @param name    Le nom du client
     * @param email   L'email du client
     * @param phone   Le numéro de téléphone du client
     * @param address L'adresse du client
     */
    public Client(String name, String email, String phone, String address, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Constructeur avec tous les attributs
     * 
     * @param id      L'identifiant unique du client
     * @param name    Le nom du client
     * @param email   L'email du client
     * @param phone   Le numéro de téléphone du client
     * @param address L'adresse du client
     */
    public Client(int id, String name, String email, String phone, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    /**
     * Obtient l'identifiant du client
     * 
     * @return L'identifiant du client
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant du client
     * 
     * @param id Le nouvel identifiant du client
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtient le nom du client
     * 
     * @return Le nom du client
     */
    public String getName() {
        return name;
    }

    /**
     * Définit le nom du client
     * 
     * @param name Le nouveau nom du client
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtient l'email du client
     * 
     * @return L'email du client
     */
    public String getEmail() {
        return email;
    }

    /**
     * Définit l'email du client
     * 
     * @param email Le nouvel email du client
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtient le numéro de téléphone du client
     * 
     * @return Le numéro de téléphone du client
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Définit le numéro de téléphone du client
     * 
     * @param phone Le nouveau numéro de téléphone du client
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Obtient l'adresse du client
     * 
     * @return L'adresse du client
     */
    public String getAddress() {
        return address;
    }

    /**
     * Définit l'adresse du client
     * 
     * @param address La nouvelle adresse du client
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Retourne une représentation textuelle du client
     * 
     * @return Une chaîne de caractères représentant le client
     */
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}