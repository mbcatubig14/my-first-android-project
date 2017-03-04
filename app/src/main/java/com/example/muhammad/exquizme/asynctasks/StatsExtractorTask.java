package com.example.muhammad.exquizme.asynctasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.muhammad.exquizme.SQLiteDatabaseHelper;
import com.example.muhammad.exquizme.WebRequestURLConstants;
import com.example.muhammad.exquizme.activities.StatisticsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by mbcat on 24/02/2017.
 */

public class StatsExtractorTask extends AsyncTask<String, Void, JSONObject> {
    private static final String ROOT_OBJECT = "stats", BEST_SCORE = "score", BEST_CATEGORY = "category";
    private ProgressDialog loading;

    private Activity activity = new StatisticsActivity();

    private SQLiteDatabaseHelper statsDatabase;

    public StatsExtractorTask(Activity activity) {
        this.activity = activity;
        statsDatabase = SQLiteDatabaseHelper.createSQLiteDbHelper(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(activity, "Loading stats...", null, true, true);
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        HashMap<String, String> data = new HashMap<>();
        data.put("quiz_username", params[0]);
        if (params.length > 1) {
            data.put("best_score", params[1]);
            data.put("best_category", params[2]);
        }

        HttpURLConnection conn = null;
        StringBuilder result = null;
        StringBuilder sbParams = new StringBuilder();
        JSONObject jObj = null;

        int i = 0;
        for (String key : data.keySet()) {
            try {
                if (i != 0) {
                    sbParams.append("&");
                }
                sbParams.append(key).append("=").append(URLEncoder.encode(data.get(key), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }

        // request method is POST
        try {
            URL urlObj = new URL(WebRequestURLConstants.STATISTICS_URL);
            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.connect();

            String paramsString = sbParams.toString();
            Log.d("key", paramsString);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(paramsString);
            wr.flush();
            wr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //Receive the response from the server
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            Log.d("JSON Parser", "result: " + result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.disconnect();

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(result.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        if (jObj != null) {
            Log.d("JSON result", jObj.toString());
            return jObj;
        }

        // return JSON Object
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonData) {
        super.onPostExecute(jsonData);
        loading.dismiss();
        //Log.d("jsonData", jsonData);
        if (jsonData != null) {
            try {
                JSONObject jsonObjectStats = jsonData.getJSONObject(ROOT_OBJECT);

                JSONArray jsonArrayScore = jsonObjectStats.getJSONArray(BEST_SCORE);
                JSONArray jsonArrayCategory = jsonObjectStats.getJSONArray(BEST_CATEGORY);
                String[] scores = new String[jsonArrayScore.length()];
                String[] categories = new String[jsonArrayCategory.length()];

                //statsDatabase = new SQLiteDatabaseHelper(getApplicationContext());
                int i = 0;
                while (i < 5) {
                    scores[i] = jsonArrayScore.getString(i);
                    categories[i] = jsonArrayCategory.getString(i);
                    statsDatabase.addStats(scores[i], categories[i]);
                    i++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public SQLiteDatabaseHelper getStatsDatabase() {
        return statsDatabase;
    }

    public void setStatsDatabase(SQLiteDatabaseHelper statsDatabase) {
        this.statsDatabase = statsDatabase;
    }
}
