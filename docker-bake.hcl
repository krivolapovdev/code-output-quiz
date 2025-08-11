group "default" {
  targets = [
    "config-server",
    "eureka-server",
    "auth-service",
    "notification-service",
    "quiz-service",
    "user-service",
    "api-gateway",
    "nginx",
    "prometheus",
    "loki",
    "grafana"
  ]
}

group "staging" {
  targets = ["nginx-dev"]
}

variable "DOCKERHUB_USERNAME" {
  default = "krivolapovdev"
}

variable "IMAGE_TAG" {
  default = "latest"
}

variable "VITE_API_URL" {
  default = "localhost"
}


target "config-server" {
  context    = "./backend"
  dockerfile = "./config-server/Dockerfile"
  tags = [
    "${DOCKERHUB_USERNAME}/config-server:${IMAGE_TAG}",
    "${DOCKERHUB_USERNAME}/config-server:latest"
  ]
}

target "eureka-server" {
  context    = "./backend"
  dockerfile = "./eureka-server/Dockerfile"
  tags = [
    "${DOCKERHUB_USERNAME}/eureka-server:${IMAGE_TAG}",
    "${DOCKERHUB_USERNAME}/eureka-server:latest"
  ]
}

target "auth-service" {
  context    = "./backend"
  dockerfile = "./auth-service/Dockerfile"
  tags = [
    "${DOCKERHUB_USERNAME}/auth-service:${IMAGE_TAG}",
    "${DOCKERHUB_USERNAME}/auth-service:latest"
  ]
}

target "notification-service" {
  context    = "./backend"
  dockerfile = "./notification-service/Dockerfile"
  tags = [
    "${DOCKERHUB_USERNAME}/notification-service:${IMAGE_TAG}",
    "${DOCKERHUB_USERNAME}/notification-service:latest"
  ]
}

target "quiz-service" {
  context    = "./backend"
  dockerfile = "./quiz-service/Dockerfile"
  tags = [
    "${DOCKERHUB_USERNAME}/quiz-service:${IMAGE_TAG}",
    "${DOCKERHUB_USERNAME}/quiz-service:latest"
  ]
}

target "user-service" {
  context    = "./backend"
  dockerfile = "./user-service/Dockerfile"
  tags = [
    "${DOCKERHUB_USERNAME}/user-service:${IMAGE_TAG}",
    "${DOCKERHUB_USERNAME}/user-service:latest"
  ]
}

target "api-gateway" {
  context    = "./backend"
  dockerfile = "./api-gateway/Dockerfile"
  tags = [
    "${DOCKERHUB_USERNAME}/api-gateway:${IMAGE_TAG}",
    "${DOCKERHUB_USERNAME}/api-gateway:latest"
  ]
}

target "nginx" {
  context = "./frontend"
  dockerfile = "./nginx/Dockerfile"
  tags = [
    "${DOCKERHUB_USERNAME}/nginx:${IMAGE_TAG}",
    "${DOCKERHUB_USERNAME}/nginx:latest"
  ]
  args = {
    VITE_API_URL = "${VITE_API_URL}"
  }
}

target "nginx-dev" {
  context    = "./frontend"
  dockerfile = "./nginx/Dockerfile-dev"
  tags = [
    "${DOCKERHUB_USERNAME}/nginx:${IMAGE_TAG}",
    "${DOCKERHUB_USERNAME}/nginx:staging",
  ]
  args = {
    VITE_API_URL = "http://localhost:8765"
  }
}

target "prometheus" {
  context = "./infrastructure/docker/prometheus"
  tags = [
    "${DOCKERHUB_USERNAME}/prometheus:${IMAGE_TAG}",
    "${DOCKERHUB_USERNAME}/prometheus:latest"
  ]
}

target "loki" {
  context = "./infrastructure/docker/loki"
  tags = [
    "${DOCKERHUB_USERNAME}/loki:${IMAGE_TAG}",
    "${DOCKERHUB_USERNAME}/loki:latest"
  ]
}

target "grafana" {
  context = "./infrastructure/docker/grafana"
  tags = [
    "${DOCKERHUB_USERNAME}/grafana:${IMAGE_TAG}",
    "${DOCKERHUB_USERNAME}/grafana:latest"
  ]
}
