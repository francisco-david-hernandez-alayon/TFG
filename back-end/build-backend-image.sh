#!/bin/bash

# Usage: ./build-backend-image.sh -t YOUR_GITHUB_TOKEN

while getopts t: flag; do
  case "${flag}" in
    t) GH_TOKEN=${OPTARG};;
  esac
done

if [ -z "$GH_TOKEN" ]; then
  echo "❌ Debes pasar el token con -t"
  echo "Ejemplo: ./build-backend-image.sh -t ghp_123abc"
  exit 1
fi

echo "🛠️ Compilando el backend con Maven (sin tests)..."
mvn clean package -DskipTests

echo "🚧 Construyendo la imagen Docker del backend..."
docker build --build-arg GITHUB_TOKEN="$GH_TOKEN" -t ullsoftware/workflow-maker_back-end:latest -f Dockerfile .

if [ $? -eq 0 ]; then
  echo "✅ Imagen construida correctamente: ullsoftware/workflow-maker_back-end:latest"
  echo "🔄 Recreando el contenedor back-end con la nueva imagen..."
  docker compose up -d --no-deps --build back-end

else
  echo "❌ Error al construir la imagen"
  exit 1
fi
