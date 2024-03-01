package de.krieger.management.security;

public enum Role {
    ADMIN("ADMIN"),
    AUTHOR("AUTHOR");

    private String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
