package com.example.muhammad.exquizme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private final static String STATISTICS_URL = "http://mbcatubig.net16.net/ExQuiz%20Me/Statistics.php";
    private final ArrayList<Entry> entries = new ArrayList<>();
    private final TextView[] categoryTextViews = new TextView[5];
    private final TextView[] scoresTextViews = new TextView[5];
    private final SQLiteDatabaseHelper statsDatabase = new SQLiteDatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        //Initialise text views
        categoryTextViews[0] = (TextView) findViewById(R.id.first_category);
        categoryTextViews[1] = (TextView) findViewById(R.id.second_category);
        categoryTextViews[2] = (TextView) findViewById(R.id.third_category);
        categoryTextViews[3] = (TextView) findViewById(R.id.fourth_category);
        categoryTextViews[4] = (TextView) findViewById(R.id.fifth_category);
        scoresTextViews[0] = (TextView) findViewById(R.id.first_score);
        scoresTextViews[1] = (TextView) findViewById(R.id.second_score);
        scoresTextViews[2] = (TextView) findViewById(R.id.third_score);
        scoresTextViews[3] = (TextView) findViewById(R.id.fourth_score);
        scoresTextViews[4] = (TextView) findViewById(R.id.fifth_score);

        TextView scoreDisplayText = (TextView) findViewById(R.id.current_score);
        String currentScore, bestScore, category;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                currentScore = null;
                bestScore = null;
                category = null;
            } else {
                currentScore = extras.getString("score");
                bestScore = extras.getString("best_score");
                category = extras.getString("best_category");
            }
        } else {
            currentScore = (String) savedInstanceState.getSerializable("score");
            bestScore = (String) savedInstanceState.getSerializable("best_score");
            category = (String) savedInstanceState.getSerializable("best_category");
        }
        scoreDisplayText.setText(currentScore);
        if (currentScore != null) {
            updateStats(Integer.parseInt(currentScore.substring(11, 13)));
        } else {
            updateStats(0);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("userLogin", this.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        getJSONFromURL(username, category, bestScore);
        updateChart();
    }

    private void updateStats(int score) {
        if (score != 0) {
            statsDatabase.addScore(score + "");
        }
        List<ArrayList<String>> statList = statsDatabase.getStats();
        for (int count = 0; count < 5; count++) {
            categoryTextViews[count].setText(statList.get(1).get(count));
            scoresTextViews[count].setText(statList.get(0).get(count));
        }
        for (int i = 0; i < statsDatabase.getScores().size(); i++) {
            entries.add(new Entry(Integer.parseInt(statsDatabase.getScores().get(i)), i));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = getIntent();
        if (intent.equals(UserProfileMenuActivity.class)) {
            startActivity(intent);
        } else {
            startActivity(new Intent(this, UserProfileMenuActivity.class));
        }
    }

    private void updateChart() {
        LineChart lineChart = (LineChart) findViewById(R.id.chart);
        LineDataSet lineDataSet = new LineDataSet(entries, "Best Score");
        lineDataSet.setDrawFilled(true);
        // creating labels
        ArrayList<String> labels = new ArrayList<>();
        for (int week = 1; week <= 52; week++) {
            labels.add(week + "");
        }
        LineData data = new LineData(labels, lineDataSet);
        lineChart.setData(data); // set the data and list of labels into chart

        lineChart.setDescription("Description");  // set the description
    }

    private void getJSONFromURL(String username, String category, String score) {
        class StatsExtractorTask extends AsyncTask<String, Void, JSONObject> {
            private static final String ROOT_OBJECT = "stats", BEST_SCORE = "score", BEST_CATEGORY = "category";
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(StatisticsActivity.this, "Loading stats...", null, true, true);
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
                    URL urlObj = new URL(STATISTICS_URL);
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
        }
        StatsExtractorTask statsExtractorTask = new StatsExtractorTask();
        if (score != null || category != null) {
            statsExtractorTask.execute(username, score, category);
        } else {
            statsExtractorTask.execute(username);
        }
    }
}