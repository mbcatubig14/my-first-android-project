<?php

/**
 * Created by PhpStorm.
 * User: Muhammad
 * Date: 19/04/2016
 * Time: 01:19
 */
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $quiz_username = $_POST['quiz_username'];
    $quiz_userpassword = $_POST['quiz_userpassword'];

    if ($quiz_username == '' && $quiz_userpassword == '') {
        echo 'Please fill all values';
    } else {
      require_once('Database_Connect.php');

      $stmt = mysqli_stmt_init($con);

      if (mysqli_stmt_prepare($stmt, "SELECT quiz_username, quiz_userpassword FROM user WHERE quiz_username=?")) {

        mysqli_stmt_bind_param($stmt, "s", $quiz_username);

        mysqli_stmt_execute($stmt);

        mysqli_stmt_bind_result($stmt, $user, $password);

          if (mysqli_stmt_fetch($stmt) && password_verify($quiz_userpassword, $password)) {
              echo "Successfully logged in";
          }else{
            echo "Invalid username or password\n";
          }
        }
      mysqli_close($con);
    }
} else {
    echo "Error try again";
}
