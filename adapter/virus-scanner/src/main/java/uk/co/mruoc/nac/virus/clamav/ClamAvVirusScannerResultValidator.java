package uk.co.mruoc.nac.virus.clamav;

import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import uk.co.mruoc.nac.entities.VirusScanException;

@RequiredArgsConstructor
public class ClamAvVirusScannerResultValidator {

  private static final String FOUND = "FOUND";

  public void validate(InputStream stream) {
    validate(toString(stream));
  }

  private static void validate(String result) {
    if ("stream: OK".equals(result)) {
      return;
    }
    if (result == null || result.isEmpty()) {
      throw new VirusScanException("empty result");
    }
    if (result.endsWith(FOUND)) {
      throw new VirusScanException(String.format("failed with signature %s", toSignature(result)));
    }
    throw new VirusScanException("error");
  }

  private static String toString(InputStream stream) {
    try {
      return new String(IOUtils.toByteArray(stream)).trim();
    } catch (IOException e) {
      throw new VirusScanException(e);
    }
  }

  private static String toSignature(String s) {
    int startIndex = "stream:".length();
    int endIndex = s.lastIndexOf(FOUND) - 1;
    return s.substring(startIndex, endIndex).trim();
  }
}
