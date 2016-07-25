package com.example.muhammad.exquizme;

/**
 * This class is a class model for creating user objects.
 * Created by Muhammad on 19/04/2016.
 */
class QuizUser {
    private String username;
    private String password;
    private String email;

    public QuizUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public QuizUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
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
}
