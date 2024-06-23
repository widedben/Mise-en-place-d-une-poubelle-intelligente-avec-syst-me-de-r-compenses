<?php 
// Connexion à la base de données avec PDO
try {
    $pdo = new PDO('mysql:host=localhost;dbname=pfe', 'root', '');
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION); // Activer les erreurs PDO
} catch (PDOException $e) {
    die("Erreur de connexion à la base de données : " . $e->getMessage());
}
$response['success'] = true; // Définir le succès sur true par défaut
$response['data'] = array(
    'toast_message' => '',
    'update_message' => '',
    'error_message' => '',
    'new_reduction' => 0, 
    'received_data' => $_POST // Ajouter cette ligne pour afficher les données reçues
// Initialiser la réduction à 0 par défaut
);
$phpMailerDir = 'C:\xampp\htdocs\PHPMailer\vendor\phpmailer\phpmailer';
 require 'C:\xampp\htdocs\PHPMailer\vendor\autoload.php';

 require_once $phpMailerDir . '/src/PHPMailer.php';
 require_once $phpMailerDir . '/src/SMTP.php';
 require_once $phpMailerDir . '/src/Exception.php';
 
 use PHPMailer\PHPMailer\PHPMailer;
 use PHPMailer\PHPMailer\SMTP;
 use PHPMailer\PHPMailer\Exception;

// Vérifier si les données nécessaires sont présentes dans la requête POST
if (isset($_POST['id']) && isset($_POST['mail']) && isset($_POST['marque'])) {
    $id = $_POST['id'];
    $email = $_POST['mail'];
    $marque = $_POST['marque'];
    // Vérifier si l'ID existe déjà dans la table score
    $check_query = "SELECT  reduction FROM score WHERE id = $id";
    $check_result = $pdo->query($check_query);

    if ($check_result->rowCount() > 0) {
        // L'ID existe déjà, récupérer la réduction
        $row = $check_result->fetch(PDO::FETCH_ASSOC);
        $reduction = $row['reduction'];
// Initialiser $newReduction à la valeur actuelle de réduction
$newReduction = $reduction;

// Ajouter une condition selon la marque pour changer le nombre de réduction
switch ($marque) {
    case "Shein":
        if ($reduction < 7000) {
            $response['data']['toast_message'] = "La réduction actuelle est inférieure à 7000. Pas de modification nécessaire.";
        } else {
            $newReduction = $reduction - 7000;
        }
        break;
    case "Hamadi":
        if ($reduction < 2500) {
            $response['data']['toast_message'] = "La réduction actuelle est inférieure à 2500. Pas de modification nécessaire.";
        } else {
            $newReduction = $reduction - 2500;
        }
        break;
    case "Geant":
        if ($reduction < 5000) {
            $response['data']['toast_message'] = "La réduction actuelle est inférieure à 5000. Pas de modification nécessaire.";
        } else {
            $newReduction = $reduction - 5000;
        }
        break;
    case "Huwei":
        if ($reduction < 100000) {
            $response['data']['toast_message'] = "La réduction actuelle est inférieure à 100000. Pas de modification nécessaire.";
        } else {
            $newReduction = $reduction - 100000;
        }
        break;
    default:
    
        break;
}

// Utilisez $newReduction ici pour d'autres opérations si nécessaire

if ($newReduction != $reduction) {
    // Mettre à jour la réduction dans la base de données
            $update_query = "UPDATE score SET reduction = $newReduction WHERE id = $id";
            if ($pdo->query($update_query)) {
                $response['data']['update_message'] = "La valeur de la réduction a été réduite avec succès.";
                $response['data']['new_reduction'] = $newReduction; // Ajouter la nouvelle réduction à la réponse

                if ($pdo) {
                    try {
                        $otp = random_int(100000, 999999);
                    } catch (Exception $e) {
                        $otp = rand(100000, 999999);
                    }
                  
                              // Create a new PHPMailer instance
                             $mail = new PHPMailer();
                             try{
                                // Set the mailer to use SMTP
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
            
            // Set the subject line for the email
            $mail->Subject = 'Your Promo Code for ' . $marque;

            
            
            
            // Créez le corps de l'e-mail avec la marque
            $mail->Body = 'Dear Customer,

           Congratulations! You have been selected to receive a special discount from ' . $marque . '! This exclusive promo code is our way of saying thank you for your loyalty.

           Promo Code: ' . $otp . '

          Simply use this code during checkout on your next purchase to enjoy the discount. Hurry, as this offer is valid for a limited time only.

          We look forward to serving you again soon.

         Best regards,
         
         Your ' . $marque . ' Team';


            
             // Attempt to send the email
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
            
            } else {
                $response['data']['update_message'] = "Erreur lors de la mise à jour de la réduction : " . $pdo->errorInfo();
            }
        }else {
            // Aucune modification de réduction n'est nécessaire, garder la réduction actuelle
            $newReduction = $reduction;
            $response['data']['toast_message'] = "Aucune modification de réduction n'est nécessaire.";
            $response['data']['new_reduction'] = $reduction; // Ajouter la nouvelle réduction à la réponse
        }
        
    } else {
        $response['data']['toast_message'] = "La réduction actuelle est inférieure à celle requise. Aucune modification n'est nécessaire.";
        
    }
} else {
    // Si les données requises ne sont pas présentes, définir un message d'erreur
    $response['error_message'] = "Les données requises ne sont pas présentes dans la requête.";
}
   

// Convertir la réponse en JSON et l'afficher
header('Content-Type: application/json');
// Convertir la réponse en JSON et l'afficher
echo json_encode($response);
?>
