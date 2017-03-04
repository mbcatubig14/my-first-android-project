<?php

/**
 * Created by PhpStorm.
 * User: Muhammad
 * Date: 19/04/2016
 * Time: 01:11
 */
define('HOST', 'localhost');
define('USER', 'db_user_name');
define('PASS', 'db_password');
define('DB', 'mbcatubi_exquizme');

$con = mysqli_connect(HOST, USER, PASS, DB) or die('Unable to Connect');
