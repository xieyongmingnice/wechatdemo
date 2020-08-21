package com.blue.wechatdemo.util;


import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.springframework.util.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * RAS加密demo
 */
public class RSAEncryptionDemo {
    private static final String RSA_PUBLIC_KEY = "" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm+8HoQa/yBmtUK3HqLi9HTt0CfgmcmBf\n" +
            "pYxLUv3ehZm6x3ika/TF1tOA0vdsnFSD46eLJHFkuk0AQ6P++J94HTA/6QA4AnUWUzj5YpBszD7F\n" +
            "mlqhm4OatQB0JOkKf4gOqya3zmB5I4KskS7H2HcH37zJ2e8Ay6kKIgyDo4tOA9lGRjfa3equ9e3F\n" +
            "Wa3cbN+s6sGaKBFMDHvPgvbPNAMzC51Y/5Zx/GqHwvu+j749p9RBz89kc8AhZvWZm/Dwb1M1q7Vi\n" +
            "gfMfhTrW2krr1JoK7KfkMtiTDXv3SWQI8qWtw35/NJveiJBELKawzvXxDPKHsOXpVNd5u11VHaW4\n" +
            "DrpWPwIDAQAB";

    public static final int KEY_SIZE = 2048;


    // 加密方法demo
    public static void main(String[] args) {
        String dataStr = "{" +
                "\"hospitalId\":\"37818012-5c55-4aa1-99a8-fa5da2fa8358\"," +
                "    \"prescriptionCode\":\"M081201\"," +
                "    \"prescriptionDate\":\"2020-08-12 14:47:36\"," +
                "\"dispenseDoctorId\":\"\"," +
                "\"patientBirthday\":\"1989-08-12\"," +
                "    \"patientName\":\"M081201\"," +
                "    \"patientNumber\":\"\"," +
                "    \"patientRegisterCode\":\"M081201\"," +
                "    \"patientSex\":\"女\"," +
                "    \"patientWeight\":\"56.0\"," +
                "    \"specialCrowd\":\"\"," +
                "    \"diagnoseList\":[" +
                "        \"急性肾衰恢复期\"," +
                "\"肾衰竭\"" +
                "    ]," +
                "    \"drugList\":[" +
                "        {" +
                "            \"drugFrequency\":\"q12h1\"," +
                "            \"drugHisId\":\"5349\"," +
                "            \"drugRoute\":\"口服\"," +
                "            \"quantity\":\"1.0\"," +
                "            \"quantityUnit\":\"kg\"," +
                "            \"singleDose\":\"6.0\"," +
                "            \"singleDoseUnit\":\"g\"" +
                "        }," +
                "        {" +
                "            \"drugFrequency\":\"q12h1\"," +
                "            \"drugHisId\":\"864\"," +
                "            \"drugRoute\":\"口服\"," +
                "            \"quantity\":\"1.0\"," +
                "            \"quantityUnit\":\"kg\"," +
                "            \"singleDose\":\"5.0\"," +
                "            \"singleDoseUnit\":\"ml\"" +
                "        }" +
                "]" +
                "}";
        String encryptedContent = RSAEncryptionDemo.encipher(dataStr.replace("\n","").
                replace("\r\n",""), RSAEncryptionDemo.RSA_PUBLIC_KEY);
        System.out.println("原文为："+dataStr.replace("\n","").replace("\r\n",""));
        System.out.println("加密后："+encryptedContent);
    }


    /**
     * 使用公钥加密
     *
     * @param content         待加密内容
     * @return 经过 base64 编码后的字符串
     */
    public static String encipher(String content) {
        return encipher(content, RSAEncryptionDemo.RSA_PUBLIC_KEY, KEY_SIZE / 8 - 11);
    }

    /**
     * 使用公钥加密
     *
     * @param content         待加密内容
     * @param publicKeyBase64 公钥 base64 编码
     * @return 经过 base64 编码后的字符串
     */
    public static String encipher(String content, String publicKeyBase64) {
        return encipher(content, publicKeyBase64, KEY_SIZE / 8 - 11);
    }

    /**
     * 获取公钥对象
     *
     * @param publicKeyBase64
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PublicKey getPublicKey(String publicKeyBase64)
            throws InvalidKeySpecException, NoSuchAlgorithmException, Base64DecodingException {

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicpkcs8KeySpec =
                new X509EncodedKeySpec(Base64.decode(publicKeyBase64));

        return keyFactory.generatePublic(publicpkcs8KeySpec);
    }

    /**
     * 使用公钥加密（分段加密）
     *
     * @param content         待加密内容
     * @param publicKeyBase64 公钥 base64 编码
     * @param segmentSize     分段大小,一般小于 keySize/8（段小于等于0时，将不使用分段加密）
     * @return 经过 base64 编码后的字符串
     */
    public static String encipher(String content, String publicKeyBase64, int segmentSize) {
        try {
            PublicKey publicKey = getPublicKey(publicKeyBase64);
            return encipher(content, publicKey, segmentSize);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分段加密
     *
     * @param ciphertext  密文
     * @param key         加密秘钥
     * @param segmentSize 分段大小，<=0 不分段
     * @return
     */
    public static String encipher(String ciphertext, Key key, int segmentSize) {
        try {
            // 用公钥加密
            byte[] srcBytes = ciphertext.getBytes();

            // Cipher负责完成加密或解密工作，基于RSA
            Cipher cipher = Cipher.getInstance("RSA");
            // 根据公钥，对Cipher对象进行初始化
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] resultBytes = null;

            if (segmentSize > 0)
                resultBytes = cipherDoFinal(cipher, srcBytes, segmentSize); //分段加密
            else
                resultBytes = cipher.doFinal(srcBytes);
            String base64Str = Base64Utils.encodeToString(resultBytes);
            return base64Str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分段大小
     *
     * @param cipher
     * @param srcBytes
     * @param segmentSize
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
    public static byte[] cipherDoFinal(Cipher cipher, byte[] srcBytes, int segmentSize)
            throws IllegalBlockSizeException, BadPaddingException, IOException {
        if (segmentSize <= 0)
            throw new RuntimeException("分段大小必须大于0");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int inputLen = srcBytes.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > segmentSize) {
                cache = cipher.doFinal(srcBytes, offSet, segmentSize);
            } else {
                cache = cipher.doFinal(srcBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * segmentSize;
        }
        byte[] data = out.toByteArray();
        out.close();
        return data;
    }



}
