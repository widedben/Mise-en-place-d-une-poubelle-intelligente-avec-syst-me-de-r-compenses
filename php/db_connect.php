<?php
// Connexion au serveur MySQL
$cnx=mysqli_connect("localhost","root","");

// Vérifier la connexion
if (!$cnx) {
    die("Erreur de connexion au serveur : " . mysqli_connect_error());
}

// Sélection de la base de données
$db=mysqli_select_db($cnx,"pfe");

// Vérifier la sélection de la base de données
if (!$db) {
    die("Erreur de connexion à la base de données : " . mysqli_error($cnx));
}


?>
