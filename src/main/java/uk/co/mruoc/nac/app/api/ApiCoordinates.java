package uk.co.mruoc.nac.app.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiCoordinates {

    private final long x;
    private final long y;

    @JsonIgnore
    public String getKey() {
        return String.format("%d-%d", x, y);
    }
}
