# pre-requis :
# cd dans le répertoire racine des .class (target/classes pour ce projet maven)
# lance 4 serveurs
docker run -p 1099:1099 -p 1234:1234 -d -v "$PWD":/run -w /run --name serveur1 java:8-jre-alpine rmiregistry
docker exec -d serveur1 java -Djava.security.policy=server.policy -Djava.rmi.server.hostname="192.168.99.100" serveur.Run
docker run -d -v "$PWD":/run -w /run --name serveur2 --link serveur1 java:8-jre-alpine rmiregistry
docker exec -d serveur2 java -Djava.security.policy=server.policy serveur.Run serveur1
docker run -d -v "$PWD":/run -w /run --name serveur3 --link serveur1 java:8-jre-alpine rmiregistry
docker exec -d serveur3 java -Djava.security.policy=server.policy serveur.Run serveur1
# le 4eme s'enregistre auprès de serveur 2 :
docker run -d -v "$PWD":/run -w /run --name serveur4 --link serveur2 java:8-jre-alpine rmiregistry
docker exec -d serveur4 java -Djava.security.policy=server.policy serveur.Run serveur2

# pour lister les serveurs actifs :
# docker run --rm -v "$PWD":/run -w /run  --link serveur1 java:8-jre-alpine java client.Call serveur1
