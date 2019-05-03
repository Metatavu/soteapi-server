package fi.metatavu.soteapi.bisnode;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

/**
 * Helper class for creating jwt tokens
 * 
 * @author Heikki Kurhinen
 */
public class JwtUtil {

  private static final long NOT_BEFORE_SLACK_SECONDS = 5l;

  private static final long EXPIRE_AFTER_SECONDS = 60l;

  /**
   * Creates jwt token
   * 
   * @param secret secret key
   * @param issuer issuer
   * @param audience audience
   * 
   * @return created jwt token string
   */
  public static String createToken(String secret, String issuer, String audience) {

    OffsetDateTime iat = OffsetDateTime.now();
    OffsetDateTime nbf = iat.minusSeconds(NOT_BEFORE_SLACK_SECONDS);
    OffsetDateTime exp = iat.plusSeconds(EXPIRE_AFTER_SECONDS);

    Algorithm algorithm = Algorithm.HMAC256(secret);
    
    return JWT.create()
      .withIssuer(issuer)
      .withAudience(audience)
      .withNotBefore(Date.from(nbf.toInstant()))
      .withIssuedAt(Date.from(iat.toInstant()))
      .withExpiresAt(Date.from(exp.toInstant()))
      .withJWTId(UUID.randomUUID().toString())
      .sign(algorithm);

  }

}