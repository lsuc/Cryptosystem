package hr.fer.aes;

import hr.fer.aes.Key.KeySize;

public class Key {

		
	private byte[] key;		//kljuè
	private KeySize keySize;
	
	public enum KeySize {	//moguæa velièina kljuèa u bajtovima - 16, 24, 32
		AES_128(16),
		AES_192(24),
		AES_256(32);
		
		private final int bytesNum;
		KeySize (int bytesNum){
			this.bytesNum = bytesNum;
		}
		public int getBytesNum() {
			return bytesNum;
		}
		public int getHexLength(){
			return getBytesNum()*2;		
		}		
	}
	
	public Key(KeySize size, String hexKey){ 
		if(size.getHexLength() != hexKey.length()){
			System.out.println("Uneseni kljuc u heksadekadskom obliku ima pogresnu duljinu!");
			System.exit(1);
		}		
		int keySize = size.getBytesNum();
		byte[] key = new byte[keySize];
		for (int i = 0; i < keySize; i++){
			key[i] = (byte)Integer.parseInt(hexKey.substring(i*2, i*2+2), 16);
		}
		setKey(key);
		setKeySize(size);
	}

	public Key(KeySize size, byte[] data) {
		if (data.length != size.getBytesNum()){
			System.out.println("Kriva duljina kljuca");
			System.exit(1);
		}		
		setKeySize(size);
		setKey(data);
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

	public byte[] getKey() {
		return key;
	}

	public void setKeySize(KeySize keySize) {
		this.keySize = keySize;
	}

	public KeySize getKeySize() {
		return keySize;
	}
	
}
