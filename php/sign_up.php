<?php
// Inclure le fichier de connexion à la base de données
include ("db_connect.php");

$phpMailerDir = 'C:\\xampp\\htdocs\\PHPMailer\\vendor\\phpmailer\\phpmailer';
require 'C:\\xampp\\htdocs\\PHPMailer\\vendor\\autoload.php';

require_once $phpMailerDir . '/src/PHPMailer.php';
require_once $phpMailerDir . '/src/SMTP.php';
require_once $phpMailerDir . '/src/Exception.php';

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;

// Définir l'en-tête pour indiquer que la réponse est au format JSON et encodée en UTF-8
header('Content-type: application/json; charset=utf-8');

// Initialiser un tableau pour la réponse JSON
$response = array();

// Vérifier si les paramètres 'nom', 'id', 'mail', 'password' et 'pass2' sont présents dans la requête GET
if (isset($_GET["nom"]) && isset($_GET["id"]) && isset($_GET["mail"]) && isset($_GET["password"]) && isset($_GET["pass2"])) {
    // Récupérer les valeurs des paramètres 'nom', 'id', 'mail', 'password' et 'pass2' sans les modifier
    $name = $_GET["nom"];
    $id = $_GET["id"];
    $email = $_GET["mail"];
    $password = $_GET["password"];
    $pass2 = $_GET["pass2"];
   // Vérifier si le code parrain est présent dans la requête GET
// Vérifier si le code parrain est présent dans la requête GET
if (isset($_GET["code"])) {
    // Convertir la chaîne en entier
    $parrain = intval($_GET["code"]);

    // Vérifier si le code parrain existe déjà dans la base de données
    $check_parrain = mysqli_query($cnx, "SELECT * FROM sign_up WHERE ton_code = '$parrain'");
    if (mysqli_num_rows($check_parrain) > 0) {
        // Le code parrain existe déjà, vous pouvez l'insérer dans la base de données
    } else {
        // Le code parrain n'existe pas, vous pouvez définir une valeur par défaut ou afficher un message d'erreur
        $response["success"] = 0;
        $response["message"] = "Le code parrain n'existe pas.";
        echo json_encode($response);
        exit(); // Arrêter l'exécution du script
    }
} else {
    // Si le code parrain n'est pas présent, définir une valeur par défaut ou afficher un message d'erreur
    $parrain = 0 ; // Ou toute autre valeur par défaut que vous souhaitez utiliser
}
        // Vérifier si les valeurs des paramètres 'nom', 'id', 'mail', 'password' et 'pass2' ne sont pas vides
    if (!empty($name) && !empty($id) && !empty($email) && !empty($password) && !empty($pass2)) {
        // Exécuter une requête d'insertion dans la table 'sign_up' avec les valeurs fournies

        $otp = random_int(100000, 999999);

        $req = "insert into sign_up(name, id, mail, password, pass2, ton_code,parrain) values('$name', '$id', '$email', '$password', '$pass2','$otp','$parrain')";

        if (mysqli_query($cnx, $req)) {
            // Envoi d'un email avec le code OTP
            $mail = new PHPMailer(true);

            try {
                $mail->isSMTP();
    
                // Set the SMTP host
                $mail->Host = 'smtp.gmail.com';
                
                // Set the SMTP port
                $mail->Port = 587;
                
                // Enable SMTP authentication
                $mail->SMTPAuth = true;
                
                // Set the SMTP username and password
                $mail->Username   = 'beenyaagoubwided123@gmail.com';                     // Nom d'utilisateur SMTP
                $mail->Password   = 'acixxskdltkxpftd';                               // Mot de passe SMTP
                
                // Set the email address that the message will be sent from
                $mail->setFrom('beenyaagoubwided123@gmail.com', 'Wided Ben');
                
                // Set the email address that the message will be sent to
                $mail->addAddress($email);
                
                // Content
                $mail->isHTML(true);
                $mail->Subject = 'Votre Code de Parrainage est Arrivé !';
                $mail->Body = 'Bonjour ,

                Nous sommes ravis de vous annoncer que vous avez reçu un code de parrainage exclusif ! Vous pouvez utiliser ce code pour inviter vos amis et votre famille à rejoindre notre application. En tant que parrain, vous serez récompensé pour chaque nouvel utilisateur qui s inscrit en utilisant votre code.
                
                Voici votre code de parrainage :  '. $otp .'
               
                
                Comment utiliser votre code de parrainage :
                1. Partagez ce code avec vos amis et votre famille.
                2. Demandez-leur de s inscrire sur notre application en utilisant ce code.
                3. Vous recevrez des points de récompense chaque fois que vos amis gagneront des points.
 
                Nous vous remercions pour votre soutien et votre confiance. Si vous avez des questions ou des préoccupations, n hésitez pas à nous contacter.
                
                Cordialement.' ;

                $mail->send();
                $response["success"] = 1;
                $response["message"] = "inserted and email sent!";
            } catch (Exception $e) {
                $response["success"] = 0;
                $response["message"] = "inserted but email not sent: {$mail->ErrorInfo}";
            }
        } else {
            // Si la requête a échoué, définir le succès sur 0 et afficher un message d'erreur
            $response["success"] = 0;
            $response["message"] = "request error !";
        }

        // Ajouter une nouvelle ligne dans la table 'categorie' avec l'id correspondant
        $req_categorie = mysqli_query($cnx, "insert into categorie(id, carton, batterie, bouteille) values('$id', 0, 0, 0)");
        // Ajouter une nouvelle ligne dans la table 'score' avec l'id correspondant et des valeurs par défaut de 0 pour les autres colonnes
        $req_score = mysqli_query($cnx, "insert into score(id, score,reduction,ton_code) values('$id', 0,0,'$otp')");

        if ($req_categorie && $req_score) {
            $response["success"]=1;
            $response["message"]="inserted !";
        } else {
            // Si la requête a échoué, définir le succès sur 0 et afficher un message d'erreur
            $response["success"]=0;
            $response["message"]="request error !";
        }
    } else {
        // Si les valeurs des paramètres 'nom', 'id', 'mail', 'password' et 'pass2' sont vides, définir le succès sur 0 et afficher un message d'erreur
        $response["success"] = 0;
        $response["message"] = "required field is missing!";
    }
} else {
    // Si les paramètres 'nom', 'id', 'mail', 'password' et 'pass2' sont manquants, définir le succès sur 0 et afficher un message d'erreur
    $response["success"] = 0;
    $response["message"] = "required field is missing!";
}

// Retourner la réponse JSON
echo json_encode($response);
?>
