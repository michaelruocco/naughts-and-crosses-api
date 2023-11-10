# Naughts and Crosses API

[![Build](https://github.com/michaelruocco/naughts-and-crosses-api/workflows/pipeline/badge.svg)](https://github.com/michaelruocco/naughts-and-crosses-api/actions)
[![codecov](https://codecov.io/gh/michaelruocco/naughts-and-crosses-api/graph/badge.svg?token=9yBPb1pvXr)](https://codecov.io/gh/michaelruocco/naughts-and-crosses-api)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/01a56ca6bd194fac92c786d0aed09c8c)](https://app.codacy.com/gh/michaelruocco/naughts-and-crosses-api/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/01a56ca6bd194fac92c786d0aed09c8c)](https://app.codacy.com/gh/michaelruocco/naughts-and-crosses-api/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_naughts-and-crosses-api&metric=alert_status)](https://sonarcloud.io/dashboard?id=michaelruocco_naughts-and-crosses-api)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_naughts-and-crosses-api&metric=sqale_index)](https://sonarcloud.io/dashboard?id=michaelruocco_naughts-and-crosses-api)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_naughts-and-crosses-api&metric=coverage)](https://sonarcloud.io/dashboard?id=michaelruocco_naughts-and-crosses-api)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_naughts-and-crosses-api&metric=ncloc)](https://sonarcloud.io/dashboard?id=michaelruocco_naughts-and-crosses-api)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.michaelruocco/naughts-and-crosses-api.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.michaelruocco%22%20AND%20a:%22naughts-and-crosses-api%22)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Overview

This repo contains the backend API for a simple naughts and crosses (or tic-tac-toe) game.
The accompanying UI that uses this API can be found [here](https://github.com/michaelruocco/naughts-and-crosses-ui)

## Useful Commands

```gradle
// cleans build directories
// checks dependency versions
// checks for gradle issues
// formats code
// builds code
// runs unit tests
// runs integration test
./gradlew clean dependencyUpdates lintGradle spotlessApply build integrationTest
```

### Running the API locally with an in memory repository

To start up the API using an in memory repository so no database is required, 
and on the default port 8080, you can run:

```gradle
./gradlew bootRun \
    -Dcors.allowed.origins=http://localhost:3001 \
    -Din.memory.repository.enabled=true
```

### Running the API locally with a postgres database repository

To run the API using the postgres database, first you will need to start
an instance of postgres by running:

```bash
docker-compose up -d
```

This will start an instance of postgres running on port 5433, then to start
the app connecting to postgres and running on a port other than the default 8080
you can run:

```gradle
./gradlew bootRun \
    -Dserver.port=3002 \
    -Dcors.allowed.origins=http://localhost:3001 \
    -Ddatabase.url="jdbc:postgresql://localhost:5433/naughts-and-crosses-api" \
    -Ddatabase.username=postgres \
    -Ddatabase.password=postgres \
    -Ddatabase.driver=org.postgresql.Driver
```

### Running the API as a docker container with a postgres database repository

To build the docker image you can run the following commands:

```bash
./gradlew clean bootJar
docker build -t naughts-and-crosses-api .
```

Then to run the API linked up to postgres with both running as docker containers
you can run:

```bash
docker-compose --profile docker-api up -d
```

### Creating a game

Once the API is running locally, to generate a game you can run:

```bash
curl -X POST http://localhost:3002/v1/games
```

To take a turn you can run:

```bash
curl -X POST http://localhost:3002/v1/games/{game-id}/turns -H "Content-Type: application/json" -d '{"coordinates":{"x":1,"y":1},"token":"X"}'  
```

To get all created games you can do either:

```bash
curl http://localhost:3002/v1/games
```

To get a full representation of all games including board state and all players.
Or if you want to get a minimal representation of all games that just includes
id and game status then you can do:

```bash
curl "http://localhost:3002/v1/games?minimal=true"
```