import requests
import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522

# Configuration de la connexion au serveur PHP
url = ' https://7fa0-165-51-103-27.ngrok-free.app/php/test.php'

# Configuration du lecteur RFID
reader = SimpleMFRC522()

# Variable de verrouillage
verrouillage = False

try:
    while True:
        if not verrouillage:
            # Attente de la détection d'une carte RFID
            print("Attente de la détection d'une carte RFID...")
            id, text = reader.read()
            print("Carte détectée. ID:", id)

        
            # Envoi des données au serveur PHP
            data = {
                'id': id,
                'batterie': '10',
                'carton': '20',
                'bouteille': '50'
            }
            response = requests.post(url, data=data)
            print("Réponse du serveur:", response.text)

            # Réinitialiser la variable de verrouillage
            verrouillage = False
except KeyboardInterrupt:
    GPIO.cleanup()
