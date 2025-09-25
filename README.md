# Workflow maker
Autor: Francisco David Hernández Alayón

<br>
<br>

## Configuración del entorno
Características de la máquina donde se han seguido los siguientes pasos:
```Linux debian 6.1.0-26-amd64 #1 SMP PREEMPT_DYNAMIC Debian 6.1.112-1 (2024-09-30) x86_64 GNU/Linux.```

### Docker
1. Instalar la última versión estable de docker mediante un script.
```
 curl -fsSL https://get.docker.com -o get-docker.sh
 sudo sh get-docker.sh --dry-run
```
2. Eliminar fichero del script de instalación.
```
rm get-docker.sh
```
3. Comprobar versión de docker.
```
docker -v
```
4. Ejecutar docker sin usar sudo.
```
sudo groupadd docker
sudo usermod -aG docker $USER
newgrp docker
```

<br>

### Docker Compose
1. Instalar plugin
```
DOCKER_CONFIG=${DOCKER_CONFIG:-$HOME/.docker}
mkdir -p $DOCKER_CONFIG/cli-plugins
curl -SL https://github.com/docker/compose/releases/download/v2.30.3/docker-compose-linux-x86_64 -o $DOCKER_CONFIG/cli-plugins/docker-compose
```
2. Dar permisos de ejecución al ejecutable de docker compose 
```
chmod +x $DOCKER_CONFIG/cli-plugins/docker-compose
```
3. Comprobar versión de docker compose
```
docker-compose -v
```

<br>

### Node.js
Instalar Node
```
sudo apt-get update
sudo apt-get install -y ca-certificates curl gnupg
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://deb.nodesource.com/gpgkey/nodesource-repo.gpg.key | sudo gpg --dearmor -o /etc/apt/keyrings/nodesource.gpg
NODE_MAJOR=20
echo "deb [signed-by=/etc/apt/keyrings/nodesource.gpg] https://deb.nodesource.com/node_$NODE_MAJOR.x nodistro main" | sudo tee /etc/apt/sources.list.d/nodesource.list
sudo apt-get update
sudo apt-get install nodejs -y
```
Versión de Node
```
node -v
```

<br>

### Yarn
Instalar Yarn
```
curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | gpg --dearmor | sudo tee /etc/apt/trusted.gpg.d/yarn.gpg
echo "deb [signed-by=/etc/apt/trusted.gpg.d/yarn.gpg] https://dl.yarnpkg.com/debian/ stable main" | sudo tee /etc/apt/sources.list.d/yarn.list
sudo apt update
sudo apt install yarn
```

Versión de Yarn
```
yarn --version
```

<br>

### Java
Instalar Java en Debian
```
sudo apt update
sudo apt install openjdk-17-jdk
wget https://download.oracle.com/java/17/archive/jdk-17.0.12_linux-x64_bin.tar.gz
tar -xvzf jdk-17.0.12_linux-x64_bin.tar.gz
sudo mv jdk-17.0.12 /opt/
vi ~/.bashrc
```
Añdir al fichero 
```
export JAVA_HOME=/opt/jdk-17.0.12
export PATH=$JAVA_HOME/bin:$PATH
```
Ejecutar el siguiente comando
```
source ~/.bashrc
```
Versión de Java(al menos *17.0.6*)
```
java -version
```

<br>

### Compilación de Java con Maven
Herramienta que ayuda a construir los componentes en Java de una forma sencilla. Instalar Maven:
```
wget https://dlcdn.apache.org/maven/maven-3/3.9.5/binaries/apache-maven-3.9.5-bin.tar.gz -P /tmp
sudo tar xf /tmp/apache-maven-3.9.5-bin.tar.gz -C /opt
sudo mv /opt/apache-maven-3.9.5 /opt/maven
```
Configurar Maven
```
sudo vim /etc/profile.d/maven.sh
```
incluir lo siguiente
```
export JAVA_HOME=/usr/lib/jvm/java-17-oracle
export M3_HOME=/opt/maven
export MAVEN_HOME=/opt/maven
export PATH=${M3_HOME}/bin:${PATH}
```
Guardar variables de entorno
```
sudo chmod +x /etc/profile.d/maven.sh
source /etc/profile.d/maven.sh
```
Comprobar estado de maven en la maquina
```
mvn -v
```

<br>

### Mongodb
Instalar mongodb y habilitar servicio
```
wget -qO - https://pgp.mongodb.com/server-7.0.asc | sudo tee /etc/apt/trusted.gpg.d/mongodb-server-7.0.asc
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/7.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-7.0.list
sudo apt update
sudo apt install -y mongodb-org
sudo systemctl enable mongod
```

Iniciar mongo 
```
sudo systemctl start mongod
```


<br>

### Backend
Rellenar formulario y descargar zip de [SpringBoot Initializer](https://start.spring.io/)
```
sudo apt-get install unzip
unzip back-end.zip
rm back-end.zip
```
Compilar backend y, tras esto, debe existir ```back-end/target/back-end-1.0-SNAPSHOT.jar```
```
cd back-end
mvn clean package
```
Crear imagen docker
```
cd back-end
mvn clean package
docker build -t dockerizedbackend .
```
Ver imagen docker
```
docker images
```
Crear contenedor docker a partir de imagen
```
docker run -p 8081:8080 --name mycontainerback dockerizedbackend
```
Eliminar contenedores docker
```
docker rm -f $(docker ps -aq)
```


<br>

### FrontEnd
Creamos el FrontEnd usando vite en la raíz del repositorio
```
yarn create vite
```

Desplegar FrontEnd en local
```
cd front-end
yarn
yarn dev
```

Añadir ull-tfg-typescript.
```
touch .npmrc
echo "//npm.pkg.github.com/:_authToken=\${GITHUB_TOKEN}" >> .npmrc
echo "@ull-tfg:registry=https://npm.pkg.github.com" >> .npmrc
export GITHUB_TOKEN=<PERSONAL_ACCESS_TOKEN>
yarn add @ull-tfg/ull-tfg-typescript@1.0.0
```

Añadir vue-flow
```
npm install @vue-flow/core
npm install @vue-flow/background
npm install @vue-flow/controls 
npm install @vue-flow/minimap 
```


Crear imagen docker
```
cd front-end
docker build -t dockerizedfrontend . --build-arg GITHUB_TOKEN=<PERSONAL_ACCESS_TOKEN>
```
Ver imagen docker
```
docker images
```

Ejecutar docker
```
docker run -p 8090:80 --name mycontainerfront dockerizedfrontend
```
Eliminar contenedores docker
```
docker rm -f $(docker ps -aq)
```

<br>
<br>

## Ejecución
### Ejecutar docker-compose despliege
```
docker-compose --profile full up 
```

### Realizar cambios en frontend mientras está activo el docker-compose para dev
```
cd frontend
./build-frontend-image.sh -t YOUR_GITHUB_TOKEN
```
### Realizar cambios en el backend mientras está activo el docker-compose para dev
```
cd backend
./build-backend-image.sh -t YOUR_GITHUB_TOKEN
```

### Ejecutar docker-compose de para dev
Para esta opción es necesario crear un archivo .env donde se indice el parametro GH_TOKEN=YOUR_GITHUB_TOKEN
```
./dev-docker.sh -p
```

## Ejecutar back-end
Pasar variables de entorno y ejecutar mvn
```
SHARED_CONTAINER_PATH=/home/david/workflow-maker/files
HOST_FILES_PATH=/home/david/workflow-maker/files
SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/db-application \
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9000

```

## Ejecutar front-end
```
yarn dev
```


<br>
<br>


## Subir imagen a Docker Hub en linux
```
docker login
cd /mi-proyecto
docker build -t usuario-dockerhub/mi-imagen:tag .
docker push usuario-dockerhub/mi-imagen:tag
```

## Crear servidor pyhton para subir fichero
Situarse en el directorio donde están los ficheros y ejecutar:
```
python3 -m http.server 8000 --bind 0.0.0.0
```
Se accede desde http://<tu-IP>:8000/archivo.txt
(Para saber la ip: ip a)



<br>
<br>

## Código

### Ejemplo de petición a la API en local 
GET
```
http://localhost:8080/flows
```


<br>
<br>

## Recursos
* [Cómo Instalar Docker](https://docs.docker.com/engine/install/ubuntu/)
* [Funcionamiento Básico de Docker](https://www.freecodecamp.org/espanol/news/guia-de-docker-para-principiantes-como-crear-tu-primera-aplicacion-docker/)
* [Cómo instalar Docker Compose](https://docs.docker.com/compose/install/)
* [Node.js](https://nodejs.org/en)
* [Yarn](https://yarnpkg.com/)
* [Java](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
* [SpringBoot Initializer](https://start.spring.io/)