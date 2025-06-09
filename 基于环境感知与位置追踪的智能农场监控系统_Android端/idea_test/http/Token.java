package com.example.idea_test.http;
import android.os.Build;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
/*
 * URL=iot-api.heclouds.com/thingmodel/query-device-property?product_id=9hu56lk2XL&device_name=mqtt
 *authorization    version=2020-05-29&res=userid%2F360841&et=1956499200&method=sha1&sign=TQ3e%2FaG%2FmWVHsiAvmwiZGWvDp6s%3D
 *
 * */
public class Token {

    public static String assembleToken(String version, String resourceName, String expirationTime, String signatureMethod, String accessKey)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        StringBuilder sb = new StringBuilder();
        String res = URLEncoder.encode(resourceName, "UTF-8");
        String sig = URLEncoder.encode(generatorSignature(version, resourceName, expirationTime, accessKey, signatureMethod), "UTF-8");
        sb.append("version=")
                .append(version)
                .append("&res=")
                .append(res)
                .append("&et=")
                .append(expirationTime)
                .append("&method=")
                .append(signatureMethod)
                .append("&sign=")
                .append(sig);
        return sb.toString();
    }

    public static String generatorSignature(String version, String resourceName, String expirationTime, String accessKey, String signatureMethod)
            throws NoSuchAlgorithmException, InvalidKeyException {
        String encryptText = expirationTime + "\n" + signatureMethod + "\n" + resourceName + "\n" + version;
        String signature = null;
        byte[] bytes = HmacEncrypt(encryptText, accessKey, signatureMethod);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            signature = Base64.getEncoder().encodeToString(bytes);
        }
        return signature;
    }

    public static byte[] HmacEncrypt(String data, String key, String signatureMethod)
            throws NoSuchAlgorithmException, InvalidKeyException {
        //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKeySpec signinKey = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            signinKey = new SecretKeySpec(Base64.getDecoder().decode(key),
                    "Hmac" + signatureMethod.toUpperCase());
        }

        //生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = null;
        mac = Mac.getInstance("Hmac" + signatureMethod.toUpperCase());

        //用给定密钥初始化 Mac 对象
        mac.init(signinKey);

        //完成 Mac 操作
        return mac.doFinal(data.getBytes());
    }

    public enum SignatureMethod {
        SHA1, MD5, SHA256;
    }

    public  String  key() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String version = "2018-10-31";
        String resourceName = "userid/405422";//"userid/12321";
        String expirationTime = 1903093211+"";
        String signatureMethod = SignatureMethod.SHA1.name().toLowerCase();
        String accessKey = "y168z0xaivhgOJLLajCAHzJkh/am+BtX4JuEjFdYNPLGAQZsjaJLxagjN2CILawR";
        String token = assembleToken(version, resourceName, expirationTime, signatureMethod, accessKey);
        System.out.println("Authorization:" + token);
        return token;
    }
}
