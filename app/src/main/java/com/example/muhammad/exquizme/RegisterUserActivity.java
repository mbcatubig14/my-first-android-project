package com.example.muhammad.exquizme;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class RegisterUserActivity extends AppCompatActivity {

    private static final String REGISTER_URL = "http://mbcatubig.net16.net/ExQuiz%20Me/Register.php";
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = (EditText) findViewById(R.id.reg_username);
        editTextPassword = (EditText) findViewById(R.id.reg_password);
        editTextEmail = (EditText) findViewById(R.id.reg_email);

        Button buttonRegister = (Button) findViewById(R.id.btnRegister);

        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);

        //Listening to Register Button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Listening to LoginUserActivity Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Switching to LoginUserActivity Screen/closing register screen
                finish();
            }
        });
    }

    private void registerUser() {
        String quizUsername = editTextUsername.getText().toString().trim().toLowerCase();
        String quizPassword = editTextPassword.getText().toString().trim().toLowerCase();
        String quizEmail = editTextEmail.getText().toString().trim().toLowerCase();

        register(new QuizUser(quizUsername, quizPassword, quizEmail));
    }

    private void register(QuizUser user) {
        String quizUsername = user.username, quizPassword = user.password, quizEmail = user.email;
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            final RegisterUserRequest ruc = new RegisterUserRequest();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RegisterUserActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("Successfully registered")) {
                    Toast.makeText(RegisterUserActivity.this, s, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(RegisterUserActivity.this, s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> userDataParameters = new HashMap<>();
                userDataParameters.put("quiz_username", params[0]);
                userDataParameters.put("quiz_userpassword", params[1]);
                userDataParameters.put("quiz_useremail", params[2]);

                return ruc.sendPostRequest(REGISTER_URL, userDataParameters);
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(quizUsername, quizPassword, quizEmail);
    }


}
