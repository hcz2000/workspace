package com.zhan.myreader.util;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * <br> *RSA瀹夊叏缂栫爜缁勪欢
 * 
 */
public abstract class RSACoder extends Coder {
	public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
     
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivatekey";
     
    /**
     * 鐢ㄧ閽ュ淇℃伅鐢熸垚鏁板瓧绛惧悕
     * @param data 鍔犲瘑鏁版嵁
     * @param privateKey 绉侀挜
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        //瑙ｅ瘑鐢眀ase64缂栫爜鐨勭閽�
        byte[] keyBytes = decryptBASE64(privateKey);
         
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
         
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
         
        //鍙栫閽ュ璞�
        PrivateKey pKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
         
        //鐢ㄧ閽ョ敓鎴愭暟瀛楃鍚�
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(pKey);
        signature.update(data);
         
        return encryptBASE64(signature.sign());
    }
     
    /**
     * 鏍￠獙鏁板瓧绛惧悕
     * @param data 鍔犲瘑鏁版嵁
     * @param publicKey 鍏挜
     * @param sign 鏁板瓧绛惧悕
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
         
        //瑙ｅ瘑鏈塨ase64缂栫爜鐨勫叕閽�
        byte[] keyBytes = decryptBASE64(publicKey);
         
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
         
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
         
        //鍙栧叕閽ュ璞�
        PublicKey pKey = keyFactory.generatePublic(keySpec);
         
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pKey);
        signature.update(data);
        //楠岃瘉绛惧悕鏄惁姝ｅ父
        return signature.verify(decryptBASE64(sign));
    }
     
    /**
     * 瑙ｅ瘑
     *  鐢ㄧ閽ヨВ瀵�
     * @param data 鍔犲瘑鏁版嵁
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptPrivateKey(byte[] data, String key) throws Exception {
        byte[] keyBytes = decryptBASE64(key);
         
        //鍙栧緱绉侀挜
        PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key pKey = factory.generatePrivate(encodedKeySpec);
         
        //瀵规暟鎹В瀵�
        Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pKey);
         
        return cipher.doFinal(data);
    }
     
    /**
     * 鐢ㄥ叕閽ヨВ瀵�
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey( byte[] data, String key) throws Exception {
         
        //瑙ｅ瘑
        byte[] keyBytes = decryptBASE64(key);
         
        //鍙栧緱鍏挜
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key pKey = keyFactory.generatePublic(keySpec);
         
        //瀵规暟鎹В瀵�
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pKey);
         
        return cipher.doFinal(data);
    }
     
    /**
     * 鐢ㄥ叕閽ュ姞瀵�
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey( byte[] data, String key) throws Exception {
         
        byte[] keyBytes = decryptBASE64(key);
         
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key pKey = keyFactory.generatePublic(keySpec);
         
         
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pKey);
         
        return cipher.doFinal(data);
    }
     
    /**
     * 鐢ㄧ閽ュ姞瀵�
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
         
        byte[] keyBytes = decryptBASE64(key);
         
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(keySpec);
         
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
         
        return cipher.doFinal(data);
    }
     
    /**
     * 鍙栧緱绉侀挜
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
 
        Key key = (Key) keyMap.get(PRIVATE_KEY);
         
        return encryptBASE64(key.getEncoded());
    }
     
    /**
     * 鍙栧緱鍏挜
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
 
        Key key = (Key) keyMap.get(PUBLIC_KEY);
         
        return encryptBASE64(key.getEncoded());
    }
    /**
     * 鍒濆鍖栧瘑閽�
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
         
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(1024);
         
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //鍏挜
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
         
        //绉侀挜
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
         
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PRIVATE_KEY, privateKey);
        keyMap.put(PUBLIC_KEY, publicKey);
        return keyMap;
    }
    
   
    public static void test2() throws Exception
    {
       Map<String , Object> keyMap = RSACoder.initKey();
        
        String publicKey = RSACoder.getPublicKey(keyMap);
        String privateKey = RSACoder.getPrivateKey(keyMap);
         
        System.out.println("鍏挜锛歕n" + publicKey);
        System.out.println("绉侀挜锛歕n" + privateKey);
        
    	System.err.println("绉侀挜鍔犲瘑鈥斺�斿叕閽ヨВ瀵�");
        String inputStr = "sign";
        byte[] data = inputStr.getBytes(); 
 
        byte[] encodedData = RSACoder.encryptByPrivateKey(data, privateKey); 
 
        byte[] decodedData = RSACoder 
                .decryptByPublicKey(encodedData, publicKey); 
 
        String outputStr = new String(decodedData);
        System.err.println("鍔犲瘑鍓�: " + inputStr + "\n\r" + "瑙ｅ瘑鍚�: " + outputStr);
      
 
        System.err.println("绉侀挜绛惧悕鈥斺�斿叕閽ラ獙璇佺鍚�");
        // 浜х敓绛惧悕 
        String sign = RSACoder.sign(encodedData, privateKey);
        System.err.println("绛惧悕:\r" + sign);
 
        // 楠岃瘉绛惧悕 
        boolean status = RSACoder.verify(encodedData, publicKey, sign); 
        System.err.println("鐘舵��:\r" + status);
        
    }
    
  /*  public static void main(String[] args) throws Exception {
    	
    	
    	//test2();
    	test();
    }*/
}