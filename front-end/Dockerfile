# Develop stage
FROM node:alpine AS develop-stage
LABEL maintainer="development@ull.es"

ARG GITHUB_TOKEN
ENV GITHUB_TOKEN $GITHUB_TOKEN

ARG VITE_API_URL
ENV VITE_API_URL=$VITE_API_URL

WORKDIR /app

RUN echo @ull-tfg:registry=https://npm.pkg.github.com/ >> ./.npmrc
RUN echo //npm.pkg.github.com/:_authToken=$GITHUB_TOKEN >> ./.npmrc

COPY package*.json ./
RUN yarn install
COPY . .

# Build stage
FROM develop-stage AS build-stage
RUN yarn build

# Production stage
FROM nginx:stable-alpine AS production-stage
COPY --from=build-stage /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]