<?php
// Inclure le fichier de connexion à la base de données
include("db_connect.php");

// Récupérer le code parrain envoyé par l'application (à adapter selon votre méthode d'envoi)
$codeParrain = 207080;

// Sélectionner les données des personnes ayant ce code parrain
$sql = "SELECT ton_code , name , reset_password_created_at , mail FROM  sign_up WHERE parrain = '$codeParrain'";
// Exécute la requête SQL spécifiée dans $sql en utilisant l'objet de connexion PDO $cnx
$result = $cnx->query($sql);

// Vérifier si des résultats ont été trouvés
if ($result->num_rows > 0) {
    // Créer un tableau pour stocker les données
    $data = array();

    // Parcourir les résultats et ajouter chaque ligne au tableau de données
    while($row = $result->fetch_assoc()) {
        $data[] = $row;
    }

    // Retourner les données au format JSON
    header('Content-Type: application/json');
    echo json_encode(array("data" => $data));
} else {
    echo "0 results";
}

// Fermer la connexion à la base de données
$cnx->close();
?>
