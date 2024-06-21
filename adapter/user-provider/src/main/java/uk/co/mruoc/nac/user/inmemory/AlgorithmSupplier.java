package uk.co.mruoc.nac.user.inmemory;

import com.auth0.jwt.algorithms.Algorithm;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;

@RequiredArgsConstructor
public class AlgorithmSupplier implements Supplier<Algorithm> {

  private final String publicKeyPath;
  private final String privateKeyPath;
  private final KeyFactory keyFactory;

  public AlgorithmSupplier() {
    this("public-key.pem", "private-key.pem");
  }

  public AlgorithmSupplier(String publicKeyPath, String privateKeyPath) {
    this(publicKeyPath, privateKeyPath, buildRsaKeyFactory());
  }

  @Override
  public Algorithm get() {
    return Algorithm.RSA256(getPublicKey(), getPrivateKey());
  }

  public RSAPublicKey getPublicKey() {
    try {
      byte[] bytes = readAllBytes(publicKeyPath);
      return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(bytes));
    } catch (InvalidKeySpecException e) {
      throw new KeyLoaderException(publicKeyPath, e);
    }
  }

  public RSAPrivateKey getPrivateKey() {
    try {
      byte[] bytes = readAllBytes(privateKeyPath);
      return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(bytes));
    } catch (InvalidKeySpecException e) {
      throw new KeyLoaderException(privateKeyPath, e);
    }
  }

  private byte[] readAllBytes(String path) {
    try (InputStream stream =
        Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(path))) {
      String pem =
          new String(IOUtils.toByteArray(stream), Charset.defaultCharset())
              .replace("-----BEGIN PUBLIC KEY-----", "")
              .replace("-----END PUBLIC KEY-----", "")
              .replace("-----BEGIN PRIVATE KEY-----", "")
              .replace("-----END PRIVATE KEY-----", "")
              .replaceAll(System.lineSeparator(), "");
      return Base64.getDecoder().decode(pem);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static KeyFactory buildRsaKeyFactory() {
    try {
      return KeyFactory.getInstance("RSA");
    } catch (NoSuchAlgorithmException e) {
      throw new KeyLoaderException(e);
    }
  }
}
