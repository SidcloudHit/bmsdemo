/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Security;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 *
 * @author acer
 */
public class AES {

    /**
     * Program to Encrypt/Decrypt String Using AES 128 bit Encryption Algorithm
     */
    private static final String encryptionKey = "543";
    private static final String characterEncoding = "UTF-8";
    private static final String cipherTransformation = "AES/CBC/PKCS5PADDING";
    private static final String aesEncryptionAlgorithem = "AES";

    /**
     * Method for Encrypt Plain String Data
     *
     * @param plainText
     * @return encryptedText
     */
    public static String encrypt(String plainText) {
        String encryptedText = "";
        try {
            Cipher cipher = Cipher.getInstance(cipherTransformation);
            byte[] key = encryptionKey.getBytes(characterEncoding);
            byte[] keyBytes = new byte[16];
            System.arraycopy(key, 0, keyBytes, 0, Math.min(key.length, keyBytes.length));
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, aesEncryptionAlgorithem);
//            SecureRandom random = new SecureRandom();
//            byte[] iv = random.generateSeed(16);

            IvParameterSpec ivparameterspec = new IvParameterSpec(keyBytes);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparameterspec);
            byte[] cipherText = cipher.doFinal(plainText.getBytes("UTF8"));
            Base64.Encoder encoder = Base64.getEncoder();
            encryptedText = encoder.encodeToString(cipherText);

        } catch (Exception E) {
            System.err.println("Encrypt Exception : " + E.getMessage());
        }
        return encryptedText;
    }

    /**
     * Method For Get encryptedText and Decrypted provided String
     *
     * @param encryptedText
     * @return decryptedText
     */
    public static String decrypt(String encryptedText) {
        String decryptedText = "";
        try {
            Cipher cipher = Cipher.getInstance(cipherTransformation);
            byte[] key = encryptionKey.getBytes(characterEncoding);
            byte[] keyBytes = new byte[16];
            System.arraycopy(key, 0, keyBytes, 0, Math.min(key.length, keyBytes.length));
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, aesEncryptionAlgorithem);
            IvParameterSpec ivparameterspec = new IvParameterSpec(keyBytes);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] cipherText = decoder.decode(encryptedText.getBytes("UTF8"));
            decryptedText = new String(cipher.doFinal(cipherText), "UTF-8");

        } catch (Exception E) {
            System.err.println("decrypt Exception : " + E.getMessage());
        }
        return decryptedText;
    }

    

//    public static void main(String[] args) {
////        Scanner sc = new Scanner(System.in);
////        System.out.println("Enter String : ");
//        String plainString = "338833399861";
////        
//        String encyptStr = encrypt(plainString);
//        String decryptStr = decrypt(encyptStr);
//
//        System.out.println("Plain   String  : " + plainString);
//        System.out.println("Encrypt String  : " + encyptStr);
//        System.out.println("Decrypt String  : " + decryptStr);
//
//    }
}
