<?php
$servername = "localhost";
$username = "app";
$password = "script12345";
$db = "tas_db";

// Create connection
$conn = new mysqli($servername, $username, $password, $db);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
?>