<?php
session_start();
include ('db-conn.php');
?>
    <!DOCTYPE html>
    <html lang="en">
    <head>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    </head>
    <body>
        <header>
        </header>
        <div class="container">
            <?php 
    if ($_SESSION['logged_in'] != ""){ 
        echo 'You are logged in.';}
        ?>
                <?php include ("login-logout.php"); ?>
        </div>
        <div id="formContainer">
        <?php 
    if ($_SESSION['logged_in'] == ""){
        include("sign-up.html");
    }
        ?>
                </div>
        </div>
    </body>
    </html>