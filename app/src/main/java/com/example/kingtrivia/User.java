package com.example.kingtrivia;

public class User {
    private int isAdmin; // 0 no, 1 yes

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }
}
