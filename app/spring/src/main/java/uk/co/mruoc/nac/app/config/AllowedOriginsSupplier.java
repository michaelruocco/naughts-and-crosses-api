package uk.co.mruoc.nac.app.config;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class AllowedOriginsSupplier implements Supplier<String[]> {

    private final String[] allowedOrigins;

    public AllowedOriginsSupplier(String allowedOrigins) {
        this(toArray(allowedOrigins));
    }

    @Override
    public String[] get() {
        return allowedOrigins;
    }

    private static String[] toArray(String input) {
        if (StringUtils.isEmpty(input)) {
            return new String[0];
        }
        return input.split(",");
    }
}
