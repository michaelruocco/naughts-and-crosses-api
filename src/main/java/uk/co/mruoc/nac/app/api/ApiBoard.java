package uk.co.mruoc.nac.app.api;

import java.util.Collection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiBoard {

    private final long size;
    private final Collection<ApiLocation> locations;
}
