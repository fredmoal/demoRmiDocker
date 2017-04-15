# pre-requis :
# cd dans le répertoire racine des .class (target/classes pour ce projet maven)
# pour lister les serveurs actifs :
docker run --rm -v "$PWD":/run -w /run  --link serveur1 java:8-jre-alpine java client.Call serveur1
