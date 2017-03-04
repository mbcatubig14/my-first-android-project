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

    if ($quiz_username == "" && $quiz_userpassword == "" && $quiz_useremail == "" ) {
        echo 'Please fill all values';
    } else {
        require_once('Database_Connect.php');

        $stmt = mysqli_stmt_init($con);

        if (mysqli_stmt_prepare($stmt, "SELECT quiz_username, quiz_useremail FROM user WHERE quiz_username=? OR quiz_useremail=?")) {
          mysqli_stmt_bind_param($stmt, "ss", $quiz_username, $quiz_useremail);

          mysqli_stmt_execute($stmt);

          mysqli_stmt_bind_result($stmt, $user, $email);

          if (mysqli_stmt_fetch($stmt)) {
            echo 'Username or email already exist';
          }else{
                if (mysqli_stmt_prepare($stmt, "INSERT INTO user (quiz_username,quiz_userpassword,quiz_useremail) VALUES(?,?,?)")) {

                  $hashedPassword = password_hash($quiz_userpassword, PASSWORD_BCRYPT);

                  mysqli_stmt_bind_param($stmt, "sss", $quiz_username, $hashedPassword, $quiz_useremail);

                  mysqli_stmt_execute($stmt);

                    echo 'Successfully registered';
                } else {
                  
                    echo 'Oops! Please try again!';
                }
          }
        }
        mysqli_close($con);
    }
} else {
    echo 'Error try again';
}
