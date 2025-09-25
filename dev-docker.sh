#!/bin/bash

PRUNE=false

# Process options -p
while getopts ":p" opt; do
  case ${opt} in
    p )
      PRUNE=true
      ;;
    \? )
      echo "❌ Parámetro inválido: -$OPTARG" >&2
      exit 1
      ;;
  esac
done

# Stop Nginx and MongoDB services (ignorar errores si no están activos)
sudo systemctl stop nginx 2>/dev/null
sudo systemctl stop mongod 2>/dev/null

# Execute prune only if -p is on the command line
if $PRUNE; then
  echo "🧱 Limpiando build cache de Docker..."
  docker builder prune -f
  
  echo "🧼 Eliminando todas las imágenes de Docker..."
  IMAGES=$(docker images -q)
  if [ -n "$IMAGES" ]; then
    docker rmi -f $IMAGES
  else
    echo "No hay imágenes para eliminar."
  fi
fi

echo "🚫 Deteniendo todos los contenedores..."
CONTAINERS=$(docker ps -aq)
if [ -n "$CONTAINERS" ]; then
  docker stop $CONTAINERS
  echo "🧹 Eliminando contenedores..."
  docker rm $CONTAINERS
else
  echo "No hay contenedores para detener o eliminar."
fi

echo "🗑️ Eliminando imágenes del front-end y back-end..."
docker rmi ullsoftware/workflow-maker_front-end:latest ullsoftware/workflow-maker_back-end:latest 2>/dev/null || true

echo "🔧 Reconstruyendo y levantando servicios desde 'docker-compose-test.yml'..."
docker compose --env-file .env -f docker-compose-test.yml up --build
