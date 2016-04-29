package com.example.muhammad.exquizme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class LoginUserActivity extends AppCompatActivity {

    static final String USER_NAME = "USER_NAME";
    private static final String LOGIN_URL = "http://mbcatubig.net16.net/ExQuiz%20Me/Login.php";

    private EditText editTextUserName;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUserName = (EditText) findViewById(R.id.login_username);
        editTextPassword = (EditText) findViewById(R.id.login_password);

        Button buttonLogin = (Button) findViewById(R.id.login_button);

        TextView registerScreen = (TextView) findViewById(R.id.register_link);
        TextView guestPlay = (TextView) findViewById(R.id.guest_login_link);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Switching to RegisterUserActivity screen
                Intent registerIntent = new Intent(LoginUserActivity.this, RegisterUserActivity.class);
                startActivity(registerIntent);
            }
        });

        guestPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playQuizIntent = new Intent(LoginUserActivity.this, PlayQuizActivity.class);
                startActivity(playQuizIntent);
            }
        });

    }

    private void login() {
        String quizUsername = editTextUserName.getText().toString().trim();
        String quizPassword = editTextPassword.getText().toString().trim();
        loginUser(new QuizUser(quizUsername, quizPassword));
    }

    private void loginUser(final QuizUser user) {
        final String username = user.username;
        String password = user.password;

        class LoginUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginUserActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("Successfully logged in")) {
                    Toast.makeText(LoginUserActivity.this, s, Toast.LENGTH_LONG).show();
                    SharedPreferences settings = getSharedPreferences("userLogin", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("username", username);
                    editor.apply();
                    Intent intent = new Intent(LoginUserActivity.this, UserProfileMenuActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginUserActivity.this, s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                data.put("quiz_username", params[0]);
                data.put("quiz_userpassword", params[1]);

                RegisterUserRequest ruc = new RegisterUserRequest();

                return ruc.sendPostRequest(LOGIN_URL, data);
            }
        }
        LoginUser ulc = new LoginUser();
        ulc.execute(username, password);
    }

}
