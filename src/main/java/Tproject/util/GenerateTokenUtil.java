package Tproject.util;
import java.security.SecureRandom;
import java.util.Base64;

public class GenerateTokenUtil {
    private static final SecureRandom secureRandom = new SecureRandom(); // криптографический генератор
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    public static String generateRefreshToken() {
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
    public static String generateInviteToken() {
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

}
