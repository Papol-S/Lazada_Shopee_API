package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
public class SignatureUtil {
    private static final String HMAC_SHA256 = "HmacSHA256";

    private static final Logger LOGGER = LoggerFactory.getLogger(SignatureUtil.class);

    /**
     * Hmac-SHA256
     * @param base {AppKey} + {messageBody}
     * @param secret {AppSecret}
     * E.g.: AppKey = 123456, AppSecret = 3412gyo124goi3124
     * messageBody ：{"seller_id":"1234567", "message_type":0, "data":{...}..}
     *
     * base = "123456" + "{\"seller_id\":\"1234567\", \"message_type\":0, "data":{...}..}"
     * secret = 3412gyo124goi3124
     * signature = getSignature(base, secret);
     * signature =  f3d2ca947f16a50b577c036adecd18bec126ea19cadedd59816e255d3b6104ab
     * @return 签名
     */
    public static String getSignature(String base, String secret) {
        try {
            Mac sha256Hmac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA256);
            sha256Hmac.init(secretKey);
            return byteArraytoHexString(sha256Hmac.doFinal(base.getBytes()));
        } catch (Exception e) {
            LOGGER.error("Failed to generate signature");
        }
        return null;

    }

    /**
     * Hex Encode
     * @param bytes
     * @return
     */
    private static String byteArraytoHexString(byte[] bytes) {
        if(bytes == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String stmp;
        for (byte aByte : bytes) {
            stmp = Integer.toHexString(aByte & 0XFF);
            if (stmp.length() == 1) {
                sb.append('0');
            }
            sb.append(stmp);
        }
        return sb.toString().toLowerCase();
    }
}
