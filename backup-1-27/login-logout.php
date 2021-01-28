<!-- code that uses includes to display proper login elements via php includes -->
<?php
  if ($_SESSION['logged_in'] == TRUE) {
    include("logout-element.html");
  } else {
    include("login-element.html");
  }

?>
