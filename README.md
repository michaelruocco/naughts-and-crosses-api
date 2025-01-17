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

This repo contains the backend API for a simple naughts and crosses
(or tic-tac-toe) game. The accompanying UI that uses this API can be
found [here](https://github.com/michaelruocco/naughts-and-crosses-ui)

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
    -Dstub.user.provider.enabled=true \
    -Dstub.token.service.enabled=true \
    -Dstub.auth.code.client.enabled=true \
    -Dstub.jwt.parser.enabled=true \
    -Dauth.issuer.public.key=classpath:/public-key.pem
```

### Running the API locally with a postgres database repository, kafka and cognito

Note - for cognito to work correctly you will also need to update your hosts file,
on a mac this can be found at `/etc/hosts` and you will need to map the domain name
`cognito` to the local machine by adding the following line to the file:

```
127.0.0.1    cognito
```

To run the API using the postgres database, kafka and cognito, you will need to start
an instance of each of those systems running in docker by running

```bash
docker-compose up -d
```

This will start an instance of postgres running on port 5433, kafka running on 9094
and cognito running on 9229.

To start the app running on a port 3002 and connecting to postgres, kafka and cognito
you can run:

```gradle
./gradlew bootRun \
    -Dserver.port=3002 \
    -Dcors.allowed.origins=http://localhost:3001 \
    -Ddatabase.url="jdbc:postgresql://localhost:5433/naughts-and-crosses-api" \
    -Ddatabase.username=postgres \
    -Ddatabase.password=postgres \
    -Ddatabase.driver=org.postgresql.Driver \
    -Dbroker.ssl.enabled=false \
    -Dbroker.host=127.0.0.1 \
    -Dbroker.port=61613 \
    -Dbroker.client.login=artemis \
    -Dbroker.client.passcode=artemis \
    -Dbroker.system.login=artemis \
    -Dbroker.system.passcode=artemis \
    -Dauth.issuer.url=http://cognito:9229/local_4RsGXSAf \
    -Daws.cognito.userPoolId=local_4RsGXSAf \
    -Daws.cognito.userPoolClientId=6b0j5hb1u25z290vv502lfl1c \
    -Daws.cognito.regionName=eu-central-1 \
    -Daws.cognito.endpointOverride=http://localhost:9229 \
    -Daws.cognito.accessKeyId=abc \
    -Daws.cognito.secretAccessKey=123 \
    -Dclam.av.host=localhost \
    -Dclam.av.port=3310 \
    -Dclam.av.connect.timeout=2 \
    -Dclam.av.read.timeout=20 \
    -Dstub.auth.code.client.enabled=true
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

### Generating a bearer token from Cognito

If you are running the API with oauth security enabled you will need
to generate a bearer token that you can provide when calling the API.
To do this you can run the following command:

```bash
curl -X POST "http://localhost:3002/v1/tokens" \
  -H 'Content-Type:application/json' \
  -d '{"username":"user-1","password":"pwd1"}'
```

### Creating users

You can create users by uploading a csv file using the following command:

```bash
curl -X POST \
    -H 'Authorization:Bearer <token>' \
    -F "data=@./app/spring/src/testFixtures/resources/users/users.csv;type=text/csv" \
    -F "name=users.csv" \
    http://localhost:3002/v1/users
```

### Getting users

```bash
curl -H 'Authorization:Bearer <token>' http://localhost:3002/v1/users
```

### Creating a game

Once the API is running locally, to generate a game you can run, note - for
this command and any of the subsequent ones listed

```bash
curl -H "Content-Type: application/json" \
  -H 'Authorization:Bearer <token-value>' \
  -d '{"requestedPlayers":[{"username":"707d9fa6-13dd-4985-93aa-a28f01e89a6b","token":"X"},{"username":"dadfde25-9924-4982-802d-dfd0bce2218d","token":"O"}]}' \
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
  -H 'Authorization:Bearer <token-value>' \
  -d '{"coordinates":{"x":1,"y":1},"token":"X"}' \
  -X POST http://localhost:3002/v1/games/{game-id}/turns  
```

To get all created games you can do either:

```bash
curl -H 'Authorization:Bearer <token-value>' http://localhost:3002/v1/games
```

To get a full representation of all games including board state and all players.
Or if you want to get a minimal representation of all games that just includes
id and game status then you can do:

```bash
curl -H 'Authorization:Bearer <token-value>' http://localhost:3002/v1/games?minimal=true
```