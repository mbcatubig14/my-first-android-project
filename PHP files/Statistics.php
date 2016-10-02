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

    $insert_stats_query = "INSERT INTO user_score (user_id, best_score, best_category) SELECT quiz_user_id,'$best_score','$best_category' FROM user WHERE quiz_username='$quiz_username'";

    $best_score_query = "SELECT best_score FROM user_score us, user u  WHERE u.quiz_user_id=us.user_id AND u.quiz_username='$quiz_username' ORDER BY us.best_score DESC LIMIT 5";
    $best_category_query = "SELECT best_category FROM user_score us, user u WHERE u.quiz_user_id=us.user_id AND u.quiz_username='$quiz_username' GROUP BY best_category ORDER BY count(*) desc limit 5";
    require_once('Database_Connect.php');

    $response = array();

    if (!empty($best_score) || $best_score != 0 || !empty($best_category)) {
        //Insert best score and best category to the database according to the username
        $insert_query_result = mysqli_query($con, $insert_stats_query);
    }

    //Get results
    $score_query_result = mysqli_query($con, $best_score_query) or die("error loading scores");
    $category_query_result = mysqli_query($con, $best_category_query) or die("error loading categories");

//Get best_score data from database

    while ($r = mysqli_fetch_assoc($score_query_result)) {
        $response['score'][] = $r['best_score'];
    }

//Get best_category data from database
    while ($r = mysqli_fetch_assoc($category_query_result)) {
        $response['category'][] = $r['best_category'];
    }

    echo json_encode(array(
        'stats' => $response
    ));

    mysqli_close($con);
}
