up:
	docker compose -f docker-compose.yml up --build

up-dev:
	docker compose -f docker-compose-dev.yml up --build

down:
	docker compose -f docker-compose.yml down
