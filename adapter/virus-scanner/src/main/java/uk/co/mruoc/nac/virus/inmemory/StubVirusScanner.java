package uk.co.mruoc.nac.virus.inmemory;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import uk.co.mruoc.nac.entities.VirusScanException;
import uk.co.mruoc.nac.usecases.VirusScanner;

public class StubVirusScanner implements VirusScanner {

  public void scan(InputStream fileStream) {
    String content = toString(fileStream);
    if (content.contains("EICAR-STANDARD-ANTIVIRUS-TEST-FILE")) {
      throw new VirusScanException("failed with signature Win.Test.EICAR_HDB-1");
    }
  }

  private static String toString(InputStream stream) {
    try {
      return new String(IOUtils.toByteArray(stream)).trim();
    } catch (IOException e) {
      throw new VirusScanException(e);
    }
  }
}
