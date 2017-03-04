package com.example.muhammad.exquizme.asynctasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.muhammad.exquizme.PostRequestHandler;
import com.example.muhammad.exquizme.WebRequestURLConstants;
import com.example.muhammad.exquizme.activities.RegisterUserActivity;
import com.example.muhammad.exquizme.entities.QuizUser;

import java.util.HashMap;

/**
 * Created by mbcat on 24/02/2017.
 */

public class RegisterUser extends AsyncTask<String, Void, String> {

    private Activity activity = new RegisterUserActivity();

    private QuizUser user;

    public RegisterUser(Activity activity){
        this.activity = activity;
    }
    
    private ProgressDialog loading;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(activity, "Please Wait", null, true, true);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        loading.dismiss();
        if (s.equalsIgnoreCase("Successfully registered")) {
            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
            activity.finish();
        } else {
            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected String doInBackground(String... params) {

        HashMap<String, String> userDataParameters = new HashMap<>();
        userDataParameters.put("quiz_username", params[0]);
        userDataParameters.put("quiz_userpassword", params[1]);
        userDataParameters.put("quiz_useremail", params[2]);

        return PostRequestHandler.sendPostRequest(WebRequestURLConstants.REGISTER_URL, userDataParameters);
    }
}
