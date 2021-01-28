<!-- essential script from class to allow session functionality  -->
<?php
  // $conn will be available after this script is included
  //                     server       username      password     database
  $conn = mysqli_connect("localhost", "admin", "Csi-4999!", "ocrinventory") or die("Database error: " . mysqli_error($conn));
?>
