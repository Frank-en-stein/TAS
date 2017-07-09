<?php
/**
 * Created by PhpStorm.
 * User: home
 * Date: 10/26/2016
 * Time: 2:52 AM
 */

    $e = array();
	
    	//Start session
    	session_start();
     
    	//Include database connection details
    	require_once('dbconnect.php');
     
    	//Array to store validation errors
    	$errmsg_arr = array();
     
    	//Validation error flag
    	$errflag = false;
     
    	//Function to sanitize values received from the form. Prevents SQL injection
    	function clean($str) {
            $str = @trim($str);
            if(get_magic_quotes_gpc()) {
                $str = stripslashes($str);
            }
            return mysql_real_escape_string($str);
        }
     
    	//Sanitize the POST values
        if(!isset($_POST['phone']) || !isset($_POST['password'])) {
            $e["success"] = 0;
            $e["message"] = "fail";
            echo json_encode($e);
            exit();
        }
    	$username = clean($_POST['phone']);
    	$password = clean($_POST['password']);
     
    	//Input Validations
    	if($username == '') {
            $errmsg_arr[] = 'Username missing';
            $errflag = true;
        }
    	if($password == '') {
            $errmsg_arr[] = 'Password missing';
            $errflag = true;
        }
     
    	//If there are input validations, redirect back to the login form
    	if($errflag) {
            $_SESSION['ERRMSG_ARR'] = $errmsg_arr;
            session_write_close();
            $e["success"] = 0;
            $e["message"] = "fail";
            echo json_encode($e);
            exit();
        }
     
    	//Create query
    	$qry="SELECT * FROM user WHERE phone_number='$username' AND password='$password'";
        $result = $conn->query($qry);
     
    	//Check whether the query was successful or not
    	if($result) {
            if ($result->num_rows > 0) {
                //Login Successful
                session_regenerate_id();
                $member = $result->fetch_assoc();
                session_write_close();
                $e["success"] = 1;
                $e["message"] = $member["phone_number"];
                echo json_encode($e);
                exit();
            }else {
                //Login failed
                $errmsg_arr[] = 'user name and password not found';
                $errflag = true;
                if($errflag) {
                    $_SESSION['ERRMSG_ARR'] = $errmsg_arr;
                    session_write_close();
                    $e["success"] = 0;
                    $e["message"] = "fail";
                    echo json_encode($e);
                    exit();
                }
            }
        }else {
            $e["success"] = 0;
            $e["message"] = "failed DB search";
            echo json_encode($e);
            exit();
        }
    ?>