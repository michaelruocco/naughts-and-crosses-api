package uk.co.mruoc.nac.postgres;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

@Slf4j
public class PostgresExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private boolean started = false;

    private static final TestPostgresContainer DB_CONTAINER = new TestPostgresContainer();

    @Override
    public void beforeAll(ExtensionContext context) {
        if (!started) {
            log.info("starting postgres extension");
            started = true;

            DB_CONTAINER.start();
        }
    }

    @Override
    public void close() {
        DB_CONTAINER.close();
    }

    public static DataSource getDataSource() {
        return DB_CONTAINER.getDataSource();
    }
}
