package uk.co.mruoc.nac.app.api.converter;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.app.api.dto.ApiLocation;
import uk.co.mruoc.nac.app.domain.entities.Location;

@RequiredArgsConstructor
public class ApiLocationConverter {

    private final ApiCoordinatesConverter coordinatesConverter;

    public ApiLocationConverter() {
        this(new ApiCoordinatesConverter());
    }

    public Collection<ApiLocation> toApiLocations(Collection<Location> locations) {
        return locations.stream().map(this::toApiLocation).toList();
    }

    public ApiLocation toApiLocation(Location location) {
        return ApiLocation.builder()
                .coordinates(coordinatesConverter.toApiCoordinates(location.getCoordinates()))
                .token(location.getToken())
                .build();
    }
}
