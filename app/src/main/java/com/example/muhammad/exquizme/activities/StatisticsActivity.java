package com.example.muhammad.exquizme.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.muhammad.exquizme.R;
import com.example.muhammad.exquizme.SQLiteDatabaseHelper;
import com.example.muhammad.exquizme.asynctasks.StatsExtractorTask;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private final ArrayList<Entry> entries = new ArrayList<>();
    private final TextView[] categoryTextViews = new TextView[5];
    private final TextView[] scoresTextViews = new TextView[5];
    private final SQLiteDatabaseHelper statsDatabase = SQLiteDatabaseHelper.createSQLiteDbHelper(this);

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

        StatsExtractorTask statsExtractorTask = new StatsExtractorTask(StatisticsActivity.this);
        if (score != null || category != null) {
            statsExtractorTask.execute(username, score, category);
        } else {
            statsExtractorTask.execute(username);
        }
    }
}