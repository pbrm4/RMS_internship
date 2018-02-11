<?php
 
/*
 * Following code will get single product details
 * A product is identified by product id (pid)
 */
 
// array for JSON response
$response = array();
$ni=4;
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
$con = $db->connect();
 
// check for post data
if (isset($_GET["noitems"])) {
    $ni = (int)$_GET['noitems'];
 
$productname = array();
 for($i=1;$i<=$ni;$i++){
	 $productname[$i] = $_GET['product_'.(string)$i];
 }
 $sql = "SELECT * FROM products WHERE ";
 $i = (int)1;
 $sql = $sql."productname ='".$productname[$i]."'";

 for($i=2;$i<=$ni;$i++){
	 $sql =$sql." || productname = '".$productname[$i]."';";
 }
 
    // get a product from products table
    $result = mysqli_query($con,$sql);
    
            $product = array();
            $pname = array(); 
            
            if (!empty($result)) {
        // check for empty resultt
        if (mysqli_num_rows($result) > 0) {
 
            while($res = mysqli_fetch_assoc($result)){
            $product[$res['productname'].$res['retailname']] = $res;
            
            }
            
            
            $response["success"] = 1;
 
            // user node
            $response["product"] = array();
 
            array_push($response["product"], $product);
 
            // echoing JSON response
            echo json_encode($response);
    
            
            
        } else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No product found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no product found
        $response["success"] = 0;
        $response["message"] = "No product found";
 
        // echo no users JSON
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>