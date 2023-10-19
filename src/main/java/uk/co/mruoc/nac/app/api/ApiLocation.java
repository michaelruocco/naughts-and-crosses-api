package uk.co.mruoc.nac.app.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.With;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiLocation {

    private static final char AVAILABLE = '-';

    private final ApiCoordinates coordinates;
    @With private final char token;

    public ApiLocation(long x, long y) {
        this(new ApiCoordinates(x, y), AVAILABLE);
    }

    public boolean isAt(ApiLocation otherLocation) {
        return isAt(otherLocation.getCoordinates());
    }

    public boolean isAt(ApiCoordinates otherCoordinates) {
        return coordinates.getX() == otherCoordinates.getX() &&
                coordinates.getY() == otherCoordinates.getY();
    }

    @JsonIgnore
    public boolean isAvailable() {
        return token == AVAILABLE;
    }
}
