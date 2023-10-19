package uk.co.mruoc.nac.app.api;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiBoard {

    private final long size;
    private final Collection<ApiLocation> locations;
}
