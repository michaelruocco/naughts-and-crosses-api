package uk.co.mruoc.nac.repository.postgres.dto;

import java.util.Collection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class DbPlayers {

    private final Collection<DbPlayer> values;
    private final int currentIndex;
}
