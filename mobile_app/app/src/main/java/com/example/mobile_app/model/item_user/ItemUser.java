package com.example.mobile_app.model.item_user;

public class ItemUser {
    private int id;
    private String last_name;
    private String first_name;
    private String email;
    private int ensisaGroup;

    private String is_staff;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return last_name; }
    public void setName(String name) { this.last_name = name; }

    public String getFirstname() { return first_name; }
    public void setFirstname(String firstname) { this.first_name = firstname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getRole() { return ensisaGroup; }
    public void setRole(int role) { this.ensisaGroup = role; }

    public String getIs_staff() {
        return is_staff;
    }

    public void setIs_staff(String is_staff) {
        this.is_staff = is_staff;
    }

    public ItemUser(int id, String name, String firstname, String email, int role) {
        this.id = id;
        this.last_name = name;
        this.first_name = firstname;
        this.email = email;
        this.ensisaGroup = role;
    }
}
