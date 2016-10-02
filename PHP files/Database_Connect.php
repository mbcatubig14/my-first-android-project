<?php
/**
 * Created by PhpStorm.
 * User: Muhammad
 * Date: 19/04/2016
 * Time: 01:11
 */
	define('HOST','mysql11.000webhost.com');
	define('USER','a9333571_admin');
	define('PASS','sharmc27');
	define('DB','a9333571_quizapp');

	$con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');
