package model;

abstract class Barcode {
	abstract public String getDecodedData();
	abstract public char getChecksum();
	abstract public String getPlainDecodedData();
}
