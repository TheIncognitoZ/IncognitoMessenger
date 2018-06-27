package com.the_incognito.darry.incognitochatmessengertest;

/**
 * Created by darry on 12/3/2016.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
        import java.security.Key;
        import java.security.NoSuchAlgorithmException;
        import java.security.spec.InvalidKeySpecException;
        import java.security.spec.KeySpec;
        import java.util.Scanner;
        import javax.crypto.*;
        import javax.crypto.spec.IvParameterSpec;
        import javax.crypto.spec.PBEKeySpec;
        import javax.crypto.spec.SecretKeySpec;
        import org.apache.commons.codec.binary.Base64;
        import org.apache.commons.codec.binary.Hex;
        import org.apache.commons.lang3.RandomStringUtils;
        import org.spongycastle.jce.provider.BouncyCastleProvider;
/**
 *
 * @author Smitesh
 */

public class BouncyCastleImplementation {
   /* public static void main(String args[]) throws Exception
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the string to encrypt:");
        String plainText= sc.nextLine();
        System.out.println("Enter the key:");
        String key = sc.nextLine();
        String encryptedText = encryption(key,plainText);
        String decryptedText = decryption(key,encryptedText);
        System.out.println("Encrypted Text: "+encryptedText);
        *//**
         * Use the encryption function to generated a probabilistic ciphertext which is also hashed. If the message is invalid, it will be destroyed
         * and when decryption is used on it, it will return "Invalid Text"
         * @param key will be a password on which the key will be derived.
         * @param plaintext will be the message to be encrypted.
         *//*

        System.out.println("Decrypted Text: "+decryptedText);
    }*/
    public static String encryption(String key, String plainText) throws Exception
    {
        String ET = encrypt(key,plainText) ;
        String HMAC = hmacSha256(key,ET);
        return ET+HMAC;
    }
    public static String decryption(String key, String encrypted) throws Exception
    {
        String extractHMAC = encrypted.substring(encrypted.length()-64, encrypted.length());
        System.out.println("Extracted HMAC = "+extractHMAC);
        boolean check = isValid(encrypted.substring(0,encrypted.length()-64),extractHMAC,key);
        //String result = new String();
        if(check)
        {
            String DT = decrypt(key,encrypted.substring(0, encrypted.length()-64));
            return DT;
        }
        return "Invalid String";
    }
    public static String encrypt(String key, String toEncrypt) throws Exception {
        Key skeySpec = generateKeySpec(key);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding",new BouncyCastleProvider());
        String abc = RandomStringUtils.randomAlphanumeric(16);
        System.out.println(abc);
        byte[] ivBytes = abc.getBytes();
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());
        byte[] encryptedValue;
        encryptedValue = Base64.encodeBase64(encrypted);
        String result = new String();
        result += new String(encryptedValue);
        result = abc+result;
        System.out.println(result);
        return result;
    }

    public static String decrypt(String key, String encrypted) throws Exception {
        Key skeySpec = generateKeySpec(key);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding",new BouncyCastleProvider());
        String abc = encrypted.substring(0,16);
        System.out.println(abc);
        byte[] ivBytes = abc.getBytes();
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE,skeySpec, ivSpec);
        //cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decodedBytes = Base64.decodeBase64(encrypted.substring(16).getBytes());
        byte[] original = cipher.doFinal(decodedBytes);
        return new String(original);
    }

    private static Key generateKeySpec(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256",new BouncyCastleProvider());
        char password[]= key.toCharArray();
        byte salt[]= "salt".getBytes();
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        return secret;
        //To change body of generated methods, choose Tools | Templates.
    }
    public static String hmacSha256(String key, String value) {
        try {
            //System.out.println(Base64.getEncoder().encodeToString(keyBytes));
            // Get an hmac_sha256 key from the raw key bytes
            System.out.println("First HMAC on = "+value);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256",new BouncyCastleProvider());
            char password[]= key.toCharArray();
            byte salt[]= "salt".getBytes();
            KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "HmacSHA256");

            // Get an hmac_sha256 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance("HmacSHA256",new BouncyCastleProvider());
            mac.init(secret);

            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(value.getBytes());

            // Convert raw bytes to Hex
            byte[] hexBytes = new Hex().encode(rawHmac);
            //  Covert array of Hex bytes to a String
            return new String(hexBytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public static boolean isValid(String plainText, String HMAC, String key)
    {
        try {
            System.out.println("HMAC on = "+plainText);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256",new BouncyCastleProvider());
            char password[]= key.toCharArray();
            byte salt[]= "salt".getBytes();
            KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "HmacSHA256");
            // Get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance("HmacSHA256", new BouncyCastleProvider());
            mac.init(secret);
            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(plainText.getBytes());
            // Convert raw bytes to Hex
            byte[] hexBytes = new Hex().encode(rawHmac);

            //  Covert array of Hex bytes to a String
            String check = new String(hexBytes, "UTF-8");
            System.out.println("Checking = "+check);
            return check.equals(HMAC);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
