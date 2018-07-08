package model;

public enum BarcodeType {
	CODE39("Code 39"), CODE39FULLASCII("Code-39 Full ASCII"), UPCA("UPC-A");
	
    private String name;

	BarcodeType(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
