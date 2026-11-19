package service;

import database.UserDAO;
import model.User;
import model.UserRole;

import java.sql.SQLException;

public class AuthService {
    private final UserDAO userDAO;

    public AuthService() {
        userDAO = new UserDAO();
    }

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User login(String username, String password) throws SQLException {
        if (username == null || username.isBlank() || password == null) {
            return null;
        }

        User user = userDAO.findByUsername(username.trim());
        if (user != null && user.verifyPassword(password)) {
            return user;
        }

        return null;
    }

    public boolean registerUser(String username, String password, UserRole role) throws SQLException {
        if (username == null || username.isBlank() || password == null || password.isBlank() || role == null) {
            return false;
        }

        String trimmedUsername = username.trim();
        if (userDAO.findByUsername(trimmedUsername) != null) {
            return false;
        }

        userDAO.insertUser(trimmedUsername, password, role);
        return true;
    }
}
