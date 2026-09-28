package utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JwtUtil {
    private static final String SECRET_KEY = "MiSuperSecretoSeguroParaAgroConnect"; // Debería estar en .env idealmente

    public static String generarToken(String email) {
        try {
            // 1. Header
            String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
            String encodedHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes(StandardCharsets.UTF_8));

            // 2. Payload (Incluye expiración de 1 hora)
            long exp = (System.currentTimeMillis() / 1000) + 3600; 
            String payload = "{\"email\":\"" + email + "\",\"exp\":" + exp + "}";
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8));

            // 3. Signature
            String dataToSign = encodedHeader + "." + encodedPayload;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] signatureBytes = mac.doFinal(dataToSign.getBytes(StandardCharsets.UTF_8));
            String signature = Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);

            return encodedHeader + "." + encodedPayload + "." + signature;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean validarToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;

            String header = parts[0];
            String payload = parts[1];
            String providedSignature = parts[2];

            // Re-calcular la firma para ver si coincide
            String dataToSign = header + "." + payload;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] signatureBytes = mac.doFinal(dataToSign.getBytes(StandardCharsets.UTF_8));
            String expectedSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);

            if (!expectedSignature.equals(providedSignature)) {
                return false; // Token fue modificado o es falso
            }

            // Validar expiración
            String decodedPayload = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
            if (decodedPayload.contains("\"exp\":")) {
                int expIndex = decodedPayload.indexOf("\"exp\":") + 6;
                int expEnd = decodedPayload.indexOf("}", expIndex);
                if (expEnd == -1) expEnd = decodedPayload.indexOf(",", expIndex);
                long exp = Long.parseLong(decodedPayload.substring(expIndex, expEnd));
                long now = System.currentTimeMillis() / 1000;
                if (now > exp) return false; // Token expirado
            }

            return true; // Es totalmente válido
        } catch (Exception e) {
            return false;
        }
    }
}
