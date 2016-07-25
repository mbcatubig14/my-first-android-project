package com.example.muhammad.exquizme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the local SQLite database for storing the questions generated from the web server.
 * Created by Muhammad on 19/04/2016.
 */
class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    private static final String[] TABLES = new String[]{"question", "scores", "stats"};

    public SQLiteDatabaseHelper(Context context) {
        super(context, "QUESTION_DATABASE", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create Questions Table
        String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + TABLES[0] + " (\n" +
                "  quiz_question_id INT  PRIMARY KEY, question TEXT, choice_a TEXT, choice_b TEXT, choice_c TEXT , choice_d TEXT,\n" +
                "  answer TEXT , category TEXT)";
        db.execSQL(CREATE_QUESTIONS_TABLE);

        String CREATE_SCORE_TABLE = "CREATE TABLE " + TABLES[1] + " (score_id INTEGER PRIMARY KEY, best_score INT )";
        db.execSQL(CREATE_SCORE_TABLE);

        //Create Stats Table
        String CREATE_STATS_TABLE = "CREATE TABLE " + TABLES[2] + "(stats_id INTEGER PRIMARY KEY, best_score INT, best_category TEXT)";
        db.execSQL(CREATE_STATS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLES[0]);
        db.execSQL("DROP TABLE IF EXISTS" + TABLES[1]);
        db.execSQL("DROP TABLE IF EXISTS" + TABLES[2]);
    }

    //Managing question for the quiz
    public void addQuestion(QuizQuestion questions) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Insert in question table
        ContentValues values = new ContentValues();
        values.put("question", questions.getQuestion());
        values.put("choice_a", questions.getChoices()[0]);
        values.put("choice_b", questions.getChoices()[1]);
        values.put("choice_c", questions.getChoices()[2]);
        values.put("choice_d", questions.getChoices()[3]);
        values.put("answer", questions.getAnswer());
        values.put("category", questions.getCategory());

        // Inserting Row
        db.insert(TABLES[0], null, values);
        db.close(); // Closing database connection
    }

    public void addStats(String bestScore, String bestCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Insert in stats table
        ContentValues values = new ContentValues();
        values.put("best_score", bestScore);
        values.put("best_category", bestCategory);
        // Inserting Row
        db.insert(TABLES[2], null, values);
        db.close(); // Closing database connection
    }

    public void addScore(String bestScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Insert in stats table
        ContentValues values = new ContentValues();
        values.put("best_score", bestScore);
        // Inserting Row
        db.insert(TABLES[1], null, values);
        db.close(); // Closing database connection
    }

    //Managing best scores and best category for stats

    public List<QuizQuestion> getQuestions() {
        List<QuizQuestion> quizQuestionArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLES[0], null, null, null, null, null, null, null);
        if (!cursor.isLast()) {
            while (cursor.moveToNext()) {
                String[] choices = {cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)};
                quizQuestionArrayList.add(new QuizQuestion(cursor.getString(1), choices, cursor.getString(6), cursor.getString(7)));
            }
        }
        cursor.close();
        db.close();
        return quizQuestionArrayList;
    }

    public List<ArrayList<String>> getStats() {
        List<ArrayList<String>> statsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor scoreCursor = db.query(TABLES[2], new String[]{"best_score"}, null, null, null, null, "best_score DESC");
        Cursor categoryCursor = db.query(TABLES[2], new String[]{"best_category"}, null, null, "best_category", null, "count(*) DESC");
        ArrayList<String> scores = new ArrayList<>(), categories = new ArrayList<>();
        if (!scoreCursor.isLast()) {
            while (scoreCursor.moveToNext()) {
                scores.add(scoreCursor.getString(0));
            }
        }
        if (!categoryCursor.isLast()) {
            while (categoryCursor.moveToNext()) {
                categories.add(categoryCursor.getString(0));
            }
        }
        statsList.add(scores);
        statsList.add(categories);
        scoreCursor.close();
        categoryCursor.close();
        db.close();
        return statsList;
    }

    public List<String> getScores() {
        List<String> scoresMap = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryStats = "SELECT * FROM " + TABLES[1];
        Cursor cursor = db.rawQuery(queryStats, null);
        if (!cursor.isLast()) {
            while (cursor.moveToNext()) {
                scoresMap.add(cursor.getString(1));
            }
        }
        cursor.close();
        db.close();
        return scoresMap;
    }

}
