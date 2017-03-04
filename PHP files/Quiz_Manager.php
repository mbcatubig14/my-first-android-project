<?php

/**
 * Created by PhpStorm.
 * User: Muhammad
 * Date: 19/04/2016
 * Time: 00:46
 */
require_once('Database_Connect.php');

$questions_query = "SELECT q.quiz_question_id, question, quiz_choice_id, choice ,is_correct_choice, category FROM category_question cq, question q, question_choices qc WHERE q.quiz_question_id = qc.quiz_question_id AND q.cat_ques_id = cq.cat_ques_id";

$questions_query_result = mysqli_query($con, $questions_query) or die("error loading questions");

$questions_results = array();

encode_result($questions_query_result);

function encode_result($result) {

    $questions = array();

    while ($r = mysqli_fetch_array($result)) {
        $key = $r['quiz_question_id']; // key here that is unique to the question and this will group them in that segment of the array.
        if (!isset($questions[$key])) {
            //if we never had this question, create the top level in the results
            $questions[$key] = array(
                'question' => $r['question'],
                'category' => $r['category'],
                'choices' => array()
            );
        }
        //add in the choices for this question
        //on the next iteration, the key is the same so we only do this part
        //and append that rows choices to the previous data using $key to match the question
        $questions[$key]['choices'][] = array('quiz_choice_id' => $r['quiz_choice_id'], 'choice' => $r['choice'], 'is_correct_choice' => $r['is_correct_choice']);
    }
    echo json_encode(array("questions" => $questions));
}

mysqli_close($con);
