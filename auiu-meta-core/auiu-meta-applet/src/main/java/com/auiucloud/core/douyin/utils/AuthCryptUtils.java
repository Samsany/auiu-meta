package com.auiucloud.core.douyin.utils;

import com.auiucloud.core.common.exception.ApiException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @author dries
 **/
public class AuthCryptUtils {

    public static String decrypt(String encryptedData, String sessionKey, String iv) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] sessionKeyBytes = decoder.decode(sessionKey);
            byte[] ivBytes = decoder.decode(iv);
            byte[] encryptedBytes = decoder.decode(encryptedData);

            // JDK does not support PKCS7Padding, use PKCS5Padding instead
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec skeySpec = new SecretKeySpec(sessionKeyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
            byte[] ret = cipher.doFinal(encryptedBytes);
            return new String(ret);
        } catch (Exception e) {
            throw new ApiException("AES解密失败！", e.getCause());
        }
    }

}
