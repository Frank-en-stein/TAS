<?php
/**
 * Created by PhpStorm.
 * User: home
 * Date: 10/26/2016
 * Time: 6:25 AM
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
        if(!isset($_POST['name']) || !isset($_POST['email']) || !isset($_POST['phone']) || !isset($_POST['password']) ||
        !isset($_POST['contact1']) || !isset($_POST['contact2']) || !isset($_POST['contact3'])) {
            $e["success"] = 0;
            $e["message"] = "fail";
            echo json_encode($e);
            exit();
        }
    	$username = clean($_POST['name']);
        $phone = clean($_POST['phone']);
        $email = clean($_POST['email']);
    	$password = clean($_POST['password']);
        $c1 = clean($_POST['contact1']);
        $c2 = clean($_POST['contact2']);
        $c3 = clean($_POST['contact3']);

    	//Input Validations
    	if($username == '' || $phone == '' || $email == '' || $password == '' || $c1 =='' || $c2 == '' || $c3=='') {
            $errmsg_arr[] = 'Input missing';
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
    	$qry="INSERT INTO user(name, email, password, phone_number) VALUES('$username', '$email', '$password', '$phone')";
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