<?php
// Connexion à la base de données avec PDO
try {
    $pdo = new PDO('mysql:host=localhost;dbname=pfe', 'root', '');
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION); // Activer les erreurs PDO
} catch (PDOException $e) {
    die("Erreur de connexion à la base de données : " . $e->getMessage());
}
// Vérifier si l'ID a été envoyé dans la requête POST
if(isset($_POST['id'] )) {
    $id = $_POST['id'];
// Préparer la requête SQL avec un seul marqueur de paramètre
$query = $pdo->prepare("SELECT name, mail FROM sign_up WHERE id = ?");

// Exécuter la requête avec le bon nombre de paramètres
$query->execute([$id]);

// Récupérer les résultats
$result = $query->fetch(PDO::FETCH_ASSOC);
}

// Afficher les résultats
echo json_encode($result);
?>

