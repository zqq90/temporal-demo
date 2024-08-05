# Temporal Demo

## Introduction

## Development

### Prerequisites

+ Docker & Docker Compose
+ JDK 21

### Build

```bash
./gradlew clean build
```

### Start a Kafka mock server

```bash
kcat -M 3
```

## Startup

### Start Temporal & Kafka

```bash
docker-compose -f startup/docker-compose.yaml up
```

### Init Temporal with `tctl`

+ Alias for tctl

```bash
alias docker_exec="docker exec -it temporal_temporal-admin-tools_1"
alias tctl="docker_exec tctl --namespace febit-dev"

+ [First Time] Init Namespace

```bash
docker_exec tctl --namespace febit-dev \
  namespace  register \
  --description "Febit develop env" \
  febit-dev
  
docker_exec temporal operator search-attribute create \
  --namespace="febit-dev" \
  --name="Biz" \
  --type="Text"
  
docker_exec temporal operator search-attribute create \
  --namespace="febit-dev" \
  --name="BizGroup" \
  --type="Text"
  
docker_exec temporal operator search-attribute create \
  --namespace="febit-dev" \
  --name="BizBatchNumber" \
  --type="Int"
```

+ List Namespaces

```bash
tctl namespace list
```

+ Start Cron Workflow Demo

```bash

curl -X 'POST' \
  'http://localhost:8082/api/v1/workflows/schedule/start' \
  -H 'accept: application/json' \
  -d ''
```

+ Stop Cron Workflow Demo

```bash
curl -X 'POST' \
  'http://localhost:8082/api/v1/workflows/schedule/stop' \
  -H 'accept: application/json' \
  -d ''
```

+ Actuator - Prometheus

http://localhost:9999/actuator/prometheus
