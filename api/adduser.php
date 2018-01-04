<?php
 
require_once __DIR__ . '/DB_functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['name']) && isset($_POST['ep']) && isset($_POST['pwd'])) {
 
    // receiving the post params
    $name = $_POST['name'];
    $email = $_POST['emphone'];
    $password = $_POST['password'];
	
 
    // check if user is already existed with the same email
    if ($db->isUserExisted($emphone)) {
        // user already existed
        $response["success"]=0;
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed with " . $email;
        echo json_encode($response);
    } else {
        // create a new user
        $user = $db->storeUser($name, $emphone, $password);
        if ($user) {
            // user stored successfully
            $response["success"]=1;
            $response["error"] = FALSE;
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["created_at"] = $user["created_at"];
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
    }
} else {
    $response["success"]=0;
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (name, email or password) is missing!";
    echo json_encode($response);
}
?>