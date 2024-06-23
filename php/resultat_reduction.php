<?php
// Inclure le fichier de connexion à la base de données
include("db_connect.php");

// Initialiser un tableau pour stocker les messages
$response = array();

// Vérifier si l'ID a été envoyé dans la requête POST
if(isset($_POST['id'])) {
    $id = $_POST['id'];

    // Requête SQL préparée pour récupérer la valeur du champ "reduction" pour l'ID spécifié
    $stmt = $cnx->prepare("SELECT reduction FROM score WHERE id = ?");
    $stmt->bind_param("i", $id);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        // Récupérer le résultat de la requête
        $row = $result->fetch_assoc();
        $reduction = $row["reduction"];

        // Ajouter la réduction au tableau de réponse
        $response['success'] = true;
        $response['total_score'] = $reduction; // Ajouter la réduction au tableau
    } else {
        $response['success'] = false;
        $response['message'] = "Aucun enregistrement trouvé pour l'ID spécifié.";
    }
} else {
    $response['success'] = false;
    $response['message'] = "ID non spécifié dans la requête.";
}

// Fermer la connexion à la base de données
$cnx->close();

// Retourner la réponse encodée en JSON
echo json_encode($response);
?>
