package com.fedormamaevv.SpringProjectTemp;

public class User {
    private String username;
    private String password;
    private int age;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public User(String username, String password, int age) {
        this.username = username;
        this.password = password;
        this.age = age;
    }
    public User() { }

    public User secureReturn() { return new User(username, null, age); }
}
