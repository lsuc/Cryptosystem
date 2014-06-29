package hr.fer.crypto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//pomocne funkcije koje uzimaju u obzir format datoteka napravljenih za OS2 predmet
public class OS2CryptoFile {
	
	private HashMap<String, ArrayList<String>> fileData;
	private final String NEW_LINE = System.getProperty("line.separator");
	
	public OS2CryptoFile() {
		fileData = new HashMap<String, ArrayList<String>>();
	}
	
	public OS2CryptoFile(String path) throws IOException {
		fileData = new HashMap<String, ArrayList<String>>();		
		read(path);
	}
	
	public void read(String path) throws IOException {
		//idemo otvoriti file
		BufferedReader br = new BufferedReader(new FileReader(path));
		
		String line;
		String key = null;
		Boolean readingRelevant = false, readValue = false;
		
		//sad idemo citati liniju po liniju
		while((line = br.readLine()) != null) {
			
			//pocetak relevantnih podataka 
			if ( !readingRelevant && line.startsWith("---BEGIN OS2 CRYPTO DATA---")) {
				readingRelevant = true;
				continue;
			}			
			//kraj relevantnih podataka
			if (line.startsWith("---END OS2 CRYPTO DATA---")){
				break;
			}
			
			//prazna linija
			if (line.length() < 1){
				continue;
			}
			
			//kljuc u hashmapi
			if (line.endsWith(":")) {
				//moramo prvo ukloniti dvotoèku
				key = line.substring(0, line.length() - 1);
				
				//...pa tek onda ubaciti kljuè u hashmapu
				fileData.put(key, new ArrayList<String>());

				//slijedi vrijednost iza : koju æemo ubaciti kao string u hashmapu
				readValue = true;
				continue;
			}
			
			//vrijednost kljuèa hashmape
			if (readValue) {
				if (line.startsWith("    ")) {	//moraju biti razmaci na poèetku vrijednosti
					//prvo uklonimo te razmake (njih 4)
					String valueData = line.substring(4, line.length());					
					//i sad tek ubacimo vrijednost kljuèa
					fileData.get(key).add(valueData);
				} else {	//inaèe se ne radi o liniji u kojoj su vrijednosti
					readValue = false;
				}
			}
		}
	}	
	public void write(String path) throws IOException {
		//otvorimo/stvaramo file
		BufferedWriter br = new BufferedWriter(new FileWriter(path));

		// na pocetku pisemo
		br.write("---BEGIN OS2 CRYPTO DATA---" + NEW_LINE);
		
		//upisemo kljuceve i povezane vrijednosti u odredjenom formatu
		for (Map.Entry<String, ArrayList<String>> entry : fileData.entrySet()) {
			br.write(entry.getKey() + ":" + NEW_LINE);
			
			for (String valueData : entry.getValue()) {
				br.write("    " + valueData + NEW_LINE);
			}
			
			br.write(NEW_LINE);
		}
		//kraj datoteke
		br.write("---END OS2 CRYPTO DATA---" + NEW_LINE);
		br.close();
	}
	
	//spojimo polje stringova koje predstavlja vrijednost kljuca u jedan string
	public String getJoined(String key) {
		if (fileData.get(key) == null){
			return null;		
		}
		String s = new String();
		
		for (String valueData : fileData.get(key))
			s += valueData;
		
		return s;
	}	

	
	public void put(String key, ArrayList<String> value) {
		if (fileData.get(key) == null){
			fileData.put(key, new ArrayList<String>());
		}
		fileData.get(key).addAll(value);
	}
	
	public void put(String key, String value) {
		ArrayList<String> arrayedValue = new ArrayList<String>();
		
		//podijeli se vrijednost u substringove duljine maximalno 60 znakova
		Collections.addAll(arrayedValue, value.split("(?<=\\G.{60})"));
		put(key, arrayedValue);
	}
	public ArrayList<String> get(String key) {
		return fileData.get(key);
	}
}
