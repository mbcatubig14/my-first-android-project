<?php

/**
 * Created by PhpStorm.
 * User: Muhammad
 * Date: 19/04/2016
 * Time: 00:46
 */

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $quiz_username = $_POST['quiz_username'];
    $best_score = $_POST['best_score'];
    $best_category = $_POST['best_category'];

    require_once('Database_Connect.php');

    $best_score_query = "SELECT us.best_score FROM user_score us INNER JOIN user u ON u.quiz_user_id=us.user_id WHERE u.quiz_username= ? ORDER BY us.best_score DESC LIMIT 5";

    $best_category_query = "SELECT us.best_category FROM user_score us INNER JOIN user u ON u.quiz_user_id=us.user_id WHERE u.quiz_username=? GROUP BY best_category ORDER BY count(*) desc limit 5";

    $insert_stats_query = "INSERT INTO user_score (user_id, best_score, best_category) SELECT quiz_user_id, ? , ? FROM user WHERE quiz_username= ?";

    $stmt = mysqli_stmt_init($con);

    if (!empty($best_score) || $best_score != 0 || !empty($best_category)) {
        //Insert best score and best category to the database according to the username
        $insert_query_result = mysqli_query($con, $insert_stats_query);
        if (mysqli_stmt_prepare($stmt, $insert_stats_query)) {
          mysqli_stmt_bind_param($stmt, "sis", $best_score, $best_category, $quiz_username);

          mysqli_stmt_execute($stmt);

          echo "Score saved.";
        }
    }


    //Get best_score data from database
    if (mysqli_stmt_prepare($stmt, $best_score_query)) {
      mysqli_stmt_bind_param($stmt, "s", $quiz_username);

      mysqli_stmt_execute($stmt);

      mysqli_stmt_bind_result($stmt, $scores);

        while (mysqli_stmt_fetch($stmt)) {
          $response['scores'][] = $scores;
        }
    }

    //Get best_category data from database
    if (mysqli_stmt_prepare($stmt, $best_category_query)) {

      mysqli_stmt_bind_param($stmt, "s", $quiz_username);

      mysqli_stmt_execute($stmt);

      mysqli_stmt_bind_result($stmt, $categories);

        while (mysqli_stmt_fetch($stmt)) {
           $response['category'][] = $categories;
        }
    }

    //Print results in JSON format
    echo json_encode($response);

    mysqli_close($con);
}else{
  echo "Error try again";
}
