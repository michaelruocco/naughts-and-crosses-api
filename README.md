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

## Java Version

This repo requires the use of Java 20. If you also work with other repos that
required different versions of Java, [SDKMan](https://sdkman.io/) is a useful
tool for enabling you to switch between version of Java (or other  Java SDK
based languages) easily when required.

The local build for this repo has been tested using Java 20.0.2 Temurin
version which can be set up by running

```bash
sdk env install
```

which will set the java version based on the config defined in the
`.sdkmanrc` file at the root of this project. If you also update the file
at `~/.sdkman/etc/config` to contain `sdkman_auto_env=true` then each time
you navigate into the root project directory in a terminal sdk man will
auto set the java version for you.

Alternatively if you would rather manually configure your sdk versions
using sdk man you can use the following commands

```bash
sdk install java 20.0.2-tem
sdk java use 20.0.2-tem   
```

Note - the second command is only required if you do not choose to make
the Java 20 Temurin version your default JVM when installing.

Once this is set up it will give the following output when the
`java -version` command is run:

```bash
openjdk version "20.0.2" 2023-07-18
OpenJDK Runtime Environment Temurin-20.0.2+9 (build 20.0.2+9)
OpenJDK 64-Bit Server VM Temurin-20.0.2+9 (build 20.0.2+9, mixed mode, sharing)
```

## Useful Commands

```gradle
// cleans build directories
// checks dependency versions
// checks for gradle issues
// formats code
// builds code
// runs unit tests
// runs integration test
./gradlew clean \
    dependencyUpdates \
    lintGradle \
    spotlessApply \
    build \
    integrationTest
```

### Running the API locally with an in memory repository

To start up the API using an in memory repository so no database is required, 
you can run:

```gradle
./gradlew bootRun \
    -Dserver.port=3002 \
    -Dcors.allowed.origins=http://localhost:3001 \
    -Drepository.in.memory.enabled=true \
    -Dbroker.in.memory.enabled=true \
    -Dauth.security.enabled=false
```

### Running the API locally with a postgres database repository, kafka and keycloak

Note - for keycloak to work correctly you will also need to update your hosts file,
on a mac this can be found at `/etc/hosts` and you will need to map the domain name
`keycloak` to the local machine by adding the following line to the file:

```
127.0.0.1	keycloak
```

To run the API using the postgres database, kafka and keycloak, you will need to start
an instance of each of those systems running in docker by running

```bash
docker-compose up -d
```

This will start an instance of postgres running on port 5433, kafka running on 9094
and keycloak running on 4012.

To start the app running on a port 3002 and connecting to postgres, kafka and keycloak
you can run:

```gradle
./gradlew bootRun \
    -Dserver.port=3002 \
    -Dcors.allowed.origins=http://localhost:3001 \
    -Ddatabase.url="jdbc:postgresql://localhost:5433/naughts-and-crosses-api" \
    -Ddatabase.username=postgres \
    -Ddatabase.password=postgres \
    -Ddatabase.driver=org.postgresql.Driver \
    -Dbroker.host=127.0.0.1 \
    -Dbroker.port=61613 \
    -Dbroker.client.login=artemis \
    -Dbroker.client.passcode=artemis \
    -Dbroker.system.login=artemis \
    -Dbroker.system.passcode=artemis \
    -Dauth.issuer.url=http://keycloak:4021/realms/naughts-and-crosses-local \
    -Dkeycloak.admin.client.id=naughts-and-crosses-api \
    -Dkeycloak.admin.client.secret=naughts-and-crosses-api-secret
```

### Running the API as a docker container with a postgres database repository, and kafka

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

### Generating a bearer token from Keycloak

If you are running the API with oauth security enabled you will need to generate a
bearer token that you can provide when calling the API. To do this you can run the
following command:

```bash
curl "http://keycloak:4021/realms/naughts-and-crosses-local/protocol/openid-connect/token" \
        -d "client_id=naughts-and-crosses-api" \
        -d "client_secret=naughts-and-crosses-api-secret" \
        -d "grant_type=client_credentials"
```

### Getting users

```bash
curl http://localhost:3002/v1/users
```

or with a bearer token:

```bash
curl -H 'Authorization:Bearer <token-value>' http://localhost:3002/v1/users
```

### Creating a game

Once the API is running locally, to generate a game you can run, note - for this command
and any of the subsequent ones listed, if authentication is enabled on the API then the
bearer token needs to be supplied in an authorization header on the request

```bash
curl -H "Content-Type: application/json" \
  -d '{"requestedPlayers":[{"userId":"707d9fa6-13dd-4985-93aa-a28f01e89a6b","token":"X"},{"userId":"dadfde25-9924-4982-802d-dfd0bce2218d","token":"O"}]}' \
  -X POST http://localhost:3002/v1/games
```

or with a bearer token:

```bash
curl -H 'Authorization:Bearer <token-value>' \
  -X POST http://localhost:3002/v1/games
```

To take a turn you can run:

```bash
curl -H "Content-Type: application/json" \
  -d '{"coordinates":{"x":1,"y":1},"token":"X"}' \
  -X POST http://localhost:3002/v1/games/{game-id}/turns  
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
