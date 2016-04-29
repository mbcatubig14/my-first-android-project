package com.example.muhammad.exquizme;

/**
 * This class is a class model for creating user objects.
 * Created by Muhammad on 19/04/2016.
 */
class QuizUser {
    final String username;
    final String password;
    String email;

    public QuizUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public QuizUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
