up:
	docker buildx bake -f docker-bake.hcl --load
	docker compose -f docker-compose.yml up

dev:
	docker buildx bake prometheus loki grafana -f docker-bake.hcl --load
	docker compose -f docker-compose-dev.yml up

down:
	docker compose down
