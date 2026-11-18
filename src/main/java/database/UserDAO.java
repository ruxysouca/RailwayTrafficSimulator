package database;

import model.User;
import model.UserRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public void insertUser(String username, String passwordHash, UserRole role) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, ?) ON CONFLICT (username) DO NOTHING";

        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, passwordHash);
            pstmt.setString(3, role.name());
            pstmt.executeUpdate();
        }
    }

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT id, username, password_hash, role FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String foundUsername = rs.getString("username");
                    String passwordHash = rs.getString("password_hash");
                    UserRole role = UserRole.valueOf(rs.getString("role"));

                    return new User(id, foundUsername, passwordHash, role);
                }
            }
        }

        return null;
    }
}
