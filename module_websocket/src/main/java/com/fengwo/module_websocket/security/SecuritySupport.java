package com.fengwo.module_websocket.security;


import android.util.Log;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class SecuritySupport {

    private final static String DES = "DES";
    static String key = "mtom123210woshiainide36";

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception {
        byte[] bt = encrypt(data.getBytes(), key.getBytes());
        String strs = Base64.encode(bt);//new BASE64Encoder().encode(bt);
        return strs;
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data) throws IOException,
            Exception {
        Log.e( "decrypt: ", "data "+data);
        if (data == null)
            return null;
        byte[] buf = Base64.decode(data);
        byte[] bt = decrypt(buf, key.getBytes());
        return new String(bt);
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源  
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象  
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作  
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象  
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }


    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源  
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        Log.e( "decrypt: ", "key "+key);
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作  
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象


        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }
} 