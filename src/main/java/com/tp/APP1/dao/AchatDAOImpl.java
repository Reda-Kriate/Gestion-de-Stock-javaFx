package com.tp.APP1.dao;

import com.tp.APP1.models.Achat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AchatDAOImpl implements AchatDAO {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3307/catalogue", "root", "");
    }

    @Override
    public List<Achat> getAchatsEnAttente() {
        List<Achat> achats = new ArrayList<>();

        String sql = """
            SELECT 
                p.id,
                c.name AS client_name,
                pr.Name AS product_name,
                p.quantity,
                p.status
            FROM purchases p
            JOIN clients c ON p.client_id = c.id
            JOIN produits pr ON p.product_id = pr.ID_PROD
            WHERE p.status = 'en_attente'
        """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Achat achat = new Achat(
                        rs.getInt("id"),
                        rs.getString("client_name"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getString("status")
                );
                achats.add(achat);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return achats;
    }

    @Override
    public void updateStatus(int achatId, String newStatus) {
        String sql = "UPDATE purchases SET status = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, achatId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
