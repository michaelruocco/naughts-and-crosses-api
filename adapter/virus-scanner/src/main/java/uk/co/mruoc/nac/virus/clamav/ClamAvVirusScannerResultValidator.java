package uk.co.mruoc.nac.virus.clamav;

import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import uk.co.mruoc.nac.entities.VirusScanException;

@RequiredArgsConstructor
public class ClamAvVirusScannerResultValidator {

  private static final String FOUND = "FOUND";

  public void validate(InputStream stream) throws IOException {
    validate(toString(stream));
  }

  private static void validate(String result) {
    if ("stream: OK".equals(result)) {
      return;
    }
    if (StringUtils.isEmpty(result)) {
      throw new VirusScanException("empty result");
    }
    if (result.endsWith(FOUND)) {
      throw new VirusScanException(String.format("failed with signature %s", toSignature(result)));
    }
    throw new VirusScanException("error");
  }

  private static String toString(InputStream stream) throws IOException {
    return new String(IOUtils.toByteArray(stream)).trim();
  }

  private static String toSignature(String s) {
    int startIndex = "stream:".length();
    int endIndex = s.lastIndexOf(FOUND) - 1;
    return s.substring(startIndex, endIndex).trim();
  }
}
