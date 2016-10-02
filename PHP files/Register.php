<?php
/**
 * Created by PhpStorm.
 * User: Muhammad
 * Date: 19/04/2016
 * Time: 00:46
 */
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $quiz_username = $_POST['quiz_username'];
    $quiz_userpassword = $_POST['quiz_userpassword'];
    $quiz_useremail = $_POST['quiz_useremail'];

    if ($quiz_username == '' || $quiz_userpassword == '' || $quiz_useremail == '') {
        echo 'Please fill all values';
    } else {
        require_once('Database_Connect.php');
        $sql = "SELECT * FROM user WHERE quiz_username='$quiz_username' OR quiz_useremail='$quiz_useremail'";

        $check = mysqli_fetch_array(mysqli_query($con, $sql));

        if (isset($check)) {
            echo 'Username or email already exist';
        } else {
            $sql = "INSERT INTO user (quiz_username,quiz_userpassword,quiz_useremail) VALUES('$quiz_username','$quiz_userpassword','$quiz_useremail')";
            if (mysqli_query($con, $sql)) {
                echo 'Successfully registered';
            } else {
                echo 'Oops! Please try again!';
            }
        }
        mysqli_close($con);
    }
} else {
    echo 'Error try again';
}
