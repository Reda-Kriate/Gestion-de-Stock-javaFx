package com.tp.APP1.utils;

import java.sql.*;

/**
 * Classe utilitaire pour gérer les connexions à la base de données.
 * Cette classe fournit une méthode centralisée pour obtenir une connexion
 * à la base de données MySQL.
 */
public class DatabaseConnection {

    // Informations liées à la base de données (config centralisée)
    private static final String URL = "jdbc:mysql://localhost:3307/catalogue?serverTimezone=Europe/Paris";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Méthode utilitaire pour obtenir une connexion à la base de données.
     * @return Connection : la connexion à la base de données.
     * @throws RuntimeException en cas d'erreur de connexion.
     */
    public static Connection getConnection() {
        // Crée et renvoie une nouvelle connexion
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new RuntimeException("Erreur de connexion à la base de données: " + e.getMessage(), e);
        }
    }
}