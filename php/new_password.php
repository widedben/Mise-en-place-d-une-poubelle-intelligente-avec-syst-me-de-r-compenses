<?php

// Inclure le fichier de connexion à la base de données
include ("db_connect.php");

$phpMailerDir = 'C:\xampp\htdocs\PHPMailer\vendor\phpmailer\phpmailer';
 require 'C:\xampp\htdocs\PHPMailer\vendor\autoload.php';

 // Définir l'en-tête pour indiquer que la réponse est au format JSON et encodée en UTF-8
header('Content-type: application/json; charset=utf-8');

// Initialiser un tableau pour la réponse JSON
$response=array();
// Vérifier si les paramètres 'opt', 'new_password', 'mail' sont présents dans la requête POST
if( isset($_POST["mail"]) && isset($_POST["opt"]) && isset($_POST["new_password"])  ){
    // Récupérer les valeurs des paramètres sans les modifier
    $mail = $_POST['mail'];
    $opt = $_POST['opt'];
    $new_password = $_POST['new_password'];
}
// Initialiser le message d'erreur MySQL à vide
$error_message = "";

// Vérifier si les valeurs des paramètres ne sont pas vides
if(!empty($opt) && !empty($new_password) && !empty($mail) ){
    // Vérifier si le code entré correspond au code envoyé par mail
    $check_opt = mysqli_query($cnx, "SELECT * FROM sign_up WHERE mail='" . $mail . "' and reset_password_opt='" . $opt . "'");
     // Il y a au moins une ligne dans le résultat de la requête
    if(mysqli_num_rows($check_opt) > 0){
        // Exécuter la requête de modification dans la table 'sign_up' avec les valeurs fournies
        $req=mysqli_query($cnx,"UPDATE sign_up SET password='" . $new_password . "' , reset_password_opt ='' , reset_password_created_at= '' WHERE mail='" . $mail . "' and reset_password_opt=' ".$opt."'");

        // Vérifier si la requête de modification a réussi
        if($req){
            // Si oui, définir le succès sur 1 et afficher un message de réussite
            $response["success"]=1;
            $response["message"]="modified !";
        }
        else{
            // Si la requête a échoué, définir le succès sur 0 et stocker le message d'erreur MySQL
            $response["success"]=0;
            $error_message = mysqli_error($cnx); // Récupérer l'erreur MySQL
        }
    } else {
        // Si le code entré ne correspond pas au code envoyé par mail
        $response["success"]=0;
        $response["message"]="Invalid code!";
    }
}
else{
    // Si les valeurs des paramètres ne sont pas définies, définir le succès sur 0 et afficher un message d'erreur
    $response["success"]=0;
    $response["message"]="required field is missing!";
}

// Ajouter le message d'erreur MySQL à la réponse si une erreur s'est produite
if(!empty($error_message)){
    $response["error_message"] = $error_message;
}

// Retourner la réponse JSON
echo json_encode($response);

?>
