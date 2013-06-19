package net.kiminotes.commons.util;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

/**
 * @author <a href="mailto:gang.lvg@alibaba-inc.com">kimi</a>
 */
public final class SecurityUtil {

    public static RSAPublicKey generatePublicKeyFromPrivateKey(RSAPrivateKey privateKey)
        throws Exception {
        if (privateKey == null) {
            return null;
        }

        BigInteger n, e;
        try {
            n = (BigInteger)get(privateKey, "n");
            e = (BigInteger)get(privateKey, "e");
        } catch (Throwable ex) {
            // ignore
            return null;
        }

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
        KeyFactory keyFactory = KeyFactory.getInstance(privateKey.getAlgorithm());
        return (RSAPublicKey)keyFactory.generatePublic(keySpec);
    }

    private static Object get(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    private SecurityUtil() {}

}
