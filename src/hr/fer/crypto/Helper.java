package hr.fer.crypto;

import hr.fer.aes.ECB;
import hr.fer.aes.Key;
import hr.fer.aes.MessageCipher;
import hr.fer.aes.OFB;
import hr.fer.aes.Key.KeySize;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;


public class Helper {
		
		//za base64 string
		private final static char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
	    private static int[] toInt = new int[128];

	    static {
	        for(int i=0; i< ALPHABET.length; i++){
	            toInt[ALPHABET[i]]= i;
	        }
	    }
	    
	    //prevodi specificirano polje bajtova u Base64 string
	   	    public static String base64Encode(byte[] buf){
	        int size = buf.length;
	        char[] ar = new char[((size + 2) / 3) * 4];
	        int a = 0;
	        int i=0;
	        while(i < size){
	            byte b0 = buf[i++];
	            byte b1 = (i < size) ? buf[i++] : 0;
	            byte b2 = (i < size) ? buf[i++] : 0;

	            int mask = 0x3F;
	            ar[a++] = ALPHABET[(b0 >> 2) & mask];
	            ar[a++] = ALPHABET[((b0 << 4) | ((b1 & 0xFF) >> 4)) & mask];
	            ar[a++] = ALPHABET[((b1 << 2) | ((b2 & 0xFF) >> 6)) & mask];
	            ar[a++] = ALPHABET[b2 & mask];
	        }
	        switch(size % 3){
	            case 1: ar[--a]  = '=';
	            case 2: ar[--a]  = '=';
	        }
	        return new String(ar);
	    }

	    public static byte[] base64Decode(String s){
	        int delta = s.endsWith( "==" ) ? 2 : s.endsWith( "=" ) ? 1 : 0;
	        byte[] buffer = new byte[s.length()*3/4 - delta];
	        int mask = 0xFF;
	        int index = 0;
	        for(int i=0; i< s.length(); i+=4){
	            int c0 = toInt[s.charAt( i )];
	            int c1 = toInt[s.charAt( i + 1)];
	            buffer[index++]= (byte)(((c0 << 2) | (c1 >> 4)) & mask);
	            if(index >= buffer.length){
	                return buffer;
	            }
	            int c2 = toInt[s.charAt( i + 2)];
	            buffer[index++]= (byte)(((c1 << 4) | (c2 >> 2)) & mask);
	            if(index >= buffer.length){
	                return buffer;
	            }
	            int c3 = toInt[s.charAt( i + 3 )];
	            buffer[index++]= (byte)(((c2 << 6) | c3) & mask);
	        }
	        return buffer;
	    } 
		
		public static String readFile(String path) {
			String content = new String();
			String line;
			BufferedReader br = null;
			
			try {
				br = new BufferedReader(new FileReader(path));
			} catch (IOException e1) {}
			
			try {
				while((line = br.readLine()) != null)
					content += line + System.getProperty("line.separator");
			} catch (Exception e) {}
				
			return content;
		}
		
		public static void generateAESKey(String filepath, KeySize size) {
			try {
				OS2CryptoFile file = new OS2CryptoFile();
				String key = Utilities.hexToString(Utilities.generateRandomBlock(size.getBytesNum()));				
				//spremanje u hashmapu kljuc-vrijednost
				file.put("Description", "Secret key");
				file.put("Method", "AES");
				file.put("Secret key", key);
				file.put("Key length", Integer.toHexString(size.getBytesNum()));
				
				//zapisivanje u datoteku sa zadanim filepathom
				file.write(filepath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
		}	
		
		
		public static byte[] AESFileEncrypt(String filePath, Key key, byte[] initializationVector) {
			try {
				//procitati cisti tekst iz filea
				String fileEncrypt = readFile(filePath);
				byte[] dataEncryptSize = fileEncrypt.getBytes();		
				//sad idemo kriptirati			
				MessageCipher c;
				if(initializationVector != null){
					c = new OFB(initializationVector);
				}
				else{
					c = new ECB();
				}
				return c.encrypt(dataEncryptSize, key);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		
		public static void AESEncrypt(String filePath, String encryptedPath, String keyPath, String method) {
			try {
				//kriptiranje
				byte[] initializationVector = null;
				if (method == "OFB"){
					initializationVector = Utilities.generateRandomBlock(16);			
				}
				byte[] encryptedData = AESFileEncrypt(filePath, loadAESKey(keyPath), initializationVector);
				
				//sejvanje kriptiranog
				OS2CryptoFile cf_enc_file = new OS2CryptoFile();
				cf_enc_file.put("Description", "Crypted file");
				cf_enc_file.put("Method", "AES");
				cf_enc_file.put("File name", new File(filePath).getName());
				cf_enc_file.put("Data", base64Encode(encryptedData));
				if (method == "OFB")
					cf_enc_file.put("Initialization vector", Utilities.hexToString(initializationVector));
				
				cf_enc_file.write(encryptedPath);		
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		public static Key loadAESKey(String keyPath) {
			try {
				//load key info
				OS2CryptoFile key = new OS2CryptoFile(keyPath);				
				
				KeySize size = KeySize.AES_128;
				if (key.getJoined("Key length").equals("18")) {//vraæa vrijednost za taj kljuè u stringu
					size = KeySize.AES_192;
				}
				if (key.getJoined("Key length").equals("20")) {
					size = KeySize.AES_256;
				}
				//Get key itself 
				String keyData = key.getJoined("Secret key");

				return new Key(size, keyData);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public static void AESDecrypt(String encrypted, String plain, String keyPath, String method) {
			try {
				//proèitamo kriptirani file
				OS2CryptoFile encypted = new OS2CryptoFile(encrypted);
				byte[] encryptedData = base64Decode(encypted.getJoined("Data"));				
				//dohvatimo kljuè
				Key key = loadAESKey(keyPath);				
				//inicijaliziramo vektore ako postoje
				byte[] initializationVector = Utilities.stringToHex(encypted.getJoined("Initialization vector"));				
				//dekriptiranje
				MessageCipher c = (initializationVector != null) ? new OFB(initializationVector) : new ECB();
				String decryptedData = new String(c.decrypt(encryptedData, key));				
				//save
				BufferedWriter br = new BufferedWriter(new FileWriter(plain));
				br.write(decryptedData);
				br.close();				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public static void RSAKeyGenerate(String publicPath, String privatePath, int size) {
			try {				
				//generiranje para RSA kljuceva
				KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
				kpg.initialize(size);
				KeyPair kp = kpg.genKeyPair();
				KeyFactory fact = KeyFactory.getInstance("RSA");
				RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
				RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);				
				String modulus = pub.getModulus().toString(16);
				String publicExponent = pub.getPublicExponent().toString(16);
				String privateExponent = priv.getPrivateExponent().toString(16);				
				//izjednacavanje duljina stringa
				if (modulus.length() % 2 != 0) {
					modulus = "0" + modulus;
				}
				if (publicExponent.length() % 2 != 0) {
					publicExponent = "0" + publicExponent;
				}
				if (privateExponent.length() % 2 != 0) {
					privateExponent = "0" + privateExponent;				
				}
				//spremimo javni kljuc u file
				OS2CryptoFile cfPublic = new OS2CryptoFile();
				cfPublic.put("Description", "Public key");
				cfPublic.put("Method", "RSA");
				cfPublic.put("Key length", (Integer.toHexString(size).length() % 2 == 0 ? "" : "0") + Integer.toHexString(size));
				cfPublic.put("Modulus", modulus);
				cfPublic.put("Public exponent", publicExponent);
				cfPublic.write(publicPath);				
				//spremamo privatni kljuc u file
				OS2CryptoFile cf_private = new OS2CryptoFile();
				cf_private.put("Description", "Private key");
				cf_private.put("Method", "RSA");
				cf_private.put("Key length", (Integer.toHexString(size).length() % 2 == 0 ? "" : "0") + Integer.toHexString(size));
				cf_private.put("Modulus", modulus);
				cf_private.put("Private exponent", privateExponent);
				cf_private.write(privatePath);				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public static RSAPublicKey loadPublicKeyFromOS2CryptoFile(String path) {
			try {
				//idemo procitat info iz OS2CryptoFilea
				OS2CryptoFile cfPublic = new OS2CryptoFile(path);
				String publicExponent = cfPublic.getJoined("Public exponent");
				String modulus = cfPublic.getJoined("Modulus");
				
				//pretvorimo eksponent i mod u veliki integer
				BigInteger pE = new BigInteger(publicExponent, 16);
				BigInteger m = new BigInteger(modulus, 16);
				
				//stvaramo javni kljuc
				RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, pE);
				PublicKey pubKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
				
				return (RSAPublicKey)pubKey;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public static RSAPrivateKey loadPrivateKeyFromOS2CryptoFile(String path) {
			try {
				//citamo info iz OS2kripto fajla
				OS2CryptoFile cfPrivate = new OS2CryptoFile(path);
				String privateExponent = cfPrivate.getJoined("Private exponent");
				String modulus = cfPrivate.getJoined("Modulus");
				
				//pretvorimo eksponent i mod u veliki integer
				BigInteger pE = new BigInteger(privateExponent, 16);
				BigInteger m = new BigInteger(modulus, 16);
				
				//stvorimo privatni kljuc
				RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, pE);
				PrivateKey pubKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
				
				return (RSAPrivateKey)pubKey;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}		

		
				
		public static void generateDigitalEnvelope(String inputFilePath, String aesKey, String receiversPublicKeyPath, String envelopePath, String method) {
			try {
				//kriptiraj ulazni file
				byte[] initializationVector = null;
				if (method == "OFB") {
					initializationVector = Utilities.generateRandomBlock(16);
				}				
				Key symKey = loadAESKey(aesKey);
				byte[] encryptedInput = AESFileEncrypt(inputFilePath, symKey, initializationVector);
				
				//poruka omotnice
				String envelopeData = base64Encode(encryptedInput);
				
				//kriptiramo simetricni kljuc
				RSAPublicKey pk = loadPublicKeyFromOS2CryptoFile(receiversPublicKeyPath);
				
				javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("RSA");
				cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, pk);
				byte[] encSymKey = cipher.doFinal(symKey.getKey());
				
				String envelopeCryptKey = Utilities.hexToString(encSymKey);
				
				//Sspremamo omotnicu u file
				OS2CryptoFile cfEnv = new OS2CryptoFile();
				cfEnv.put("Description", "Envelope");
				cfEnv.put("File name", new File(inputFilePath).getName());
				cfEnv.put("Method", "AES");
				cfEnv.put("Method", "RSA");
				cfEnv.put("Key length", Integer.toHexString(symKey.getKeySize().getBytesNum()));	// AES
				String rsaKeySize = Integer.toHexString(pk.getModulus().bitLength());			// RSA
				cfEnv.put("Key length", (rsaKeySize.length() % 2 == 0 ? "" : "0") + rsaKeySize);
				cfEnv.put("Envelope data", envelopeData);
				cfEnv.put("Envelope crypt key", envelopeCryptKey);
				if (method == "OFB"){
					cfEnv.put("Initialization vector", Utilities.hexToString(initializationVector));
				}
				cfEnv.write(envelopePath);			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public static void openDigitalEnvelope(String envelopePath, String receiversPrivateKeyPath, String outputFilePath) {		
			try {
				//idemo ucitat podatke omotnice
				OS2CryptoFile cfEnv = new OS2CryptoFile(envelopePath);
				
				byte[] envelopeCryptKey = Utilities.stringToHex(cfEnv.getJoined("Envelope crypt key"));				
				//kriptirana poruka
				byte[] encOutput = base64Decode(cfEnv.getJoined("Envelope data"));
				
				//dekriptiramo s privatnim kljucem za dekriptiranje
				RSAPrivateKey pk = loadPrivateKeyFromOS2CryptoFile(receiversPrivateKeyPath);				
				javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("RSA");
				cipher.init(javax.crypto.Cipher.DECRYPT_MODE, pk);
				byte[] symKey = cipher.doFinal(envelopeCryptKey);
				
				//odredimo velicinu kljuca
				KeySize size = KeySize.AES_128;
				if (cfEnv.get("Key length").get(0).equals("18")) {
					size = KeySize.AES_192;
				}
				if (cfEnv.get("Key length").get(0).equals("20")) {
					size = KeySize.AES_256;
				}
				//konstruiramo kljuc za dekriptiranje
				Key key = new Key(size, symKey);				
				//ako postoji inicijalizacijski vektor uzmemo ga
				byte[] initializationVector = Utilities.stringToHex(cfEnv.getJoined("Initialization vector"));				
				//dekriptiramo poruku
				MessageCipher c;
				if(initializationVector != null) {
					c = new OFB(initializationVector);
				}
				else{
					c =  new ECB();
				}
				String output = new String(c.decrypt(encOutput, key));				
				//spremimo izlazne podatke
				BufferedWriter br = new BufferedWriter(new FileWriter(outputFilePath));
				br.write(output);
				br.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public static String SHADigest(String inPath, String outPath) {
			try {
				//racunanje hasha
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				String input = readFile(inPath);
				String hash =  Utilities.hexToString(md.digest(input.getBytes()));			
				
				//spremanje hasha
				BufferedWriter br = new BufferedWriter(new FileWriter(outPath));
				br.write(hash);
				br.close();
				
				return hash;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public static void generateDigitalSignature(String inputFilePath,String sendersPrivateKeyPath, String signaturePath) {
			try {
				//idemo napravit hash input filea
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				String input = readFile(inputFilePath);
				byte[] hash =  md.digest(input.getBytes());		
				
				//kriptiranje hasha
				RSAPrivateKey pk = loadPrivateKeyFromOS2CryptoFile(sendersPrivateKeyPath);				
				javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("RSA");
				cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, pk);
				byte[] encHash = cipher.doFinal(hash);				
				//potpis
				String envelope_signature = Utilities.hexToString(encHash);
				
				//spremanje potpisa u file
				OS2CryptoFile cfSig = new OS2CryptoFile();
				cfSig.put("Description", "Signature");
				cfSig.put("File name", new File(inputFilePath).getName());
				cfSig.put("Method", "SHA-1");
				cfSig.put("Method", "RSA");
				cfSig.put("Key length", "A0");	// SHA-1
				String rsaKeySize = Integer.toHexString(pk.getModulus().bitLength());			// RSA
				cfSig.put("Key length", (rsaKeySize.length() % 2 == 0 ? "" : "0") + rsaKeySize);
				cfSig.put("Signature", envelope_signature);
				cfSig.write(signaturePath);			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		public static Boolean validateDigitalSignature(String inputFilePath, String sendersPublicKeyPath, String signaturePath) {
			try {
				//hash ulaznog filea
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				String input = readFile(inputFilePath);
				byte[] hash =  md.digest(input.getBytes());				
				
				//ucitamo potpis
				OS2CryptoFile cfSig = new OS2CryptoFile(signaturePath);
				byte[] encHash = Utilities.stringToHex(cfSig.getJoined("Signature"));
				
				//dekriptiranje hasha
				RSAPublicKey pk = loadPublicKeyFromOS2CryptoFile(sendersPublicKeyPath);				
				javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("RSA");
				cipher.init(javax.crypto.Cipher.DECRYPT_MODE, pk);
				byte[] decHash = cipher.doFinal(encHash);
				
				return Utilities.hexToString(hash).equals(Utilities.hexToString(decHash));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		
		public static void generateDigitalStamp(String envelopePath, String signaturePath, String stampPath) {
			try {
				// digitalni pecat je digitalna omotnica + digitalni potpis
				OS2CryptoFile cfEnv = new OS2CryptoFile(envelopePath);
				OS2CryptoFile cfSig = new OS2CryptoFile(signaturePath);
				
				//potpis
				String signature = cfSig.getJoined("Signature");				
				cfEnv.get("Description").get(0).replaceAll("Envelope", "Stamp");
				cfEnv.put("Signature", signature);
				cfEnv.put("Method", "SHA-1");
				cfEnv.put("Key length", "A0");	// SHA-1 length
				cfEnv.write(stampPath);			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public static Boolean openDigitalStamp(String stampPath, String sendersPublicKeyPath, String receiversPrivateKeyPath, String outputFilePath) {		
			try {
				//ucitavanje podataka pecata
				OS2CryptoFile cfSt = new OS2CryptoFile(stampPath);		
				
				byte[] envelopeCryptKey = Utilities.stringToHex(cfSt.getJoined("Envelope crypt key"));
				
				//kriptirana poruka
				byte[] enc_output = base64Decode(cfSt.getJoined("Envelope data"));
				
				//dekriptiranje envelope keya za kriptiranje
				RSAPrivateKey pk = loadPrivateKeyFromOS2CryptoFile(receiversPrivateKeyPath);				
				javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("RSA");
				cipher.init(javax.crypto.Cipher.DECRYPT_MODE, pk);
				byte[] symKey = cipher.doFinal(envelopeCryptKey);
				
				//procitamo velicinu kljuca simetricnog
				KeySize size = KeySize.AES_128;
				if (cfSt.get("Key length").get(0).equals("18")){
					size = KeySize.AES_192;
				}
				if (cfSt.get("Key length").get(0).equals("20")){
					size = KeySize.AES_256;
				}
				//konstruiramo kljuc za dekripciju
				Key key = new Key(size, symKey);				
				//dohvatimo IV ako postoji
				byte[] initializationVector = Utilities.stringToHex(cfSt.getJoined("Initialization vector"));
				
				//dekriptiramo poruku
				MessageCipher c = (initializationVector != null) ? new OFB(initializationVector) : new ECB();
				byte[] byteOutput = c.decrypt(enc_output, key);
				String output = new String(byteOutput);
				
				//hash dekriptirane poruke
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				byte[] hash =  md.digest(byteOutput);
				
				//ucitamo potpis u obliku kriptiranog hasha
				byte[] encHash = Utilities.stringToHex(cfSt.getJoined("Signature"));
				
				//dekriptiramo hash
				RSAPublicKey prK = loadPublicKeyFromOS2CryptoFile(sendersPublicKeyPath);
				
				cipher.init(javax.crypto.Cipher.DECRYPT_MODE, prK);
				byte[] decHash = cipher.doFinal(encHash);	
				
				//ako je poruka valjana
				if (Utilities.hexToString(hash).equals(Utilities.hexToString(decHash)) ) {
					//spremimo izlaz
					BufferedWriter br = new BufferedWriter(new FileWriter(outputFilePath));
					br.write(output);
					br.close();
					return true;
				} else {
					return false;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
}
