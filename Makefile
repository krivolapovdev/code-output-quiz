up:
	docker compose -f docker-compose.yml --profile prod up --build

up-dev:
	docker compose -f docker-compose.yml --profile dev up --build

down:
	docker compose -f docker-compose.yml down
