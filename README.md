# Naughts and Crosses API

[![Build](https://github.com/michaelruocco/naughts-and-crosses-api/workflows/pipeline/badge.svg)](https://github.com/michaelruocco/naughts-and-crosses-api/actions)
[![codecov](https://codecov.io/gh/michaelruocco/naughts-and-crosses-api/branch/master/graph/badge.svg?token=FWDNP534O7)](https://codecov.io/gh/michaelruocco/naughts-and-crosses-api)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/272889cf707b4dcb90bf451392530794)](https://www.codacy.com/gh/michaelruocco/naughts-and-crosses-api/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=michaelruocco/naughts-and-crosses-api&amp;utm_campaign=Badge_Grade)
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

### Running the API locally

To start up the API you can run:

```gradle
./gradlew bootRun
```

or to run the API on a port other than the default 8080:

```gradle
./gradlew bootRun --args='--server.port=3002 --cors.allowed.origins=http://localhost:3001'
```

### Creating a game

Once the API is running locally, to generate a game you can run:

```bash
curl -X POST http://localhost:8080/v1/games
```

To take a turn you can run:

```bash
curl -X POST http://localhost:8080/v1/games/{game-id}/turns -H "Content-Type: application/json" -d '{"coordinates":{"x":1,"y":1},"token":"X"}'  
```

To get all created games you can do either:

```bash
curl http://localhost:8080/v1/games
```

To get a full representation of all games including board state and all players.
Or if you want to get a minimal representation of all games that just includes
id and game status then you can do:

```bash
curl "http://localhost:8080/v1/games?minimal=true"
```

### Building docker image

To build the docker image you can run the following commands:

```bash
./gradlew clean bootJar
docker build -t naughts-and-crosses-api .
```
