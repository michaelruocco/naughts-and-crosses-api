package uk.co.mruoc.nac.app.api;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiPlayer {

    private final UUID id;
    private final String name;
    private final char token;
}
