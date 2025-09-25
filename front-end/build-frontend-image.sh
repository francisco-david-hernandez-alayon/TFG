#!/bin/bash

# Usage: ./build-frontend-image.sh -t YOUR_GITHUB_TOKEN

while getopts t: flag; do
  case "${flag}" in
    t) GH_TOKEN=${OPTARG};;
  esac
done

if [ -z "$GH_TOKEN" ]; then
  echo "❌ Debes pasar el token con -t"
  echo "Ejemplo: ./build-frontend-image.sh -t ghp_123abc"
  exit 1
fi

echo "🚧 Construyendo la imagen Docker del frontend..."
docker build --build-arg GITHUB_TOKEN="$GH_TOKEN" -t ullsoftware/workflow-maker_front-end:latest -f Dockerfile .

if [ $? -eq 0 ]; then
  echo "✅ Imagen construida correctamente: ullsoftware/workflow-maker_front-end:latest"
  echo "🔄 Recreando el contenedor front-end con la nueva imagen..."
  docker-compose up -d --no-deps --build front-end
else
  echo "❌ Error al construir la imagen"
  exit 1
fi
