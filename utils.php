<!DOCTYPE html>
<html lang="">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Steven Naoum | Home</title>
    <link rel="stylesheet" href="./css/style.css">
</head>

<body>
    <header>
        <div class="container">
            <div id="branding">
                <h1><span class="highlight">Steven Naoum</span></h1>
            </div>
            <nav>
                <ul>
                    <li><a href="index.html"> Home</a></li>
                    <li><a href="links.html">Links</a></li>
                    <li><a href="project.html">Projects</a></li>
                    <li><a href="courses.html">Courses</a></li>
                    <li><a href="contact.html">Contact</a></li>
                    <li><a href="quotes.html">Quotes</a></li>
                    <li class="current"><a href="login.html">Secure</a></li>
                </ul>
            </nav>
        </div>
    </header>
    <?php

// Function createa DB connection

function connectToDB()
{   
    global $conn;
    
    $servername = "35.223.15.126";
    $username = "admin";         // Local Bitname Server
    //$username = "root";      // For OCR Server
    $password = "Csi-4999!";
    $dbname = "ocrinventory";     // Local Bitname Server
    //$dbname = "sanaoum";        // For SECS server
    
    //echo "DEBUG: Connecting to DB <br>";
	$conn = new mysqli($servername, $username, $password, $dbname);
    
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }
  
    //echo "DEBUG: Connected successfully (". $conn->host_info. ") <br>";
    
}

// Function for inserting records to a database. Here 
function insertRecord()
{
    global $conn;
    
    $id = $_POST['id'];
    $product_name = $_POST['product_name'];
    $product_desc = $_POST['product_desc'];
    $product_upc = $_POST['product_upc'];
    $date = $_POST['date'];
    $user_id = $_POST['user_id'];
    
        
    if (!empty($name) && !empty($id)){
        $sql = "insert into tblProducts(id, product_name, product_desc, product_upc, date, user_id)" .
                    "values ('$id', '$product_name', '$product_desc', '$product_upc', '$date', '$user_id')";
        if ($conn->query ($sql) == TRUE) {
            //echo "DEBUG: Record added <br>";
        }
        else
        {
            echo "Could not add record: " . $conn->connect_error . "<br>";
        }
    } 
    else
    {
        echo "Must provide a id and Name to enter a record <br>";
        $action = 'showInsertForm';
    }
}

function deleteRecord()
{
    global $conn;
    $id = $_POST['id'];
    if (!empty($id)){
        $sql = "delete from tblProducts where id = '$id'";
        //echo $sql . "<br>";
        if ($conn->query ($sql) == TRUE) {
            //echo "DEBUG: Record deleted <br>";
        }
        else
        {
            echo "Could not add record: " . $conn->connect_error . "<br>";
        }
    }
    else
    {
        echo "Must provide a id to delete a record a record <br>";
    }
}


function showStudentRecords()
{
    global $conn;
    global $thisPHP;
    
    echo "<form id='insertForm' action='{$thisPHP}' method='post'>";
    echo "<fieldset><legend>Click to Insert New contact to Database</legend>";
    echo "<input type='submit' name='showInsertForm' value='Add New Contact'>";
    echo "</form></fieldset>";
    
    $sql = "SELECT * FROM tblProducts";
    $result = $conn->query($sql);
    
    if ($result->num_rows > 0) 
    {         
        
        echo "<h4>Contact Database</h4>";
        echo "<table>";
        echo "<thead><tr><td>id</td> <td>Name </td> <td>Relationship </td> <td>E-Mail</td><td>Phone</td> ";
        echo "<td>Address</td> <td>Notes </td> <td>Interaction </td> <td> Delete? </td></tr></thead>";   
        while($row = $result->fetch_assoc()) 
        {
            echo "<tbody><tr>";
            $id = $row["id"];
            echo  "<td>" . $id .
                  "  </td> <td> " . $row["product_name"] .
                  "  </td> <td> " . $row["product_desc"] .
                  "  </td> <td> " . $row["product_upc"] .
                  " </td> <td> " . $row["date"] . 
    		      " </td> <td> " . $row["user_id"] . 
                  "  </td> <td> " . $row["delete?"] .
                  "</td>  <td> "; 
                    
                  
                
                  
               
            echo "<form action='{$thisPHP}' method='post' style='display:inline' >";
            echo "<input type='hidden' name='id' value='{$id}'>";
            echo "<input type='submit' name='btnDelete' value='Delete'></form>";
            echo "</td></tr></tbody>";
        }
        
        echo "</table>";
    } 
    else 
    {
        echo "0 results";
    }
}


function displayInsertForm()
{
    global $thisPHP;
    
    // A heredoc for specifying really long strings
    $str = <<<EOD
    <form action='{$thisPHP}' method='post'>
    <fieldset>
        <legend>tblProducts Data Entry</legend> id:
        <input type="text" name="id" size="10">
        <br> Name:
        <input type="text" name="product_name" size="30">
        <br> Relationship:
        <input type="text" name="product_desc" size="30">
        <br> Email:
        <input type="email" name="product_upc" size="20">
        <br> Phone:
        <input type="tel" name="date" size="15">
        <br> Address:
        <input type="text" name="user_id" size="15">
        <br>
    
        
        
        <input type="submit" name="btnInsert" value="Insert"><br>
    </fieldset>
    </form>
EOD;

    echo $str;
}
?>
