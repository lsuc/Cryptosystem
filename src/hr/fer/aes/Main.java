package hr.fer.aes;

import hr.fer.aes.Key.KeySize;
import hr.fer.crypto.Utilities;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	String stringData = "leaaaaaaaiiiiiiiiiiipikicaaaaaaaiiiiiiiisrdjaaaaaaaaniiiiiiiiiiiiivaaaaa";
	Key key = new Key(KeySize.AES_256, "603deb1015ca71be2b73aef0857d77811f352c073b6108d72d9810a30914dff4");
		
	byte[] byteData = stringData.getBytes();
	AESBlockCipher bc = new AESBlockCipher();		
	
	
	OFB ofb = new OFB(Utilities.generateRandomBlock(16));
	byte[] encryptedData = ofb.encrypt(byteData, key);			
	System.out.println("rezultat kriptiranja");
	bc.print(encryptedData);	
	
	byte[] decryptedData = ofb.decrypt(encryptedData, key);
	System.out.println("rezultat dekriptiranja");
	System.out.println( new String (decryptedData));

	}
}
