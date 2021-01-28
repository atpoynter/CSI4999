<!-- this is the process login and php derived from class -->
<?php
session_start();
// Handle login
if (isset($_POST['signupForm'])) {

// Connect to Database
$conn = mysqli_connect("localhost", "admin", "Csi-4999!", "ocrinventory") or die("Database error: " . mysqli_error($conn));

// Field Check
$error_msg = "";
$check_user = "";
$check_email = ""; // output message to user and also our check for errors
foreach ($_POST as $key=>$value) {
if (empty($_POST[$key]))
$error_msg = "All fields must be filled in!";
}
if ($error_msg == "") {

// Sanitization
$fn = mysqli_real_escape_string($conn, $_POST['firstName']);
$ln = mysqli_real_escape_string($conn, $_POST['lastName']);
$un = mysqli_real_escape_string($conn, $_POST['userName']);
$em = mysqli_real_escape_string($conn, $_POST['email']);
$pw1 = mysqli_real_escape_string($conn, $_POST['password']);
$pw2 = mysqli_real_escape_string($conn, $_POST['verify']);

// Regular Expressions
if (!preg_match("/^[a-zA-Z ]+$/", $fn))
$error_msg = "First name only can have letters/spaces";
if (!preg_match("/^[a-zA-Z ]+$/", $ln))
$error_msg = "Last name only can have letters/spaces";
if (!filter_var($em, FILTER_VALIDATE_EMAIL))
$error_msg = "Invalid email address";
if ((strlen($pw1) < 6) || (strlen($pw1) > 50))
$error_msg = "Invalid password length";
if ($pw1 != $pw2)
$error_msg = "Passwords don't match";
if ($error_msg == "") { // no error found then
    //Check User name
    $check_user = "SELECT username FROM tblUsers WHERE username = '$un'";
    $result = mysqli_query($conn, $check_user);
    if (mysqli_num_rows($result) > 0) 
    die("<h1>Username already taken!</h1>");

    //Check Email
    $check_email = "SELECT email FROM tblUsers WHERE email = '$em'";
    $result = mysqli_query($conn, $check_email);
    if (mysqli_num_rows($result) > 0) 
    die("<h1>E-mail address is already registered!</h1>");

// Insert into database
$query = "INSERT INTO tblUsers (username, first_name, last_name, email,
password) VALUES('" . $un . "', '" . $fn . "', '" . $ln . "', '" . $em . "', '" .
md5($pw1) . "')";
echo $query; // debugging - check query

// Perform insertion
if (mysqli_query($conn, $query))
echo "<h1>Successfully added $un to database!</h1>";
else
echo "<font color='red'>Error adding user.</font>";
} else { // error in validation
echo "<font color='red'>$error_msg</font>";
}
} else { // error in missing field data
echo "<font color='red'>$error_msg</font>";
}
/////////////////////////////HANDLE USER LOGIN
  } else { 

    echo "User login";
 
    // Get our database connection
    include('db-conn.php');

    // Sanitize inputs
    $un  = mysqli_real_escape_string($conn, $_POST['username']);
    $pw  = mysqli_real_escape_string($conn, $_POST['password']);

    // Select a user -- NOTE THAT WITH THIS SCHEME
    // YOU NEED TO ENFORCE UNIQUE USERNAMES (as we just pick the 'first')
    $q   = "SELECT * FROM tblUsers WHERE username = '$un'";
    echo "<br>$q";

    $res = mysqli_query($conn, $q);
    if ($res->num_rows > 0) { // Username found
      $user = mysqli_fetch_array($res);
      echo "<br>User found! - [" . $user['username'] . "]";

      // Check password
      if (md5($_POST['password']) == $user['password']) { // login successful
        echo "<h2>Welcome $un!</h2>";
       
        // Set session vars
        $_SESSION['logged_in']  = TRUE;
        $_SESSION['username']   = $un;
        $_SESSION['first_name'] = $user['first_name'];
        $_SESSION['last_name']  = $user['last_name'];
  

      } else { // password doesn't match
        echo "<h2>Error logging in!</h2>";
        session_destroy();
      }

      echo "<a href=\"javascript:history.go(-1)\">Continue</a>";

    } else { // username not found
      echo "User not found!";
    }
  }
?>
<!-- Quick redirect to previous page -->
<script type="text/javascript">
    window.location = "javascript:history.go(-1)";
</script>