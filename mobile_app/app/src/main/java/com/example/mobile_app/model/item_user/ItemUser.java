package com.example.mobile_app.model.item_user;

public class ItemUser {
    private int id;
    private String name;
    private String firstname;
    private String email;
    private int role;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }

    public ItemUser(int id, String name, String firstname, String email, int role) {
        this.id = id;
        this.name = name;
        this.firstname = firstname;
        this.email = email;
        this.role = role;
    }
}
