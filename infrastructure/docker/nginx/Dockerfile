# Build stage
FROM node:22.17.0-alpine3.22 AS build

WORKDIR /app

COPY package.json package-lock.json ./

RUN npm install --ignore-scripts

COPY . .

RUN npm run build

# Run stage
FROM nginx:1.29.0-alpine3.22

WORKDIR /app

COPY --from=build /app/dist /usr/share/nginx/html

CMD ["nginx", "-g", "daemon off;"]
