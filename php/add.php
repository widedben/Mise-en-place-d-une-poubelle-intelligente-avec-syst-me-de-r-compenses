<?php
// Inclure le fichier de connexion à la base de données
include ("db_connect.php");

// Définir l'en-tête pour indiquer que la réponse est au format JSON et encodée en UTF-8
header('Content-type: application/json; charset=utf-8');

// Initialiser un tableau pour la réponse JSON
$response=array();

$req = null; // Déclaration explicite de la variable $req
// Vérifier si les paramètres 'nom' et 'age' sont présents dans la requête POST
if( isset($_GET["nom"]) && isset($_GET["age"]) ){
    // Récupérer les valeurs des paramètres 'nom' et 'age' sans les modifier
    $nom=$_GET["nom"];
    $age=$_GET["age"];

    // Exécuter une requête d'insertion dans la table 'test1' avec les valeurs fournies
    
    $req = mysqli_query($cnx, "insert into test(name,age) values('$nom','$age')");
    

    // Vérifier si la requête d'insertion a réussi
    if($req){
        // Si oui, définir le succès sur 1 et afficher un message de réussite
        $response["success"]=1;
        $response["message"]="inserted !";
    }
    else{
        // Si la requête a échoué, définir le succès sur 0 et afficher un message d'erreur
        $response["success"]=0;
        $response["message"]="request error !";
    }
}
else{
    // Si les paramètres 'nom' et 'age' sont manquants, définir le succès sur 0 et afficher un message d'erreur
    $response["success"]=0;
    $response["message"]="required field is missing!";
}

// Retourner la réponse JSON
echo json_encode($response);
?>
