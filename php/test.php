<?php
// Inclure le fichier de connexion à la base de données
include("db_connect.php");
// Récupération des données de la requête
$id = $_POST['id'];
$bouteille = $_POST['bouteille'];
$carton = $_POST['carton'];
$batterie = $_POST['batterie'];

// Vérifier si l'ID existe déjà dans la table
$sql_select = "SELECT * FROM categorie WHERE id = '$id'";
$result = $cnx->query($sql_select);

if ($result->num_rows > 0) {
    // L'ID existe déjà, mettre à jour les valeurs
    $row = $result->fetch_assoc();
    $new_bouteille = $row['bouteille'] + $bouteille; // Ajouter la nouvelle valeur à la valeur existante
    $new_carton = $row['carton'] + $carton; // Ajouter la nouvelle valeur à la valeur existante
    $new_batterie = $row['batterie'] + $batterie; // Ajouter la nouvelle valeur à la valeur existante
    // Calculer la nouvelle valeur de "reduction" en ajoutant la somme calculée
    $nouvelle_reduction =  $bouteille + $carton + $batterie;

   
      // Mettre à jour la ligne existante
       $sql_update = "UPDATE categorie SET bouteille = '$new_bouteille', carton = '$new_carton', batterie = '$new_batterie' WHERE id = '$id'";

       if ( $cnx->query($sql_update) === TRUE) {
        echo "Enregistrement mis à jour avec succès";
    } else {
        echo "Erreur lors de la mise à jour de l'enregistrement: " . $cnx->error;
    }
     // Mettre à jour le champ 'reduction' dans la table 'score'
     $sql_reduction_update = "UPDATE score SET reduction = reduction +   $nouvelle_reduction WHERE id = '$id'";
     if ($cnx->query($sql_reduction_update) === TRUE) {
         echo "Réduction mise à jour avec succès";
     } else {
         echo "Erreur lors de la mise à jour de la réduction: " . $cnx->error;
     }



     // Vérifier si l'utilisateur a un parrain
$sql_parrain = "SELECT * FROM sign_up WHERE id = '$id' AND parrain IS NOT NULL";
$result_parrain = $cnx->query($sql_parrain);

if ($result_parrain->num_rows > 0) {
    // L'utilisateur a un parrain, récupérer  parrain
    //récupère la première ligne de résultat de la requête SQL qui a été exécutée précédemment
    $parrain_row = $result_parrain->fetch_assoc();
    $parrain_id = $parrain_row['parrain'];

    // Calculer la nouvelle réduction pour le parrain (2% de la somme des points de l'utilisateur)
    $nouvelle_reduction_parrain = $nouvelle_reduction * 0.02;

    // Mettre à jour le champ 'reduction' dans la table 'score' pour le parrain
    $sql_parrain_update = "UPDATE score SET reduction = reduction + $nouvelle_reduction_parrain WHERE ton_code = '$parrain_id'";
    $cnx->query($sql_parrain_update);
    if ($cnx->query($sql_parrain_update) === TRUE) {
        echo "Réduction mise à jour pour le parrain avec succès";
    } else {
        echo "Erreur lors de la mise à jour de la réduction pour le parrain: " . $cnx->error;
    }
} else {
    echo "L'utilisateur n'a pas de parrain";
}



} else {
    // L'ID n'existe pas, créer une nouvelle ligne
    $sql_insert = "INSERT INTO categorie (id, batterie, carton, bouteille) VALUES ('$id', '$batterie', '$carton', '$bouteille')";
    if ($cnx->query($sql_insert) === TRUE) {
        echo "Nouvel enregistrement créé avec succès";
    } else {
        echo "Erreur lors de la création de l'enregistrement: " . $cnx->error;
    }
}

// Fermeture de la connexion à la base de données
$cnx->close(); 
?>
