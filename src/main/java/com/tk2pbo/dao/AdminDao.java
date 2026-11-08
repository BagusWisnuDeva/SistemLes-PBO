package com.tk2pbo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tk2pbo.model.Admin;
import com.tk2pbo.util.DatabaseConnection;
import com.tk2pbo.util.PasswordUtil;

public class AdminDao {

    public Admin login(String username, String passwordBiasa) throws SQLException {
        String sql = "SELECT id_admin, nama, email, no_telepon, username, password "
                + "FROM admin WHERE username = ? AND password = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, PasswordUtil.hashPassword(passwordBiasa));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Admin(rs.getInt("id_admin"), rs.getString("nama"), rs.getString("email"),
                            rs.getString("no_telepon"), rs.getString("username"), rs.getString("password"));
                }
            }
        }
        return null;
    }
}
