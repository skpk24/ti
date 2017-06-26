package org.ofbiz.common.security;


import java.security.Key;
import java.util.HashMap;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.string.FlexibleStringExpander;
import org.ofbiz.entity.util.EntityUtilProperties;


public class AdminSecurityUtil{

	
	
	private static final String PRIVATE_KEY_FILE_LOCATION = "main-aes-keystore.jck";
	
	
	/**
	 * Method to encrypt the passed plain text for the url params.
	 * 
	 * @param plainText
	 * @return cipherText
	 * @throws InvalidTicketException
	 */
	public static String encryptMessageForUrl(String plainText) throws InvalidTicketException{
		String url = FlexibleStringExpander.expandString(UtilProperties.getPropertyValue("admin.properties", "security.cipher.filelocation"), new HashMap());
		Key key = KeystoreUtil.getKeyFromKeyStore(url+PRIVATE_KEY_FILE_LOCATION, "mystorepass", "myseckey", "mykeypass");
		AESCipher cipher = new AESCipher(key);
        String cipherText = cipher.getEncryptedMessage(plainText);
        cipherText = performPostEncodeChanges(cipherText);
        return cipherText;
	}
	
	/**
	 * Performs the post encoding changes to message.
	 * 
	 * @param message
	 * @return
	 */
	public static String performPostEncodeChanges(String encodedText){
		encodedText = encodedText.replaceAll("\\+", "-");
        encodedText = encodedText.replaceAll("/", "_");
		return encodedText;
	}
	
	/**
	 * Method to decrypt the passed url encoded cipher text
	 * 
	 * @param cipherText
	 * @return plainText
	 * @throws InvalidTicketException
	 */
	public static String decryptMessageForUrl(String cipherText) throws InvalidTicketException{
		String url = FlexibleStringExpander.expandString(UtilProperties.getPropertyValue("admin.properties", "security.cipher.filelocation"), new HashMap());
		Key key = KeystoreUtil.getKeyFromKeyStore(url+PRIVATE_KEY_FILE_LOCATION, "mystorepass", "myseckey", "mykeypass");
		AESCipher cipher = new AESCipher(key);
		cipherText = performPreDecodeChanges(cipherText);
        String plainText = cipher.getDecryptedMessage(cipherText);
        return plainText;
	}
	/**
	 * Performs the message changes before decoding.
	 * 
	 * @param message
	 * @return
	 */
	public static String performPreDecodeChanges(String textToDecode){
		textToDecode = textToDecode.replaceAll("-", "\\+");
		textToDecode = textToDecode.replaceAll("_", "/");
		return textToDecode;
	}

}

