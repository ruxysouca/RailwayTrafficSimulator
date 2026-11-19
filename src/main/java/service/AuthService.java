package service;

import database.UserDAO;
import model.User;

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

}
