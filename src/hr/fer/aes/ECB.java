package hr.fer.aes;


public class ECB implements MessageCipher {

	@Override
	public byte[] encrypt(byte[] data, Key key) {
		// TODO Auto-generated method stub
		
		// nadopunjavanje bloka - PKCS5		
		int expandedDataSize = (16 - data.length % 16) + data.length;		
		byte[] expandedData = new byte[expandedDataSize];
		int additionalBytesNum = expandedData.length - data.length;
		
		for (int i = 0; i < data.length; i++) {
			expandedData[i] = data[i];
		}		
		//popunjavanje novododanih bajtova zadnjeg bloka s brojem dodanih bajtova
		for (int i = data.length; i < expandedData.length; i++)
			expandedData[i] = (byte) additionalBytesNum;
		
		//sad idemo kriptirati (ECB kriptira blok po blok - pošto koristimo aes kriptiraju se blokovi od 16 bajtova)
		byte[] encryptedData = new byte[expandedDataSize];
		int blocksNum = expandedData.length / 16;
		
		for (int i = 0; i < blocksNum; i++) {
			byte[] block = new byte[16];			
			for (int j = 0; j < 16; j++) {
				block[j] = expandedData[i*16 + j];
			}							
			block = AESBlockCipher.encrypt(block, key);
			
			for (int j = 0; j < 16; j++)
				encryptedData[i*16 + j] = block[j];
		}		
		return encryptedData;
	}

	@Override
	public byte[] decrypt(byte[] encryptedData, Key key) {
		// TODO Auto-generated method stub
		//dekriptiranje ECB - blok po blok
		byte[] decryptedData = new byte[encryptedData.length];
		int blocksNum = encryptedData.length / 16;
		
		for (int i = 0; i < blocksNum; i++) {			
			byte[] block = new byte[16];			
			for (int j = 0; j < 16; j++) {
				block[j] = encryptedData[i*16 + j];
			}							
			block = AESBlockCipher.decrypt(block, key);
			
			for (int j = 0; j < 16; j++){
				decryptedData[i*16 + j] = block[j];
			}
		}
		
		//Na kraju treba maknuti dodane bajtove
		int additionalBytesNum = decryptedData[decryptedData.length - 1];		
		byte data[] = new byte[decryptedData.length - additionalBytesNum];
		
		for (int i = 0; i < data.length; i++)
			data[i] = decryptedData[i];
		return data;
	}

}
