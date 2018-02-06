<?php
 
require_once __DIR__ . '/DB_functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
 
 
if (isset($_GET['phone']) && isset($_GET['otp'])) {
 
    // receiving the post params
    $phone = $_GET['phone'];
    $otp = $_GET['otp'];
	
 
    // check if user is already existed with the same email
	$user = $db->isUserExisted($phone);
    if ($user['status'] == 1) {
		$response["status"] = 1;
		$response["msg"] = "Phone number present, Enter info";
		$response["phone"] = $user["phone"];
		echo json_encode($response);
		
	}
		else if($user['status']==2){
		$response["status"] = 2;
		$response["msg"] = "All info present";
		$response["name"] = $user["name"];
		$response["phone"] = $user["phone"];
        echo json_encode($response);
    }
	else {
        // create a new user
		
		$status = 1;
        $user = $db->storeUserPhone($phone,$otp,$status);
        if ($user) {
            // user stored successfully
            $response["success"]=1;
            $response["error"] = FALSE;
            $response["phone"] = $user["phone"];
            $response["otp"] = $user["otp"];
            $response["status"] = 0;
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