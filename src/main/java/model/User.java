package model;

public class User {
    private int id;
    private String username;
    private String passwordHash;
    private UserRole role;

    public User(int id, String username, String passwordHash, UserRole role){
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole(){
        return role;
    }

    public boolean verifyPassword(String plainPassword){
        if (plainPassword == null || this.passwordHash == null)
            return false;

        return this.passwordHash.equals(plainPassword);
    }

    public boolean canModifySchedules(){
        return this.role == UserRole.ADMIN;
    }
}
