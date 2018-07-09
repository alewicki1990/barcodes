package model;

import java.util.HashMap;


/**
 * @author alewicki
 *
 */
public class BarcodeCode39 {
	// W - long black
	// N - short black
	// w - long white
	// n - short white

	// 0AZ/ = NnNwWnWnNWnNnNwNnWNwWnWnNnNNwNwNnNwN

	private String uncodedData = "";
	private String decodedData = "";
	private String plainDecodedData = "";
	private char checksum;
	private final HashMap<String, Character> seriesSignTransl = new HashMap<String, Character>() {
		{
			put("NnNwWnWnN", '0');
			put("WnNwNnNnW", '1');
			put("NnWwNnNnW", '2');
			put("WnWwNnNnN", '3');
			put("NnNwWnNnW", '4');
			put("WnNwWnNnN", '5');
			put("NnWwWnNnN", '6');
			put("NnNwNnWnW", '7');
			put("WnNwNnWnN", '8');
			put("NnWwNnWnN", '9');
			put("WnNnNwNnW", 'A');
			put("NnWnNwNnW", 'B');
			put("WnWnNwNnN", 'C');
			put("NnNnWwNnW", 'D');
			put("WnNnWwNnN", 'E');
			put("NnWnWwNnN", 'F');
			put("NnNnNwWnW", 'G');
			put("WnNnNwWnN", 'H');
			put("NnWnNwWnN", 'I');
			put("NnNnWwWnN", 'J');
			put("WnNnNnNwW", 'K');
			put("NnWnNnNwW", 'L');
			put("WnWnNnNwN", 'M');
			put("NnNnWnNwW", 'N');
			put("WnNnWnNwN", 'O');
			put("NnWnWnNwN", 'P');
			put("NnNnNnWwW", 'Q');
			put("WnNnNnWwN", 'R');
			put("NnWnNnWwN", 'S');
			put("NnNnWnWwN", 'T');
			put("WwNnNnNnW", 'U');
			put("NwWnNnNnW", 'V');
			put("WwWnNnNnN", 'W');
			put("NwNnWnNnW", 'X');
			put("WwNnWnNnN", 'Y');
			put("NwWnWnNnN", 'Z');
			put("NwNnNnWnW", '-');
			put("WwNnNnWnN", '.');
			put("NwWnNnWnN", ' ');
			put("NwNwNwNnN", '$');
			put("NwNwNnNwN", '/');
			put("NwNnNwNwN", '+');
			put("NnNwNwNwN", '%');
			put("NwNnWnWnN", '*');
		}
	};
	public final HashMap<String, Integer> seriesValueTransl = new HashMap<String, Integer>() {
		{
			put("NnNwWnWnN", 0);
			put("WnNwNnNnW", 1);
			put("NnWwNnNnW", 2);
			put("WnWwNnNnN", 3);
			put("NnNwWnNnW", 4);
			put("WnNwWnNnN", 5);
			put("NnWwWnNnN", 6);
			put("NnNwNnWnW", 7);
			put("WnNwNnWnN", 8);
			put("NnWwNnWnN", 9);
			put("WnNnNwNnW", 10);
			put("NnWnNwNnW", 11);
			put("WnWnNwNnN", 12);
			put("NnNnWwNnW", 13);
			put("WnNnWwNnN", 14);
			put("NnWnWwNnN", 15);
			put("NnNnNwWnW", 16);
			put("WnNnNwWnN", 17);
			put("NnWnNwWnN", 18);
			put("NnNnWwWnN", 19);
			put("WnNnNnNwW", 20);
			put("NnWnNnNwW", 21);
			put("WnWnNnNwN", 22);
			put("NnNnWnNwW", 23);
			put("WnNnWnNwN", 24);
			put("NnWnWnNwN", 25);
			put("NnNnNnWwW", 26);
			put("WnNnNnWwN", 27);
			put("NnWnNnWwN", 28);
			put("NnNnWnWwN", 29);
			put("WwNnNnNnW", 30);
			put("NwWnNnNnW", 31);
			put("WwWnNnNnN", 32);
			put("NwNnWnNnW", 33);
			put("WwNnWnNnN", 34);
			put("NwWnWnNnN", 35);
			put("NwNnNnWnW", 36);
			put("WwNnNnWnN", 37);
			put("NwWnNnWnN", 38);
			put("NwNwNwNnN", 39);
			put("NwNwNnNwN", 40);
			put("NwNnNwNwN", 41);
			put("NnNwNwNwN", 42);
		}
	};

	public final HashMap<Integer, String> valueSeriesTransl = new HashMap<Integer, String>() {
		{
			put(0, "NnNwWnWnN");
			put(1, "WnNwNnNnW");
			put(2, "NnWwNnNnW");
			put(3, "WnWwNnNnN");
			put(4, "NnNwWnNnW");
			put(5, "WnNwWnNnN");
			put(6, "NnWwWnNnN");
			put(7, "NnNwNnWnW");
			put(8, "WnNwNnWnN");
			put(9, "NnWwNnWnN");
			put(10, "WnNnNwNnW");
			put(11, "NnWnNwNnW");
			put(12, "WnWnNwNnN");
			put(13, "NnNnWwNnW");
			put(14, "WnNnWwNnN");
			put(15, "NnWnWwNnN");
			put(16, "NnNnNwWnW");
			put(17, "WnNnNwWnN");
			put(18, "NnWnNwWnN");
			put(19, "NnNnWwWnN");
			put(20, "WnNnNnNwW");
			put(21, "NnWnNnNwW");
			put(22, "WnWnNnNwN");
			put(22, "NnNnWnNwW");
			put(23, "WnNnWnNwN");
			put(24, "NnWnWnNwN");
			put(25, "NnNnNnWwW");
			put(26, "WnNnNnWwN");
			put(27, "NnWnNnWwN");
			put(28, "NnNnWnWwN");
			put(29, "WwNnNnNnW");
			put(30, "NwWnNnNnW");
			put(31, "WwWnNnNnN");
			put(32, "NwNnWnNnW");
			put(33, "WwNnWnNnN");
			put(34, "NwWnWnNnN");
			put(35, "NwNnNnWnW");
			put(36, "WwNnNnWnN");
			put(37, "NwWnNnWnN");
			put(38, "NwNwNwNnN");
			put(39, "NwNwNnNwN");
			put(40, "NwNnNwNwN");
			put(41, "NnNwNwNwN");
		}
	};

	/**
	 * Constructor.
	 * 
	 * @author alewicki
	 * @param uncodedData input data which is undecoded series of signs from scanner 
	 * @since 23-06-2018
	 * @version 1.0
	 * @exception IllegalArgumentException
	 */
	public BarcodeCode39(String uncodedData) throws IllegalArgumentException{
		//countChars();
		checkIfStringHasTwoAster(uncodedData);
		checkIfChecksumIsRight(uncodedData);


		this.uncodedData = uncodedData;
		this.decodedData = translateData(uncodedData);
		this.plainDecodedData = pullPlainData(this.decodedData);
		//checkIfPlainDataHasAnyAster();
		

	}

	public String getUncodedData() {
		return uncodedData;
	}

	public String getDecodedData() {
		return decodedData;
	}

	public String getPlainDecodedData() {
		return plainDecodedData;
	}
	
	

/*	public void countChars() {
		for (Entry<String, Integer> entry : seriesValueTransl.entrySet()) {
			String key = entry.getKey();
			int countFat = 0;
			int countThin = 0;
			for (char c : key.toCharArray()) {
				if (c == 'w' || c == 'W') {
					countFat++;
				} else if (c == 'n' || c == 'N') {
					countThin++;
				}
			}
			System.out.println("Grube: " + countFat + "Cienkie: " + countThin);
		}
	} */

	public char getChecksum() {
		return checksum;
	}

	public void checkIfChecksumIsRight(String uncodedData) {
		int checksumValue = pullReceivedChecksumValue(uncodedData);
		int sum = 0;
		int result = 0;

		for (int i = 9; i < uncodedData.length() - 18; i += 9) 
			sum += seriesValueTransl.get(uncodedData.substring(i, i + 9));;

		result = sum % 43;

		if (result != checksumValue)
			throw new IllegalArgumentException("Checksum is not proper!") ;
		else
			checksum=pullReceivedAndTranslatedChecksum(uncodedData);
	}

/*	private void checkIfPlainDataHasAnyAster(String uncodedData) throws IllegalArgumentException {
		int counter = 0;

		for (int i = 9; i < uncodedData.length() - 19; i += 9) {
			char ch = seriesSignTransl.get(uncodedData.substring(i, i + 9));
			if (ch == '*')
				counter++;
			if (counter > 1)
				return;
		}

		throw new IllegalArgumentException("Barecode has more than two asterisk. This is very serious issue!");
	}*/

	private void checkIfStringHasTwoAster(String uncodedData) {
		boolean firstSignIsAsterExist = seriesSignTransl.get(uncodedData.substring(0, 9)) == '*';
		boolean secondSignIsAsterExist = seriesSignTransl.get(uncodedData.substring(uncodedData.length() - 9)) == '*';

		if (!(firstSignIsAsterExist & secondSignIsAsterExist))
			throw new IllegalArgumentException("Barecode doesn't have asterisks at end and beginning.");
	}

	private String pullPlainData(String dataWithAdditions) {
		return dataWithAdditions.substring(1, dataWithAdditions.length() - 2);
	}

	private int pullReceivedChecksumValue(String uncodedData) {
		return seriesValueTransl.get(pullReceivedChecksumUncodedDataString(uncodedData));
	}
	
	private String pullReceivedChecksumUncodedDataString(String uncodedData) {
		return uncodedData.substring(uncodedData.length() - 18, uncodedData.length() - 9);
	}

	private char pullReceivedAndTranslatedChecksum(String uncodedData) {
		return seriesSignTransl.get(uncodedData.substring(uncodedData.length() - 18, uncodedData.length() - 9));
	}
	
	
	private String translateData(String uncodedData) {
		StringBuilder decodedData = new StringBuilder();

		for (int i = 0; i < uncodedData.length() - 1; i += 9)
			decodedData.append(seriesSignTransl.get(uncodedData.substring(i, i + 9)));

		return decodedData.toString();
	}
}