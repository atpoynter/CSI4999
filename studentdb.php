<!doctype html>
<html>

<head>
    <link rel="stylesheet" href="styles.css">
</head>

<body>
    <?php
    session_start();
    include "utils.php";

    // echo "DEBUG: Session Login Value = " . $_SESSION['login'] . "<br>";
    
    if (!isset($_SESSION['login']) || $_SESSION['login'] == '')
    {
        echo $_SESSION['login'];
        header ("Location: ./login.html");
    } 
    
    
    echo "<h2>Student DB Manager</h2>";

    //************//
    
    connectToDB();
    
    ///**********//
    
    $thisPHP = $_SERVER['PHP_SELF'];
    $databaseAction = '';            // No default modification action
    $displayAction = 'showRecords';      // Default display 

    if (isset($_POST['btnInsert']))
        $databaseAction = 'doInsert';
    if (isset($_POST["btnDelete"]))
        $databaseAction = 'doDelete';
    
    if (isset($_POST['showInsertForm']))
        $displayAction = 'showInsertForm';
    else
        $displayAction ='showRecords';

    
    ///*****************//
    // Database Actions
    ///*****************//
    // These two are pre-display database actions.
    // Insertion or Deletion will be done prior to showStudentRecords()
    // And thus, showStudentRecords() will show updated database
    
    //Insert Action
             
    if ($databaseAction == 'doInsert')
    {
       insertRecord();
    }
    
    ///**********//
    
    //Delete Action
                  
    else if ($databaseAction == 'doDelete')
    {
        deleteRecord();
    }

    ///*****************//
    // Display Actions
    ///*****************//
    
    // Actions -- Either we will show the form for inserting student Data; 
    // OR the Table with All Students. 
    // In our implentation the two displays above are Mutually Exclusive.
    
    // Display Form for entering a student's record
    
    if ($displayAction == 'showInsertForm')
    {
        //include "frmInsStudent.php";
        displayInsertForm();
    }

    // Default action: show always be true since inialized at script start
    // Display table showing all student records
    
    else if ($displayAction == 'showRecords')
    {
        showStudentRecords();
    }
    $conn->close();
?>
</body>

</html>
