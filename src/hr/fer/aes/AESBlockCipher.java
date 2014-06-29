package hr.fer.aes;

import hr.fer.aes.Key.KeySize;

public class AESBlockCipher {
	
	public static void rotateCols(byte[] col) {
		byte b = col[0];
		for (int i = 0; i < 3; i++){
			col[i] = col[i+1];
		}			
		col[3] = b;		
	}
	
	//djeluje nad stupcem proširenog kljuèa	
	public static void keyScheduleCore(byte[] col, int iter){ 
		rotateCols(col);
		for(int i = 0; i < 4; i++){
			col[i] = Tables.getSBoxValue(col[i]);
		}		
		col[0] = (byte)(col[0] ^ Tables.getRconValue(iter));
	}


	public static byte[] expandKey(Key key){
		
		int currentSize = 0;		//trenutna velièina proširenog kljuèa
		int expandedSize = 0;		//velièina proširenog kljuèa
		byte[] expandedKey;			//prošireni kljuè koji æemo kasnije stvoriti
		int iter = 1;				//trenutna rcon iteracija		
		byte[] word = new byte[4];	//pomoæna rijeè
		
		//sad idemo odrediti velièinu proširenog kljuèa u bajtovima			
		switch (key.getKeySize()) {
			case AES_128:
				expandedSize = 16 * (10 + 1); break; 
			case AES_192:
				expandedSize = 16 * (12 + 1); break;			
			case AES_256:
				expandedSize = 16 * (14 + 1); break;
		}
		//...i alocirati memoriju za prošireni kljuè
		expandedKey = new byte[expandedSize];
		
		//Prvih n stupaca(rijeèi) proširenog kljuèa dobije se kopiranjem izvornog kljuèa		
		for(int i = 0; i < key.getKeySize().getBytesNum(); i++){
			expandedKey[i] = key.getKey()[i];			
		}
		currentSize = key.getKeySize().getBytesNum();
		
		while(currentSize < expandedSize){

			//Varijabli word pridružimo rijeè s indeksom zadnje stvorene rijeèi prošlog kljuèa
			for (int i = 0; i < 4; i++){
				word[i] = expandedKey[(currentSize - 4) + i];
			}
			
			//Svakih 16, 24 ili 32 okteta (ovisno o vel. kljuèa) primjenimo keyScheduleCore i poveæamo broj iteracija
			if (currentSize % key.getKeySize().getBytesNum() == 0){
				keyScheduleCore(word, iter);
				iter = iter + 1;
			}
			
			//Za 256-bitne kljuèeve imamo još jednu operaciju zamjene iz s-kutije, ako n == AES_256 i (trVel mod n == 16):
			if ((key.getKeySize() == KeySize.AES_256) && (currentSize % key.getKeySize().getBytesNum() == 16))
				for(int i = 0; i < 4; i++){
					word[i] = Tables.getSBoxValue(word[i]);
				}
			
			//Primijenimo operaciju iskljuèivo ili nad rijeèi t i rijeèi s indeksom k - (n - 1)
			//Rezultat je rijeè kojom proširujemo kljuè
			for(int i = 0; i < 4; i++) {
				expandedKey[currentSize] = (byte) (expandedKey[currentSize - key.getKeySize().getBytesNum()] ^ word[i]);
				currentSize++;
			}
		}	 			
		return expandedKey;		
	}

	//dodaj potkljuè	
	public static void addSubKey(byte[] data, byte[] subKey, int startIndex){
		for(int i = 0; i < 16; i++){
			data[i] = (byte) (data[i] ^ subKey[i+startIndex]);
		}
	}
	//zamijeni oktete
	public static void subBytes (byte[] data){
		for(int i = 0; i < 16; i++){ 			
			data[i] = Tables.getSBoxValue(data[i]);
			}		
	}
	
	//posmakni redove - 1. ostaje isti, drugi se rotira za 1 ulijevo, treæi za 2 i 4. za 3
	public static void shiftRows(byte[] data) {
		for (int i = 1; i < 4; i++){
			for (int j = 0; j < i; j++){
				shiftRow(data, i);
			}
		}			
	}
	
	//posmicanje samo jednog retka ulijevo
	public static void shiftRow(byte[] row, int startIndex) {
		byte b = row[startIndex];
		for (int i = 0; i < 3; i++){
			row[startIndex+i*4] = row[startIndex+(i+1)*4];
		}
		row[3*4+startIndex] = b;
	}
	public static byte xputa(byte b) {
		//zastavica koja govori kakav je 8. bit, jel 1 il 0, tako da shifta za 7 mjesta udesno. ako je 1 -> preljev
		int z = (b & 0xFF) >> 7;
		byte rez = (byte) (b << 1); //množenje s 2 je pomak ulijevo 		
		//ako je došlo preljeva obavi ovu operaciju da rezultat ostane u GF(2^8) polju. 
		if (z == 1){
			rez = (byte) (rez ^ 0x1b);
		}				
		return rez;
	}
	
	
	public static void mixCol(byte[] col, int startIndex) {
		byte[] a = new byte[4];	//Kopija ulaznog stupca 
		byte[] b = new byte[4];	// Svaki element iz a pomnožen s 2 u GF(2^8)
		
		for (int i = 0; i < 4; i++) {
			a[i] = col[i+startIndex];
			b[i] = xputa(a[i]);
		}
		
		col[0+startIndex] = (byte) (b[0] ^ a[3] ^ a[2] ^ b[1] ^ a[1]);
		col[1+startIndex] = (byte) (b[1] ^ a[0] ^ a[3] ^ b[2] ^ a[2]);
		col[2+startIndex] = (byte) (b[2] ^ a[1] ^ a[0] ^ b[3] ^ a[3]);
		col[3+startIndex] = (byte) (b[3] ^ a[2] ^ a[1] ^ b[0] ^ a[0]);
	}
	
	
	public static void mixCols(byte[] data) {
		for (int i = 0; i < 4; i++) {
			mixCol(data, i*4);
		}
	}
	
	public static byte[] encrypt(byte[] data, Key key)  {
		if (data.length != 16){
			System.out.println("Duljina bloka podataka neispravna!");
			System.exit(1);	
		}
		byte[] state = data.clone();
		int roundsNum = 0;
		int currentRound = 0;
		
		switch (key.getKeySize()) {
			case AES_128:
				roundsNum = 10; break;
			case AES_192:
				roundsNum = 12; break;			
			case AES_256:
				roundsNum = 14; break;
		}
		
		//1. korak proširenje kljuèa kriptiranja
		byte[] expandedKey = expandKey(key);
		
		//2. korak - inicijalna runda (ne broji se kao runda) - dodavanje piotkljuèa
		addSubKey(state, expandedKey, currentRound*16);
		//print(state);

		//3. korak - ponavljaj brojRundi-1 puta
		//		a) Zamijeni oktete
		//		b) Posmakni redove
		//		c) Pomiješaj stupce
		//		d) Dodaj potkljuè
		for (currentRound = 1; currentRound <= roundsNum - 1; currentRound++) {
			subBytes(state);
			//print(state);
			shiftRows(state);
			//print(state);
			mixCols(state);
			addSubKey(state, expandedKey, currentRound*16);
		}
		
		//4. korak - posljednja runda, nema miješanja stupaca
		subBytes(state);
		//print(state);
		shiftRows(state);
		//print(state);
		addSubKey(state, expandedKey, currentRound*16);
		//print(state);

		return state;
	}
	
	public static void print(byte[] s) {
		for (int i = 0; i < s.length; i++)
			System.out.printf("%02x", s[i]);
		System.out.println();
	}
	
	//DEKRIPTIRANJE!
	public static void addSubKeyInv(byte[] data, byte[] subKey, int startIndex) {
		addSubKey(data, subKey, startIndex);
	}
	
	
	//posmicanje samo jednog retka udesno
	public static void shiftRowInv(byte[] row, int startIndex) {
		byte b = row[startIndex+3*4];
		for (int i = 3; i > 0; i--){
			row[startIndex+i*4] = row[startIndex+(i-1)*4];
		}			
		row[startIndex] = b;
	}
	
	public static void shiftRowsInv(byte[] data) {
		for (int i = 1; i < 4; i++)
			for (int j = 0; j < i; j++)
				shiftRowInv(data, i);
	}
	//zamijeni oktete inverz
	public static void subBytesInv (byte[] data){
		for(int i = 0; i < 16; i++){ 			
			data[i] = Tables.getSBoxInv(data[i]);
		}		
	}
	
	public static void mixColInv(byte[] col, int startIndex) {
		byte[] a = new byte[4];	
		byte[] x02 = new byte[4];	
		byte[] x04 = new byte[4];
		byte[] x09 = new byte[4];
		
		for (int i = 0; i < 4; i++) {
			a[i] = col[i + startIndex];
			x02[i] = xputa(a[i]);
			x04[i] = xputa(x02[i]);
			x09[i] = (byte) (xputa(x04[i]) ^ a[i]);
		}
		byte b = (byte) (x09[0] ^ x09[1] ^ x09[2] ^ x09[3]);
		
		col[0+startIndex] = (byte) (x04[2] ^ x04[0] ^ b ^ a[0] ^ x02[1] ^ x02[0]);
		col[1+startIndex] = (byte) (x04[3] ^ x04[1] ^ b ^ a[1] ^ x02[2] ^ x02[1]);
		col[2+startIndex] = (byte) (x04[2] ^ x04[0] ^ b ^ a[2] ^ x02[3] ^ x02[2]);
		col[3+startIndex] = (byte) (x04[3] ^ x04[1] ^ b ^ a[3] ^ x02[0] ^ x02[3]);
	}
	
	
	public static void mixColsInv(byte[] data) {
		for (int i = 0; i < 4; i++) {
			mixColInv(data, i*4);
		}
	}
	
	public static byte[] decrypt(byte[] data, Key key) {
		
		if (data.length != 16){			
			System.out.println("Duljina bloka podataka neispravna!");
			System.exit(1);
		}		
		int roundsNum = 0;
		int currentRound = 0;
		byte[] state = data.clone();
		
		switch (key.getKeySize()) {
			case AES_128:
				roundsNum = 10; break;
			case AES_192:
				roundsNum = 12; break;			
			case AES_256:
				roundsNum = 14; break;
		}		
		currentRound = roundsNum;		
		byte[] expandedKey = expandKey(key);

		addSubKeyInv(state, expandedKey, currentRound*16);
		
		for (currentRound--; currentRound > 0; currentRound--) {
			shiftRowsInv(state); 
			subBytesInv(state); 
			addSubKeyInv(state, expandedKey, currentRound*16); 
			mixColsInv(state);
		}
		
		shiftRowsInv(state);
		subBytesInv(state);
		addSubKeyInv(state, expandedKey, currentRound*16);

		return state;
	}
}



