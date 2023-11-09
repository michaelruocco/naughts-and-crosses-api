package uk.co.mruoc.nac.repository.postgres.converter;

import uk.co.mruoc.nac.entities.Coordinates;
import uk.co.mruoc.nac.repository.postgres.dto.DbCoordinates;

public class DbCoordinatesConverter {

    public DbCoordinates toDbCoordinates(Coordinates coordinates) {
        return DbCoordinates.builder()
                .x(coordinates.getX())
                .y(coordinates.getY())
                .build();
    }

    public Coordinates toCoordinates(DbCoordinates dbCoordinates) {
        return Coordinates.builder()
                .x(dbCoordinates.getX())
                .y(dbCoordinates.getY())
                .build();
    }
}
