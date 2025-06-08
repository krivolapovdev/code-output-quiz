up:
	docker compose -f ./infrastructure/docker/docker-compose.yml --profile prod up --build

up-dev:
	docker compose -f ./infrastructure/docker/docker-compose.yml --profile dev up --build

down:
	docker compose -f ./infrastructure/docker/docker-compose.yml down
