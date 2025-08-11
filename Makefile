staging:
	docker buildx bake -f docker-bake.hcl default staging --load
	docker compose -f docker-compose-staging.yml up --force-recreate

dev:
	docker buildx bake prometheus loki grafana -f docker-bake.hcl --load
	docker compose -f docker-compose-dev.yml up --force-recreate

down:
	docker compose down --remove-orphans
