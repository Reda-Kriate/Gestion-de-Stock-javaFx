package com.tp.APP1.dao;

import com.tp.APP1.models.Client;
import com.tp.APP1.models.Product;
import com.tp.APP1.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {



    private static final String TABLE_NAME = "produits";


    @Override
    public boolean add(Product product) throws SQLException {
        String sql =  "INSERT INTO " + TABLE_NAME + " (name, prix, quantite, status) VALUES (?, ?, ?, ?)";


        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getStock());
            statement.setString(4, "en_attente"); // produit ajouté par le client


            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    @Override
    public boolean update(String oldName, Product product) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET name = ?, prix = ?, quantite = ? WHERE name = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getStock());
            statement.setString(4, oldName);
            
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    @Override
    public boolean delete(String name) throws SQLException {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE name = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, name);
            
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Product getByName(String name) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE name = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, name);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractProductFromResultSet(resultSet);
                }
            }
        }
        
        return null;
    }

    @Override
    public List<Product> getAll() throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME ;
        List<Product> products = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                products.add(extractProductFromResultSet(resultSet));
            }
        }

        return products;
    }

    @Override
    public List<Product> searchByName(String searchTerm) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE name LIKE ?";
        List<Product> products = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, "%" + searchTerm + "%");
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(extractProductFromResultSet(resultSet));
                }
            }
        }
        
        return products;
    }

    @Override
    public boolean exists(String name) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE name = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, name);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        
        return false;
    }

    private Product extractProductFromResultSet(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("name");
        double price = resultSet.getDouble("prix");
        int quantity = resultSet.getInt("quantite");
        String status = resultSet.getString("status"); // <-- nouveau champ

        Product product = new Product(name, price, quantity);
        product.setStatus(status);
        return product;

    }
    public List<Product> getByStatus(String status) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE status = ?";
        List<Product> products = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(extractProductFromResultSet(resultSet));
                }
            }
        }

        return products;
    }
    public boolean updateStatus(String name, String status) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET status = ? WHERE name = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setString(2, name);

            return statement.executeUpdate() > 0;
        }
    }
    @Override
    public List<Product> getAllValidatedProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE status = 'validé'";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id_prod");
                String name = rs.getString("name");
                double price = rs.getDouble("prix");
                int quantity = rs.getInt("quantite");
                String status = rs.getString("status");

                Product p = new Product(id, name, price, quantity);
                p.setStatus(status);
                products.add(p);
            }
        }

        return products;
    }

    @Override
    public boolean updateQuantity(int productId, int quantityToSubtract) throws SQLException {
        String sql = "UPDATE products SET quantity = quantity - ? WHERE id = ? AND quantity >= ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantityToSubtract);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantityToSubtract);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean addPurchase(Client client, Product product, int quantity) throws SQLException {
        String sql = "INSERT INTO purchases (client_id, product_id, quantity, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, client.getId());
            stmt.setInt(2, product.getId());
            stmt.setInt(3, quantity);
            stmt.setString(4, "en_attente");

            return stmt.executeUpdate() > 0;
        }
    }

//    @Override
//    public boolean validatePurchase(int achatId) throws SQLException {
//        String sqlUpdateAchat = "UPDATE purchases SET status = 'validé' WHERE id = ?";
//        String sqlGetQuantite = "SELECT product_id, quantity FROM purchases WHERE id = ?";
//        String sqlUpdateStock = "UPDATE produits SET quantite = quantite - ? WHERE id_prod = ?";
//
//        try (Connection conn = DatabaseConnection.getConnection()) {
//            conn.setAutoCommit(false); // transaction
//
//            try (
//                    PreparedStatement stmtGet = conn.prepareStatement(sqlGetQuantite);
//                    PreparedStatement stmtUpdateAchat = conn.prepareStatement(sqlUpdateAchat);
//                    PreparedStatement stmtUpdateStock = conn.prepareStatement(sqlUpdateStock)
//            ) {
//                stmtGet.setInt(1, achatId);
//                ResultSet rs = stmtGet.executeQuery();
//                if (!rs.next()) return false;
//
//                int productId = rs.getInt("product_id");
//                int quantity = rs.getInt("quantity");
//
//                // Valider l’achat
//                stmtUpdateAchat.setInt(1, achatId);
//                stmtUpdateAchat.executeUpdate();
//
//                // Mettre à jour le stock
//                stmtUpdateStock.setInt(1, quantity);
//                stmtUpdateStock.setInt(2, productId);
//                stmtUpdateStock.executeUpdate();
//
//                conn.commit();
//                return true;
//            } catch (Exception e) {
//                conn.rollback();
//                throw e;
//            }
//        }
//    }
@Override
public boolean validatePurchase(int achatId) throws SQLException {
    String sqlUpdateAchat = "UPDATE purchases SET status = 'validé' WHERE id = ?";
    String sqlGetQuantite = "SELECT product_id, quantity FROM purchases WHERE id = ?";
    String sqlUpdateStock = "UPDATE produits SET quantite = quantite - ? WHERE id_prod = ?";

    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false); // Début transaction

        try (
                PreparedStatement stmtGet = conn.prepareStatement(sqlGetQuantite);
                PreparedStatement stmtUpdateAchat = conn.prepareStatement(sqlUpdateAchat);
                PreparedStatement stmtUpdateStock = conn.prepareStatement(sqlUpdateStock)
        ) {
            stmtGet.setInt(1, achatId);
            ResultSet rs = stmtGet.executeQuery();
            if (!rs.next()) {
                conn.rollback();
                return false;
            }

            int productId = rs.getInt("product_id");
            int quantity = rs.getInt("quantity");

            if (quantity <= 0) {
                conn.rollback();
                throw new SQLException("Quantité d'achat invalide.");
            }

            // Valider l'achat
            stmtUpdateAchat.setInt(1, achatId);
            stmtUpdateAchat.executeUpdate();

            // Mettre à jour le stock
            stmtUpdateStock.setInt(1, quantity);
            stmtUpdateStock.setInt(2, productId);
            stmtUpdateStock.executeUpdate();

            conn.commit();
            return true;
        } catch (Exception e) {
            conn.rollback();
            throw e;
        }
    }
}
    }

