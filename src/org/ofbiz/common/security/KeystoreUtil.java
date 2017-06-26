package org.ofbiz.common.security;
/*
 * Copyright &#169; 2014-2015 Mivim IT Solutions.  All Rights Reserved.
 * 
 * Confidential, Proprietary and Trade Secrets Notice
 * 
 * Use of this software is governed by a license agreement. This software
 * contains confidential, proprietary and trade secret information of
 * Mivim IT Solutions. and is protected under India and
 * international copyright and other intellectual property laws. Use, disclosure,
 * reproduction, modification, distribution, or storage in a retrieval system in
 * any form or by any means is prohibited without the prior express written
 * permission of Mivim IT Solutions.
 * 
 * Mivim IT Solutions.
 * India
 */


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import org.ofbiz.base.util.*;

import java.util.HashMap;
import java.util.Properties;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.string.FlexibleStringExpander;

public class KeystoreUtil {

	public static Key getKeyFromKeyStore(final String keystoreLocation,
			final String keystorePass, final String alias, final String keyPass) {
		try {
			
			String filelocation = FlexibleStringExpander.expandString(UtilProperties.getPropertyValue("admin.properties", "security.cipher.filelocation")+"main-aes-keystore.jck", new HashMap());
			Debug.log("\n\n filelocation == "+filelocation+"\n\n");
			InputStream keystoreStream = new FileInputStream(filelocation);
			KeyStore keystore = KeyStore.getInstance("JCEKS");
			Debug.log("\n\n keystoreStream == "+keystoreStream+"\n\n");
			keystore.load(keystoreStream, keystorePass.toCharArray());
			

			if (!keystore.containsAlias(alias)) {
				throw new RuntimeException("Alias for key not found");
			}

			Key key = keystore.getKey(alias, keyPass.toCharArray());

			return key;

		} catch (UnrecoverableKeyException | KeyStoreException
				| CertificateException | IOException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}
