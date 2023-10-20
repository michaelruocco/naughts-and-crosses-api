package uk.co.mruoc.nac.app.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Coordinates {

    private final long x;
    private final long y;

    @JsonIgnore
    public String getKey() {
        return String.format("%d-%d", x, y);
    }
}
