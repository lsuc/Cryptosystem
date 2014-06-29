package hr.fer.aes;

public class OFB implements MessageCipher {
	
	private byte[] initializationVector;	//IV
	
	public OFB(byte[] initializationVector) {
	
		if (initializationVector.length != 16){
			System.out.println("inicijalizacijski vektor mora biti velik 16 bajtova!");
			System.exit(1);
		}
		this.setInitializationVector(initializationVector);
	}

	@Override
	public byte[] encrypt(byte[] data, Key key) {
		// TODO Auto-generated method stub
		// nadopunjavanje bloka - PKCS5	
		byte[] initializationVector = this.initializationVector.clone();
		
		// proširivanje PKCS5
		int expandedDataSize = (16 - data.length % 16) + data.length;		
		byte[] expandedData = new byte[expandedDataSize];
		int additionalBytesNum = expandedData.length - data.length;
		
		for (int i = 0; i < data.length; i++) {
			expandedData[i] = data[i];
		}		
		//popunjavanje novododanih bajtova zadnjeg bloka s brojem dodanih bajtova
		for (int i = data.length; i < expandedData.length; i++) {
			expandedData[i] = (byte) additionalBytesNum;
		}
		
		//kriptiranje blok po blok pomoæu OFB
		byte[] encryptedData = new byte[expandedDataSize];
		int blocksNum = expandedData.length / 16;

		for (int i = 0; i < blocksNum; i++) {
			byte[] block = new byte[16];	
			
			block = AESBlockCipher.encrypt(initializationVector, key);
			
			for (int j = 0; j < 16; j++) {
				encryptedData[i*16 + j] = (byte) (block[j] ^ expandedData[i*16 + j]);
			}		
			initializationVector = block;
		}		
		return encryptedData;
	}

	@Override
	public byte[] decrypt(byte[] encryptedData, Key key) {
		byte[] initializationVector = this.initializationVector.clone();
		
		byte[] decryptedData = new byte[encryptedData.length];
		int blocksNum = encryptedData.length / 16;

		for (int i = 0; i < blocksNum; i++) {
			byte[] block = new byte[16];	
			
			block = AESBlockCipher.encrypt(initializationVector, key);
			
			for (int j = 0; j < 16; j++) {
				decryptedData[i*16 + j] = (byte) (block[j] ^ encryptedData[i*16 + j]);
			}		
			initializationVector = block;
		}	
		
		//Na kraju treba maknuti dodane bajtove
		int additionalBytesNum = decryptedData[decryptedData.length - 1];		
		byte data[] = new byte[decryptedData.length - additionalBytesNum];
		
		for (int i = 0; i < data.length; i++)
			data[i] = decryptedData[i];
		return data;
	}
	
	public void setInitializationVector(byte[] initializationVector) {
		this.initializationVector = initializationVector;
	}

	public byte[] getInitializationVector() {
		return initializationVector;
	}

}
