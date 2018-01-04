<?php

class DB_Functions {
 
    private $conn;
 
    // constructor
    function __construct() {
        require_once __DIR__ .'/db_connect.php';
        // connecting to database
        $db = new DB_CONNECT();
        $this->conn = $db->connect();
    }
 
    // destructor
    function __destruct() {
         
    }
 
    /**
     * Storing new user
     * returns user details
     */
    public function storeUserPhone($phone,$otp,$status) {
 
        $stmt = $this->conn->prepare("INSERT INTO users(phone,otp,status,created_at) VALUES(?, ?, ?, NOW())");
        $stmt->bind_param("sss",$phone,$otp,$status);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE phone = ?");
            $stmt->bind_param("s", $phone);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }
	
	/* Store name and password that the user gives, hash the password, store in user where phone is  */ 
	
	public function storeUser($name,$phone,$password) {
		
		$hash =  hashSSHA($password);
		$salt = $hash["salt"];
		$encrypted_password = $hash["encrypted"];
		
        $stmt = $this->conn->prepare("INSERT INTO users(name,salt,encrypted_password,status,updated_at) VALUES(?, ?, ?, ?, NOW()) where phone = ?");
        $stmt->bind_param("sssss",$name,$salt,$encrypted_password,$status,$phone);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE phone = ?");
            $stmt->bind_param("s", $phone);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }
 
    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($userid, $password,$league,$team) {
 
        $stmt = $this->conn->prepare("SELECT * FROM users WHERE userid = ? and league = ? and team = ?");
 
        $stmt->bind_param("sss", $userid,$league,$team);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            // verifying user password
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }
 
    /**
     * Check user is existed or not
     */
    public function isUserExisted($phone) {
        $stmt = $this->conn->prepare("SELECT * from users WHERE phone = ?");
 
        $stmt->bind_param("s", $phone);
 
        $stmt->execute();
 
		
		
        $result =$stmt->get_result();
        if ($result->num_rows > 0) {
            // user existed 
            $user = $result->fetch_assoc();
            $stmt->close();
            return $user;
        } else {
            // user not existed
            $stmt->close();
            return -1;
        }
    }
 
    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
 
}
 
?>