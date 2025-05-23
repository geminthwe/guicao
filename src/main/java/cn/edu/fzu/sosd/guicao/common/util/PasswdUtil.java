package cn.edu.fzu.sosd.guicao.common.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswdUtil {
    private static final String AES = "AES"; // 加密算法
    private static final String CHARSET = "UTF-8"; // 字符集
    private static final String SECRET_KEY = "app@guicao"; // 密钥（需要妥善保管）
    private static final String ALGORITHM = "SHA1PRNG"; // 算法

    /**
     * 加密
     *
     * @param data 明文密码
     * @return 加密后的密码
     */
    public static String encode(String data) {
        if (data == null) {
            return null;
        }
        try {
            // 生成加密密钥
            SecretKeySpec key = generateKey();
            // 创建加密器
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 加密数据
            byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            // 使用Base64编码返回
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }

    /**
     * 解密
     *
     * @param encryptedData 加密后的密码
     * @return 解密后的明文密码
     */
    public static String decode(String encryptedData) {
        try {
            // 生成解密密钥
            SecretKeySpec key = generateKey();
            // 创建解密器
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, key);
            // 解密数据
            byte[] decodedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decodedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("解密失败", e);
        }
    }

    /**
     * 生成密钥
     *
     * @return SecretKeySpec 对象
     */
    private static SecretKeySpec generateKey() throws Exception {
        // 使用指定密钥生成器
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        SecureRandom secureRandom = SecureRandom.getInstance(ALGORITHM);
        secureRandom.setSeed(PasswdUtil.SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        keyGenerator.init(128, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), AES);
    }
}
