package org.andromda.cartridges.hibernate;



import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;

import org.andromda.cartridges.hibernate.metafacades.HibernateGlobals;

public class CriptoUtils {
	public CriptoUtils(){}
 
 private static String MD_ALGORITHM = "MD5";
 private static String DS_ALGORITHM = "MD5withRSA";
 private static String DS_ALIAS = null;
 private static String DS_PASSWORD = null;
 private static File DS_CERTIFICATE = null;
 
	static {
		java.util.Properties properties = new java.util.Properties();
		try {
			properties.load(new java.io.FileInputStream(new java.io.File((new java.net.URL(System.getProperty("jboss.server.config.url") + java.io.File.separator + "${projectId}-port.properties")).getFile())));
			
			String value = properties.getProperty("message.digest.algorithm");
			if (value != null) {
				MD_ALGORITHM = value;
			}
			
			value = properties.getProperty("digital.signature.algorithm");
			if (value != null) {
				DS_ALGORITHM = value;
			}
			
			value = properties.getProperty("digital.signature.alias");
			if (value != null) {
				DS_ALIAS = value;
			}
			
			value = properties.getProperty("digital.signature.password");
			if (value != null) {
				DS_PASSWORD = value;
			}
			
			value = properties.getProperty("digital.signature.certificate.file");
			if (value != null) {
				DS_CERTIFICATE = new File(value);
			}
		}catch(java.io.IOException e) {
		}
	}
 
 private static final String hexDigits = "0123456789abcdef";
 /**
 * Realiza um digest em um array de bytes através do algoritmo especificado
 * @param input - O array de bytes a ser criptografado
 * @param algoritmo - O algoritmo a ser utilizado
 * @return byte[] - O resultado da criptografia
 * @throws NoSuchAlgorithmException - Caso o algoritmo fornecido não seja
 * válido
 */
 public static byte[] digest(byte[] input, String algoritmo)
     throws NoSuchAlgorithmException {
     MessageDigest md = MessageDigest.getInstance(algoritmo);
     md.reset();
     return md.digest(input);
 }

 /**
 * Realiza um digest em um array de bytes através do algoritmo padrao.
 * O algoritmo padrao pode ser definido atraves da propriedade
 * 'message.digest.algorithm' no arquivo '${projectId}-port.properties'.
 * Caso nao seja definido um valor para esta propriedade, sera usado o algoritimo MD5.
 * @param input - O array de bytes a ser criptografado
 * @return byte[] - O resultado da criptografia
 * @throws NoSuchAlgorithmException - Caso o algoritmo fornecido não seja
 * válido
 */
 public static byte[] digest(byte[] input)
     throws NoSuchAlgorithmException {
     return digest(input, MD_ALGORITHM);
 }

	public static PrivateKey getPrivateKeyFromFile(File cert, String alias, String password, String keyStoreType) throws Exception {  
		KeyStore ks = KeyStore.getInstance (keyStoreType);
		char[] pwd = password.toCharArray();  
		InputStream is = new FileInputStream(cert);  
		ks.load(is, pwd);  
		is.close();  
		Key key = ks.getKey(alias, pwd);  
		if(key instanceof PrivateKey) {  
			return (PrivateKey) key;  
		}
		return null;  
	}

	public static PrivateKey getPrivateKeyFromFile(File cert, String alias, String password) throws Exception {  
		return getPrivateKeyFromFile(cert, alias, password, "JKS");
	}

	public static PrivateKey getPrivateKeyFromFile() throws Exception {
		return getPrivateKeyFromFile(DS_CERTIFICATE, DS_ALIAS, DS_PASSWORD);
	}

	/** 
	 * Extrai a chave pública do arquivo. 
	 */  
	public static PublicKey getPublicKeyFromFile(File cert, String alias, String password, String keyStoreType) throws Exception {  
		KeyStore ks = KeyStore.getInstance (keyStoreType);  
		char[] pwd = password.toCharArray();  
		InputStream is = new FileInputStream(cert);  
		ks.load(is, pwd);  
//		Key key = ks.getKey(alias, pwd);  
		Certificate c = ks.getCertificate(alias);  
		PublicKey p = c.getPublicKey();  
		return p;  
	}

	/** 
	 * Extrai a chave pública do arquivo. 
	 */  
	public static PublicKey getPublicKeyFromFile(File cert, String alias, String password) throws Exception {  
		return getPublicKeyFromFile(cert, alias, password, "JKS");  
	}

	/** 
	 * Extrai a chave pública do arquivo. 
	 */  
	public static PublicKey getPublicKeyFromFile() throws Exception {  
		return getPublicKeyFromFile(DS_CERTIFICATE, DS_ALIAS, DS_PASSWORD);  
	}

	/** 
	 * Retorna a assinatura para o buffer de bytes, usando a chave privada. 
	 * @param key PrivateKey 
	 * @param buffer Array de bytes a ser assinado. 
	 * @param algorithm Nome do algoritmo a ser utilizado. 
	 */  
	public static byte[] createSignature(PrivateKey key, byte[] buffer, String algorithm) throws Exception {  
		Signature sig = Signature.getInstance(algorithm);  
		sig.initSign(key);  
		sig.update(buffer, 0, buffer.length);  
		return sig.sign();  
	}  

	/** 
	 * Retorna a assinatura para o buffer de bytes, usando a chave privada e o algoritmo padrao. 
	 * @param buffer Array de bytes a ser assinado. 
	 */  
	public static byte[] createSignature(byte[] buffer) throws Exception {  
		return createSignature(getPrivateKeyFromFile(), buffer, DS_ALGORITHM);  
	}  

	/** 
	 * Verifica a assinatura para o buffer de bytes, usando a chave pública. 
	 * @param key PublicKey 
	 * @param buffer Array de bytes a ser verificado. 
	 * @param signed Array de bytes assinado (encriptado) a ser verificado. 
	 * @param algorithm Nome do algoritmo a ser utilizado. 
	 */  
	public static boolean verifySignature(PublicKey key, byte[] buffer, byte[] signed, String algorithm) throws Exception {  
		Signature sig = Signature.getInstance(algorithm);  
		sig.initVerify(key);
		sig.update(buffer, 0, buffer.length);
		return sig.verify(signed);  
	}  

	/** 
	 * Verifica a assinatura para o buffer de bytes, usando a chave pública e o algoritmo padrao. 
	 * @param buffer Array de bytes a ser verificado. 
	 * @param signed Array de bytes assinado (encriptado) a ser verificado. 
	 */  
	public static boolean verifySignature(byte[] buffer, byte[] signed) throws Exception {  
		return verifySignature(getPublicKeyFromFile(), buffer, signed, DS_ALGORITHM);  
	}

 /**
  * Converte o array de bytes em uma representação hexadecimal.
  * @param input - O array de bytes a ser convertido.
  * @return Uma String com a representação hexa do array
  */
 public static String byteArrayToHexString(byte[] b) {
     StringBuffer buf = new StringBuffer();

     for (int i = 0; i < b.length; i++) {
         int j = ((int) b[i]) & 0xFF;
         buf.append(hexDigits.charAt(j / 16));
         buf.append(hexDigits.charAt(j % 16));
     }
 
     return buf.toString();
 }

 /**
  * Converte uma String hexa no array de bytes correspondente.
  * @param hexa - A String hexa
  * @return O vetor de bytes
  * @throws IllegalArgumentException - Caso a String não seja uma
  * representação haxadecimal válida
  */
 public static byte[] hexStringToByteArray(String hexa)
     throws IllegalArgumentException {

     //verifica se a String possui uma quantidade par de elementos
     if (hexa.length() % 2 != 0) {
         throw new IllegalArgumentException("String hexa invÃ¡lida");
     }

     byte[] b = new byte[hexa.length() / 2];

     for (int i = 0; i < hexa.length(); i+=2) {
         b[i / 2] = (byte) ((hexDigits.indexOf(hexa.charAt(i)) << 4) |
             (hexDigits.indexOf(hexa.charAt(i + 1))));
     }
     return b;
 }
 private String maxSqlNameLength;
 
 public String getMaxSqlNameLength() {
	return maxSqlNameLength;
}

public void setMaxSqlNameLength(String maxSqlNameLength) {
	this.maxSqlNameLength = maxSqlNameLength;
}

public String generateTruncatedIndex(String str1,String str2){
	String retorno = null;
	retorno = "IND_"+str1+"_"+str2;
	int tamanho= Integer.parseInt(maxSqlNameLength);
	try {
		byte[] bytes = CriptoUtils.digest(retorno.getBytes(), "MD5");
			retorno="IND_";
			retorno+=CriptoUtils.byteArrayToHexString(bytes);
			retorno = retorno.substring(0, tamanho);
	} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
	}
	return retorno;
 }
}
