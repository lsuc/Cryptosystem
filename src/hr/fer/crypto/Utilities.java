package hr.fer.crypto;

import java.util.Random;

public class Utilities {
	
	public static byte[] stringToHex(String s)  {
		if (s == null)
			return null;		
		if (s.length() % 2 != 0) {
			System.out.println("Kriva velicina hex stringa!");
		}
		int bytesNum = s.length()/2;
		byte[] data = new byte[bytesNum];
		for (int i = 0; i < data.length; i++) {
			data[i] = (byte)Integer.parseInt(s.substring(i*2, i*2+2), 16);
		}
		return data;
	}

	public static String hexToString(byte[] hex) {
		String s = new String();
		for (int i = 0; i < hex.length; i++){
			if(Integer.toHexString(hex[i] & 0xFF).length() == 1){
				s += "0"+ Integer.toHexString(hex[i] & 0xFF);
			}
			else {
				s += Integer.toHexString(hex[i] & 0xFF);
			}			  
		}			
		return s;
	}
	
	public static byte[] generateRandomBlock(int size) {
		Random rg = new Random();
		byte[] data = new byte[size];		
		for (int i = 0; i < size; i++)
			data[i] = (byte) rg.nextInt(255);		
		return data;
	}
	
}
