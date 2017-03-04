package com.example.muhammad.exquizme.entities;

/**
 * This class is a class model for creating user objects.
 * Created by Muhammad on 19/04/2016.
 */
public class QuizUser {
    
    private String username;
    private String password;
    private String email;

    public QuizUser(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public QuizUser(String username, String password, String email) {
        this.setUsername(username);
        this.setPassword(password);
        this.setEmail(email);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "QuizUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
