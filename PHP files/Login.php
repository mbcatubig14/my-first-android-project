<?php
/**
 * Created by PhpStorm.
 * User: Muhammad
 * Date: 19/04/2016
 * Time: 01:19
 */
	if($_SERVER['REQUEST_METHOD']=='POST'){
        $quiz_username = $_POST['quiz_username'];
        $quiz_userpassword = $_POST['quiz_userpassword'];

			require_once('Database_Connect.php');

		  $sql = "select * from user where quiz_username='$quiz_username' and quiz_userpassword='$quiz_userpassword'";
		  $check = mysqli_fetch_array(mysqli_query($con,$sql));

		  if(isset($check)){
		      echo "Successfully logged in";
		  }else{
		  echo "Invalid username or password";
		  }

    }else{
        echo "Error try again";
    }
