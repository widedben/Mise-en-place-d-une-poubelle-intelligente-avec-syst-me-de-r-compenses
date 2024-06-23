<?php
// Inclure le fichier de connexion à la base de données
include("db_connect.php");

// Initialiser une réponse vide
$response = array();

// Vérifier si les champs "id" et "password" ont été envoyés en POST
if (isset($_POST["id"]) && isset($_POST["password"])) {
    // Récupérer les valeurs des champs "id" et "password"
    $id = $_POST["id"];
    $password = $_POST["password"];

    // Préparer une requête SQL pour sélectionner l'utilisateur avec l'id et le mot de passe fournis
    $req = mysqli_prepare($cnx, "SELECT * FROM sign_up WHERE id=? AND password=?");
 // Lier les valeurs des paramètres aux marqueurs de paramètre dans la requête préparée
// Cela sécurise la requête contre les attaques par injection SQL
// et assure que les valeurs des paramètres sont correctement interprétées par MySQL
    mysqli_stmt_bind_param($req, "ss", $id, $password);
    // Exécuter la requête préparée
    mysqli_stmt_execute($req);
    // Récupérer le résultat de la requête
    $result = mysqli_stmt_get_result($req);

    // Vérifier si des lignes correspondantes ont été trouvées dans la base de données
    if (mysqli_num_rows($result) > 0) {
        // Si oui, définir success à 1 et un message de succès
        $response["success"] = 1;
        $response["message"] = "Connexion réussie";
    } else {
        // Sinon, définir success à 0 et un message d'erreur
        $response["success"] = 0;
        $response["message"] = "Aucun utilisateur avec cet id ou mot de passe";
    }
} else {
    // Si les champs requis sont manquants, définir success à 0 et un message d'erreur
    $response["success"] = 0;
    $response["message"] = "Champ requis manquant";
}

// Retourner la réponse au format JSON
header('Content-Type: application/json');
echo json_encode($response);
?>
