<?php

// Inclure le fichier de connexion à la base de données
include("db_connect.php");

// Initialiser un tableau pour stocker les messages
$response = array();

// Vérifier si l'ID a été envoyé dans la requête POST
if(isset($_POST['id'])) {
    $id = $_POST['id'];

    // Requête SQL pour faire la somme des colonnes bouteille, carton et batterie pour l'ID spécifié
    $sql = "SELECT SUM(bouteille + carton + batterie) AS total FROM categorie WHERE id = $id";
    $result = $cnx->query($sql);

    if ($result->num_rows > 0) {
        // Récupérer le résultat de la requête
        $row = $result->fetch_assoc();
        $total = $row["total"];

        // Ajouter le score total au tableau de réponse
    $response['success'] = true;
    $response['total_score'] = $total; // Ajouter le score total au tableau
    

        // Vérifier si l'ID existe déjà dans la table score
        $check_query = "SELECT id FROM score WHERE id = $id";
        $check_result = $cnx->query($check_query);

        if ($check_result->num_rows > 0) {
            // L'ID existe déjà, vous pouvez mettre à jour la valeur existante si nécessaire
            $update_query = "UPDATE score SET score = $total WHERE id = $id";
            if ($cnx->query($update_query) === TRUE) {
                $response['update_message'] = "La valeur du score a été mise à jour avec succès.";
            } else {
                $response['update_message'] = "Erreur lors de la mise à jour du score : " . $cnx->error;
            }
        } else {
            // L'ID n'existe pas, vous pouvez l'insérer
            $insert_query = "INSERT INTO score (id, score) VALUES ($id, $total)";
            if ($cnx->query($insert_query) === TRUE) {
                $response['insert_message'] = "L'insertion dans le tableau score a été effectuée avec succès.";
            } else {
                $response['insert_message'] = "Erreur lors de l'insertion dans le tableau score : " . $cnx->error;
            }
        }
    } else {
        $response['success'] = false;
        $response['message'] = "Aucun résultat trouvé pour l'ID $id dans la table categorie.";
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
