/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author NIC_Admin
 */
public class AESEncryption {
  private static final String ALGO = "AES";  
  private static final String key="5s4e3c2r1e0t6978";
  private static final byte[] keyValue = key.getBytes();

    public static String encrypt(String Data) throws Exception {
        SecretKeySpec key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }
          public static String decrypt(String encryptedData) throws Exception {
        SecretKeySpec key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }
    private static SecretKeySpec generateKey() throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyValue, ALGO);
        return key;
}
}
