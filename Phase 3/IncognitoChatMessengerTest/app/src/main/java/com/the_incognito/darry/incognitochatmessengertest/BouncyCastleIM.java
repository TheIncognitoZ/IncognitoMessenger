package com.the_incognito.darry.incognitochatmessengertest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import org.spongycastle.jce.ECNamedCurveTable;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.jce.spec.ECParameterSpec;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class BouncyCastleIM {

    {
        Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
    }
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
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1",new BouncyCastleProvider());
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
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1",new BouncyCastleProvider());
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
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1",new BouncyCastleProvider());
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


    public static KeyPair generateKeys() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1");
        //ECGenParameterSpec ecSpec = new ECGenParameterSpec("P-224");
        KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", new BouncyCastleProvider());
        g.initialize(ecSpec, new SecureRandom());
        return g.generateKeyPair();
    }

    public static String genSign(PrivateKey privateKey, String str) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        //KeyPair pair = keyGen.generateKeyPair();
        //PrivateKey priv = pair.getPrivate();
        //PublicKey pub = pair.getPublic();
        Signature ecdsaSign = Signature.getInstance("SHA256withECDSA", new BouncyCastleProvider());
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update(BouncyCastleIM.toByte(str));//(str.getBytes("UTF-8"));
        byte[] byteSignature = ecdsaSign.sign();
        String signature = new BigInteger(1, byteSignature).toString(16);
        return signature;
    }
    public static String hmac(String text){
        MessageDigest digest=null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        digest.reset();

        String mac = new BigInteger(1, digest.digest(text.getBytes())).toString(16);
        return mac;
    }
    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
    }
    public static boolean vrfySig(String signature, PublicKey publicKey, String msg) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        Signature ecdsaVrfy = Signature.getInstance("SHA256withECDSA", new BouncyCastleProvider());
        ecdsaVrfy.initVerify(publicKey);
        ecdsaVrfy.update(BouncyCastleIM.toByte(msg));//(msg.getBytes("UTF-8"));
        boolean result = ecdsaVrfy.verify(BouncyCastleIM.toByte(signature));//(signature.getBytes("UTF-8"));
        return result;
    }

    public static String toHex(byte[] data, int length) {
        String out = new String();
        int loop;
        for (loop = 0; loop < length; loop++)
            out = out + String.format("%02X", data[loop]);
        return out;
    }

    public static String toHex(byte[] data) {
        return toHex(data, data.length);
    }

    public static String toHexAndTrim00AtFirst(byte[] data) {
        String result = toHex(data, data.length);
        while (result.startsWith("00")) {
            result = result.substring(2);
        }
        return result;
    }

    public static String toHex(byte data) {
        byte dataArray[] = { data };
        return toHex(dataArray);
    }

    public static byte[] rendomByte(int length) {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[length];
        random.nextBytes(bytes);
        return bytes;
    }

    public static byte[] toByte(String hex) {
        if (hex == null)
            return null;
        hex = hex.replaceAll("\\s", "");
        byte[] buffer = null;
        if (hex.length() % 2 != 0) {
            hex = "0" + hex;
        }
        int len = hex.length() / 2;
        buffer = new byte[len];
        for (int i = 0; i < len; i++) {
            buffer[i] = (byte) Integer.parseInt(
                    hex.substring(i * 2, i * 2 + 2), 16);
        }
        return buffer;
    }

    public static byte[] intTo2ByteArray(int value) {
        return new byte[] { (byte) (value >>> 8), (byte) value };
    }

    public static short byteArrayToInt(byte[] bArray) {
        ByteBuffer buffer = ByteBuffer.wrap(bArray);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getShort();
    }

    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    public static String stringToHex(String input)
            throws UnsupportedEncodingException {
        if (input == null)
            throw new NullPointerException();
        return asHex(input.getBytes());
    }

    private static String asHex(byte[] buf) {
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i) {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }

    public static String hexToString(String hex) {

        StringBuilder sb = new StringBuilder();
        char[] hexData = hex.toCharArray();
        for (int count = 0; count < hexData.length - 1; count += 2) {
            int firstDigit = Character.digit(hexData[count], 16);
            int lastDigit = Character.digit(hexData[count + 1], 16);
            int decimal = firstDigit * 16 + lastDigit;
            sb.append((char) decimal);
        }
        return sb.toString();
    }
}