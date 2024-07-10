package uk.co.mruoc.nac.virus.clamav;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import uk.co.mruoc.nac.entities.VirusScanException;
import uk.co.mruoc.nac.usecases.VirusScanner;

@RequiredArgsConstructor
public class ClamAvVirusScanner implements VirusScanner {

  private final ClamAvVirusScannerResultValidator resultValidator;
  private final ClamAvVirusScannerConfig config;

  public ClamAvVirusScanner(ClamAvVirusScannerConfig config) {
    this(new ClamAvVirusScannerResultValidator(), config);
  }

  public void scan(InputStream fileStream) {
    try (Socket socket = new Socket()) {
      socket.connect(buildSocketAddress(), config.getConnectTimeoutMillis());
      socket.setSoTimeout(config.getReadTimeoutMillis());
      try (OutputStream outStream = new BufferedOutputStream(socket.getOutputStream())) {
        outStream.write("zINSTREAM\0".getBytes(UTF_8));
        outStream.flush();
        try (InputStream inStream = socket.getInputStream()) {
          validate(fileStream, outStream, inStream);
        }
      }
    } catch (IOException e) {
      throw new VirusScanException(e);
    }
  }

  private SocketAddress buildSocketAddress() {
    return new InetSocketAddress(config.getHost(), config.getPort());
  }

  private void validate(InputStream fileStream, OutputStream outStream, InputStream inStream)
      throws IOException {
    byte[] buffer = new byte[2048];
    int read = fileStream.read(buffer);
    while (read >= 0) {
      byte[] chunkSize = ByteBuffer.allocate(4).putInt(read).array();
      outStream.write(chunkSize);
      outStream.write(buffer, 0, read);
      if (inStream.available() > 0) {
        throwReplyError(inStream);
      }
      read = fileStream.read(buffer);
    }
    outStream.write(new byte[] {0, 0, 0, 0});
    outStream.flush();
    resultValidator.validate(inStream);
  }

  private void throwReplyError(InputStream inStream) throws IOException {
    String reply = new String(IOUtils.toByteArray(inStream), UTF_8);
    throw new VirusScanException(String.format("reply %s ", reply));
  }
}
