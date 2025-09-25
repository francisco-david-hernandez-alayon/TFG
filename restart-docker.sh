#!/bin/bash

PRUNE=false
PROFILE="full"

# Procesar opciones -p y -b
while getopts ":pb" opt; do
  case ${opt} in
    p )
      PRUNE=true
      ;;
    b )
      PROFILE="back-end"
      ;;
    \? )
      echo "❌ Parámetro inválido: -$OPTARG" >&2
      exit 1
      ;;
  esac
done

# Ejecutar prune solo si se especificó -p
if $PRUNE; then
  echo "🧱 Limpiando build cache de Docker..."
  docker builder prune -f
  
  echo "🧼 Eliminando todas las imágenes de Docker..."
  docker rmi -f $(docker images -q)
fi

echo "🚫 Deteniendo todos los contenedores..."
docker stop $(docker ps -aq)

echo "🧹 Eliminando contenedores..."
docker rm $(docker ps -aq)

echo "🗑️ Eliminando imágenes del front-end y back-end..."
docker rmi ullsoftware/workflow-maker_front-end:latest ullsoftware/workflow-maker_back-end:latest

echo "🔧 Reconstruyendo y levantando servicios con perfil '$PROFILE'..."
docker compose --profile "$PROFILE" up --build


