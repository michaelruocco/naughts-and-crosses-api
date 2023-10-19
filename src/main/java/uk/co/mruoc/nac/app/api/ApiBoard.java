package uk.co.mruoc.nac.app.api;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder(toBuilder = true)
@Data
public class ApiBoard {

    private final long size;
    private final Collection<ApiLocation> locations;

    public Optional<ApiLocation> getLocationIfAvailable(ApiCoordinates coordinates) {
        return findLocation(coordinates).filter(ApiLocation::isAvailable);
    }

    private Optional<ApiLocation> findLocation(ApiCoordinates coordinates) {
        return locations.stream()
                .filter(location -> location.isAt(coordinates))
                .findFirst();
    }

    public ApiBoard update(ApiLocation updatedLocation) {
        Collection<ApiLocation> updatedLocations = locations.stream()
                .map(location -> replaceIfMatches(location, updatedLocation))
                .toList();
        return toBuilder().locations(updatedLocations).build();
    }

    private static ApiLocation replaceIfMatches(ApiLocation location, ApiLocation updatedLocation) {
        if (location.isAt(updatedLocation)) {
            return updatedLocation;
        }
        return location;
    }
}
