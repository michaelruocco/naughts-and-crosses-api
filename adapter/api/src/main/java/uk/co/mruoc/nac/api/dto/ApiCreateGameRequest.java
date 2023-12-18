package uk.co.mruoc.nac.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiCreateGameRequest {

    private final Collection<ApiPlayer> players;
}
