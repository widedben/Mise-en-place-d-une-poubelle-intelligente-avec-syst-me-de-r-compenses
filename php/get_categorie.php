<?php
// Connexion à la base de données avec PDO
try {
    $pdo = new PDO('mysql:host=localhost;dbname=pfe', 'root', '');
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION); // Activer les erreurs PDO
} catch (PDOException $e) {
    die("Erreur de connexion à la base de données : " . $e->getMessage());
}
// Initialiser une réponse vide
$response = array();

// Vérifier si l'ID a été envoyé dans la requête POST
if(isset($_POST['id'])) {
    $id = $_POST['id'];
    //lahna l  on testaml lel jointure fy blast il where w c.id=s.id bech ithabt les deux colonnes hathoka mawjoudin wele 
    // Requête SQL pour récupérer les valeurs des colonnes 'battrie', 'bouteille', 'carton' de la table 'categorie' pour l'utilisateur actuel
    $query = $pdo->prepare("SELECT c.batterie, c.bouteille, c.carton FROM categorie c INNER JOIN sign_up s ON c.id = s.id WHERE c.id = ?");
    //lahna il $id bch ithat fy requete ili fogi w taamlt hakeke juste pour evite les attaques par injection sql
    $query->execute([$id]);

    // Récupérer le résultat de la requête
    $category = $query->fetch(PDO::FETCH_ASSOC);

    // Vérifier si un résultat a été trouvé
    if ($category) {
        // Retourner les valeurs des colonnes 'battrie', 'bouteille', 'carton' au format JSON
        $response['success'] = true;
        $response['battrie'] = $category['batterie'];
        $response['bouteille'] = $category['bouteille'];
        $response['carton'] = $category['carton'];
    } else {
        // Aucune valeur trouvée pour l'utilisateur actuel
        $response['success'] = false;
        $response['message'] = "Aucune valeur trouvée pour l'utilisateur actuel.";
    }
} else {
    // Aucun ID n'a été envoyé
    $response['success'] = false;
    $response['message'] = "Aucun ID n'a été envoyé.";
}

// Retourner la réponse au format JSON
echo json_encode($response);
?>
