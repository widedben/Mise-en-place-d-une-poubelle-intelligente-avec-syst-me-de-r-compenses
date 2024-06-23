import requests
import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522
from picamera import PiCamera
import time
import numpy as np
from PIL import Image
import tflite_runtime.interpreter as tflite
import argparse
from gtts import gTTS
import os
GPIO.setwarnings(False)
# Définir le mode de numérotation des broches GPIO en BOARD
GPIO.setmode(GPIO.BOARD)
GPIO.setup(11, GPIO.OUT)
pwm = GPIO.PWM(11, 50)
pwm.start(0)
# Fonction pour définir l'angle du servo moteur
def set_servo_angle(angle):
    #convertir l'angle en un signal que le moteur peux comprendre pour ce positionner a cet angle 
    duty_cycle = angle / 18 + 2
    # Appliquer le cycle de service au servo moteur pour le positionner
    pwm.ChangeDutyCycle(duty_cycle)
    time.sleep(1)

# Position fixe du servo moteur (position centrale)
position_fixe = 90     

# désactive les avertissements qui peuvent être émis par la bibliothèque RPi.GPIO comme des messages qui informent l'utilisateur sur l'utilisation incorrecte 
GPIO.setwarnings(False)

# Configuration de la connexion au serveur PHP
url = ' https://8d71-196-235-144-237.ngrok-free.app/php/test.php'

# Configuration du lecteur RFID
reader = SimpleMFRC522()

# Initialisation de la caméra
camera = PiCamera()
#une résolution de 640x480 signifie qu'une image a une largeur de 640 pixels et une hauteur de 480 pixels
camera.resolution = (640, 480)

# Variable de verrouillage
verrouillage = False
def text_to_speech(text, lang='fr'):
    # Créer une instance gTTS avec le texte et la langue
    tts = gTTS(text=text, lang=lang)
    
    # Sauvegarder le fichier audio généré
    tts.save("output.mp3")
    
    # Lire le fichier audio sans afficher les commentaires de mpg321
    os.system("mpg321 output.mp3 > /dev/null 2>&1")
# Construire l'analyseur d'arguments et analyser les arguments
ap = argparse.ArgumentParser()
ap.add_argument("-m", "--model", required=True, help="chemin vers le modèle TFLite")
ap.add_argument("-i", "--image", required=True, help="chemin vers l'image de test")
ap.add_argument("-s", "--size", required=True, type=int, help="taille de l'image")
args = vars(ap.parse_args())

# Charger le modèle TFLite et allouer les tenseurs
interpreteur = tflite.Interpreter(model_path=args["model"])
interpreteur.allocate_tensors()

# Obtenir les tenseurs d'entrée et de sortie
details_entree = interpreteur.get_input_details()
details_sortie = interpreteur.get_output_details()

try:
    while True:
        if not verrouillage:
            # Lecture de l'ID RFID
            print("Approchez votre carte RFID...")
             # Lecture de l'ID RFID
            text_to_speech("Approchez votre carte RFID")
            id, text = reader.read()
            
            print("ID de la carte RFID:", id)

            # Envoyer un message pour indiquer que la lecture de l'ID RFID est réussie
            print("La capture d'image sera déclenchée dans 3 secondes...")
            text_to_speech("La capture d'image sera déclenchée dans 3 secondes...")
            # Attendre 3 secondes avant de déclencher la capture d'image
            time.sleep(3)

           # Capture d'une image avec la caméra
            camera.capture('captured_image.jpg')
            print("Image capturée !")
            text_to_speech("Image capturée !")

            # Charger l'image
            image = Image.open('captured_image.jpg')
            image = image.convert('RGB')
            image = image.resize((args["size"], args["size"]))
            if "int" in args["model"]:
                image = np.array(image, dtype=np.uint8)
            else:
                image = np.array(image, dtype=np.float32)
                image /= 255.0
            image = np.expand_dims(image, axis=0)

            # Exécuter l'inférence et mesurer le temps
            # Enregistrer le temps de début de l'inférence sachant quue l'inference est l'etape où le model applique ce qu'il appris lors de l'entrainment
            temps_debut = time.time()
            # Définir les données d'entrée de l'interpréteur TFLite avec l'image prétraitée
            interpreteur.set_tensor(details_entree[0]['index'], image)
            interpreteur.invoke()
            temps_fin = time.time()

            # Obtenir le tenseur de sortie et afficher le résultat
            donnees_sortie = interpreteur.get_tensor(details_sortie[0]['index'])
            # Déterminer la classe prédite en trouvant l'indice du maximum dans les données de sortie
            classe_predite = donnees_sortie.squeeze().argmax(axis=0)
            # Mapper l'indice de la classe prédite à un nom de classe
            noms_classes = ["Battrie", "Bouteille", "carton"]
            resultat_inference = noms_classes[classe_predite]
            print("Résultat de l'inférence pour l'ID", id, ":", resultat_inference)
            print("Temps d'inférence pour l'ID", id, ":", temps_fin - temps_debut, "secondes")

            # Envoi des données au serveur PHP en fonction du résultat de l'inférence
            data = {
                'id': id,
                'batterie': 0,
                'carton': 0,
                'bouteille': 0
            }

            if resultat_inference == "Bouteille":
                text_to_speech("Bouteille détectée !Veuillez placer la bouteille dans le cylindre approprié. ")
                set_servo_angle(180) # Tourner à droite pour la bouteille
                # Attendre 2 secondes avant de revenir à la position fixe
                time.sleep(2)
                set_servo_angle(position_fixe)
                data = {
                    'id': id,
                    'batterie': 0,
                    'carton': 0,
                    'bouteille': 30
                }
            elif resultat_inference == "carton":
                text_to_speech("Carton détecté !Veuillez placer le carton dans le cylindre approprié ") 
                set_servo_angle(0)   # Tourner à gauche pour le carton
                # Attendre 2 secondes avant de revenir à la position fixe
                time.sleep(2)
                set_servo_angle(position_fixe)
                data = {
                    'id': id,
                    'batterie': 0,
                    'carton': 20,
                    'bouteille': 0
                }
            elif resultat_inference == "Battrie": 
                text_to_speech("Batterie détectée !Veuillez placer la  batterie dans le cylindre approprié " ) 
                set_servo_angle(270) # Aller à la position centrale pour la batterie
                
               # Attendre 2 secondes avant de revenir à la position fixe
                time.sleep(2)
                set_servo_angle(position_fixe)
                data = {
                    'id': id,
                    'batterie': 50,
                    'carton': 0,
                    'bouteille': 0
                }

            response = requests.post(url, data=data)
            print("Réponse du serveur:", response.text)
            text_to_speech("Réponse du serveur: " + response.text)


            # Réinitialiser la variable de verrouillage
            verrouillage = False
except KeyboardInterrupt: 

    GPIO.cleanup()
    camera.close()

