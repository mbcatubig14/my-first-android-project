package com.example.muhammad.exquizme.asynctasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.muhammad.exquizme.PostRequestHandler;
import com.example.muhammad.exquizme.WebRequestURLConstants;
import com.example.muhammad.exquizme.activities.LoginUserActivity;
import com.example.muhammad.exquizme.activities.UserProfileMenuActivity;
import com.example.muhammad.exquizme.entities.QuizUser;

import java.util.HashMap;

/**
 * Created by mbcat on 24/02/2017.
 */

public class LoginUser extends AsyncTask<String, Void, String> {

    private Activity activity = new LoginUserActivity();

    private QuizUser user;

    public LoginUser(Activity activity){
        this.activity = activity;
    }

    ProgressDialog loading;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(activity, "Please Wait", null, true, true);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        loading.dismiss();
        if (s.equalsIgnoreCase("Successfully logged in")) {
            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
            SharedPreferences settings = activity.getSharedPreferences("userLogin", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("username", user.getUsername());
            editor.apply();
            Intent intent = new Intent(activity, UserProfileMenuActivity.class);
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        HashMap<String, String> data = new HashMap<>();
        data.put("quiz_username", params[0]);
        data.put("quiz_userpassword", params[1]);

        return PostRequestHandler.sendPostRequest(WebRequestURLConstants.LOGIN_URL, data);
    }

    public QuizUser getUser() {
        return user;
    }

    public void setUser(QuizUser user) {
        this.user = user;
    }
}
