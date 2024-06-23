pour utilise le lecteur rfid : 
sudo raspi-config

lsmod | grep spi

sudo apt update
sudo apt upgrade

sudo apt install python3-dev python3-pip python3-venv

sudo pip3 install spidev

sudo pip3 install mfrc522
 pour installé tflite: 
alors,  il faut taper:

sudo apt-get install libatlas-base-dev
 et puis installer les packages suivants avec les versions indiquées (avec pip) :

 numpy>=1.20.0 


protobuf>=3.18.0,<4 installer 3.19

pillow


tflite-runtime==2.7

tflite-support>=0.4.2


si tout va bien, tu peux tester la commande suivante dans un terminal à partir d'un répertoire contenant  les documents ci-oint:

python run.py -s 128 -m fp_16_model.tflite -i Te-glTr_0002.jpg