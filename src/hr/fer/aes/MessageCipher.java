package hr.fer.aes;

public interface MessageCipher {
	
	//kriptiranje/dekriptiranje poruke, odnosno blokova teksta	
	public byte[] encrypt(byte[] data, Key key);
	public byte[] decrypt(byte[] data, Key key);	

}
