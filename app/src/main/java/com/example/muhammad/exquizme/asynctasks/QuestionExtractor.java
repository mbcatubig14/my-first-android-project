package com.example.muhammad.exquizme.asynctasks;

/**
 * Created by mbcat on 25/02/2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.muhammad.exquizme.SQLiteDatabaseHelper;
import com.example.muhammad.exquizme.WebRequestURLConstants;
import com.example.muhammad.exquizme.activities.LoginUserActivity;
import com.example.muhammad.exquizme.activities.PlayQuizActivity;
import com.example.muhammad.exquizme.entities.QuizQuestion;
import com.example.muhammad.exquizme.entities.QuizUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class QuestionExtractor extends AsyncTask<Void, Void, String> {
    private static final String ROOT_OBJECT = "questions",
            QUESTION = "question",
            CHOICES = "choices",
            CATEGORY = "category";
    private ProgressDialog loading;

    private Activity activity = new PlayQuizActivity();

    public QuestionExtractor(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(activity, "Loading question...", null, true, true);
    }

    @Override
    protected String doInBackground(Void... params) {

        BufferedReader bufferedReader;
        try {
            URL url = new URL(WebRequestURLConstants.QUESTION_MANAGER_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String json;
            while ((json = bufferedReader.readLine()) != null) {
                sb.append(json).append("\n");
            }
            json = sb.toString().trim();

            return json;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String jsonData) {
        super.onPostExecute(jsonData);
        try {
            //Get the questions object
            JSONObject questions = new JSONObject(jsonData).getJSONObject(ROOT_OBJECT);
            SQLiteDatabaseHelper questionDatabase = SQLiteDatabaseHelper.createSQLiteDbHelper(activity.getApplicationContext());
            //Get the questions' objects
            for (int counter = 1; counter <= questions.length(); counter++) {

                String[] choices = new String[4];
                JSONObject theQuestionObject = questions.getJSONObject(counter + "");
                String question = theQuestionObject.getString(QUESTION),
                        category = theQuestionObject.getString(CATEGORY), correctAnswer = "";

                JSONArray theChoicesArray = theQuestionObject.getJSONArray(CHOICES);
                //Get choice object from the choices object
                extractChoices(questionDatabase, choices, question, category, correctAnswer, theChoicesArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((PlayQuizActivity) activity).displayQuestion();
        loading.dismiss();
    }

    private void extractChoices(SQLiteDatabaseHelper questionDatabase, String[] choices, String question, String category, String correctAnswer, JSONArray theChoicesArray) throws JSONException {
        for (int i = 0; i < theChoicesArray.length(); i++) {
            JSONObject choiceObject = theChoicesArray.getJSONObject(i);
            choices[i] = choiceObject.optString("choice");
            if (choiceObject.getInt("is_correct_choice") == 1) {
                correctAnswer = choices[i];
            }
        }
        //add in SQLite Database
        questionDatabase.addQuestion(new QuizQuestion(question, choices, correctAnswer, category));
    }
}
