package uk.co.mruoc.nac.virus.claimav;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import uk.co.mruoc.nac.entities.VirusScanException;
import uk.co.mruoc.nac.virus.clamav.ClamAvVirusScannerResultValidator;

class ClamAvVirusScannerResultValidatorTest {

  private final ClamAvVirusScannerResultValidator validator =
      new ClamAvVirusScannerResultValidator();

  @Test
  void shouldDoNothingIfResultIsValid() throws IOException {
    try (InputStream stream = toInputStream("stream: OK")) {
      ThrowingCallable call = () -> validator.validate(stream);

      assertThatCode(call).doesNotThrowAnyException();
    }
  }

  @Test
  void shouldThrowExceptionIfStreamIsEmpty() throws IOException {
    try (InputStream stream = toInputStream("")) {
      Throwable error = catchThrowable(() -> validator.validate(stream));

      assertThat(error).isInstanceOf(VirusScanException.class).hasMessage("empty result");
    }
  }

  @Test
  void shouldThrowExceptionIfStreamContainsUnexpectedResult() throws IOException {
    try (InputStream stream = toInputStream("some other result")) {
      Throwable error = catchThrowable(() -> validator.validate(stream));

      assertThat(error).isInstanceOf(VirusScanException.class).hasMessage("error");
    }
  }

  @Test
  void shouldThrowExceptionIfStreamContainsVirusSignature() throws IOException {
    try (InputStream stream = toInputStream("stream: test-virus FOUND")) {
      Throwable error = catchThrowable(() -> validator.validate(stream));

      assertThat(error)
          .isInstanceOf(VirusScanException.class)
          .hasMessage("failed with signature test-virus");
    }
  }

  private static InputStream toInputStream(String value) {
    return IOUtils.toInputStream(value, UTF_8);
  }
}
