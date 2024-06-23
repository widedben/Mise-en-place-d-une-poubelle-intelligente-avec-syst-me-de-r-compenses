<?php
// Inclure le fichier de connexion à la base de données
include ("db_connect.php");

$phpMailerDir = 'C:\xampp\htdocs\PHPMailer\vendor\phpmailer\phpmailer';
 require 'C:\xampp\htdocs\PHPMailer\vendor\autoload.php';

 require_once $phpMailerDir . '/src/PHPMailer.php';
 require_once $phpMailerDir . '/src/SMTP.php';
 require_once $phpMailerDir . '/src/Exception.php';
 
 use PHPMailer\PHPMailer\PHPMailer;
 use PHPMailer\PHPMailer\SMTP;
 use PHPMailer\PHPMailer\Exception;
if (!empty($_POST['mail'])) {
    $response = array(); // Initialiser un tableau pour stocker la réponse
    $response['success'] = false; // Par défaut, définir le succès sur faux

    $email = $_POST['mail'];
    if ($cnx) {
        try {
            $otp = random_int(100000, 999999);
        } catch (Exception $e) {
            $otp = rand(100000, 999999);
        }
        $sql = "UPDATE sign_up SET reset_password_opt='" . $otp . "' , reset_password_created_at='"
            . date('Y-m-d H:i:s') . "' WHERE mail='" . $email . "'";
            // Vérifie si la requête SQL s'est exécutée avec succès
        if (mysqli_query($cnx, $sql)) {
            // Vérifie si des lignes ont été affectées par la requête
            if (mysqli_affected_rows($cnx)) {
                  // Create a new PHPMailer instance
                 $mail = new PHPMailer();
                 try{
                    // Set the mailer to use SMTP
 // Définit l'utilisation de SMTP pour l'envoi d'e-mails                   
$mail->isSMTP();
// Définit l'hôte SMTP (serveur SMTP) à utiliser pour l'envoi d'e-mails
$mail->Host = 'smtp.gmail.com';

// Définit le port SMTP à utiliser
$mail->Port = 587;

// Active l'authentification SMTP
$mail->SMTPAuth = true;

// Définit le nom d'utilisateur SMTP (votre adresse e-mail)
$mail->Username   = 'beenyaagoubwided123@gmail.com';                     // Nom d'utilisateur SMTP
$mail->Password   = 'acixxskdltkxpftd';                               // Mot de passe SMTP

// Définit l'adresse e-mail de l'expéditeur
$mail->setFrom('beenyaagoubwided123@gmail.com', 'Wided Ben');

// Ajoute l'adresse e-mail du destinataire
$mail->addAddress($email);

// Définit le sujet de l'e-mail
$mail->Subject = 'Password Reset';


// Définit le contenu de l'e-mail
$mail->Body = 'Your OTP code is: ' . $otp;

 
 if ($mail->send()) {
    // Si l'email est envoyé avec succès, définir success sur true
    $response['success'] = true;
} else {
    // Si l'email n'a pas pu être envoyé, ajouter un message d'erreur à la réponse
    $response['error'] = 'Message could not be sent. Mailer Error: ' . $mail->ErrorInfo;
}
} catch (Exception $e) {
// En cas d'exception, ajouter un message d'erreur à la réponse
$response['error'] = 'An error occurred while sending the email: ' . $e->getMessage();
}
}
}
}

// Envoyer la réponse au format JSON
header('Content-Type: application/json');
echo json_encode($response);
exit; // Arrêter l'exécution du script après l'envoi de la réponse JSON
}
?>