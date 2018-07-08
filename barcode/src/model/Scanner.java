package model;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Scanner {
	private Image fullImage;
	private Image barcodeImage;
	private double fullImageWidth;
	private double fullImageHeight;
	private int hFullImgBarcodeStartPoint;
	private int hFullImgBarcodeEndPoint;
	private int vFullImgBarcodeStartPoint;
	private int vFullImgBarcodeEndPoint;
	private int thinBarLenght;
	private int thickBarLenght;
	private int oneLetterPixelLength;
	private double minBarsWidthDiff;
	private double maxBarsWidthDiff;
	Barcode barcode;

	// double barcodeImageWidth;
	// double barcodeImageHeight;

	public Scanner(Image image) {
		this.fullImage = image;
		this.fullImageWidth = image.getWidth();
		this.fullImageHeight = image.getHeight();
		getBarcodePosition(image.getPixelReader());
		createBarecodeImage(image.getPixelReader());
		// this.barcodeImageHeight = this.barcodeImage.getHeight();
		// this.barcodeImageWidth = this.barcodeImage.getWidth();

		pullBarsWidths(barcodeImage.getPixelReader());
		
		// ---------------dodaj pozniej ta linie, do tworzenia malego barcode
		//checkIfBarsWidthsAreProper();
		
		String signal = deleteWhiteShortBarSeparatorFilter(decodeBars(barcodeImage.getPixelReader(), barcodeImage));
		barcode = new Barcode(signal);
	}

	public Image getBarcodeImage() {
		return this.barcodeImage;
	}

	public Image getFullImage() {
		return this.fullImage;
	}

	public String getTranslatedTextFromBarcode() {
		return barcode.getDecodedData();
	}

	private void getBarcodePosition(PixelReader pixelReader) throws IllegalArgumentException {

		//System.out.println(fullImageWidth + " " + fullImageHeight);

		for (int vPixelIndex = 0; vPixelIndex < fullImageHeight - 1; vPixelIndex++) {
			for (int hPixelIndex = 0; hPixelIndex < fullImageWidth - 1; hPixelIndex++) {
				if (isPixelDark(pixelReader, hPixelIndex, vPixelIndex)) {
					hFullImgBarcodeStartPoint = hPixelIndex;
					vFullImgBarcodeStartPoint = vPixelIndex;
					// System.out.println("fullImageWidth: " + fullImageWidth +
					// " fullImageHeight: " +fullImageHeight+ "
					// hFullImgBarcodeStartPoint: " + hFullImgBarcodeStartPoint
					// + " vFullImgBarcodeStartPoint: " +
					// vFullImgBarcodeStartPoint);

					hFullImgBarcodeEndPoint = getLastHorizontalDarkPixelIndex(pixelReader, hFullImgBarcodeStartPoint,
							(int) fullImageWidth, vPixelIndex);
					vFullImgBarcodeEndPoint = getBarsLastVerticalBlackPixelIndex(pixelReader, vFullImgBarcodeStartPoint,
							(int) fullImageHeight, hPixelIndex);
					// System.out.println("hFullImgBarcodeStartPoint: " +
					// hFullImgBarcodeStartPoint + ", vFullImgBarcodeStartPoint:
					// " + vFullImgBarcodeStartPoint);
					// System.out.println("hFullImgBarcodeEndPoint: " +
					// hFullImgBarcodeEndPoint + ", vFullImgBarcodeEndPoint: " +
					// vFullImgBarcodeEndPoint);
					return;
				}
			}
		}
		throw new IllegalArgumentException("No barcode");
	}

	private void createBarecodeImage(PixelReader pixelReader) {

		WritableImage writableImage = new WritableImage(pixelReader, hFullImgBarcodeStartPoint,
				vFullImgBarcodeStartPoint, (hFullImgBarcodeEndPoint - hFullImgBarcodeStartPoint) + 1,
				(vFullImgBarcodeEndPoint - vFullImgBarcodeStartPoint) + 1);

		PixelWriter pixelWriter = writableImage.getPixelWriter();

		/*
		 * for (int y = vFullImgBarcodeStartPoint; y < (vFullImgBarcodeEndPoint
		 * - vFullImgBarcodeStartPoint) + 1; y++) { for (int x =
		 * hFullImgBarcodeStartPoint; x < (hFullImgBarcodeEndPoint -
		 * hFullImgBarcodeStartPoint) + 1; x++) {
		 * System.out.println("metod createBarecodeImage::::: poziomo:" + x +
		 * " , pionowo: " + y); Color color = pixelReader.getColor(x, y);
		 * //color = color.brighter(); pixelWriter.setColor(x, y, color); } }
		 */

		for (int y = vFullImgBarcodeStartPoint; y < vFullImgBarcodeEndPoint + 1; y++) {
			for (int x = hFullImgBarcodeStartPoint; x < hFullImgBarcodeEndPoint + 1; x++) {
				// System.out.println("metod createBarecodeImage::::: poziomo:"
				// + x + " , pionowo: " + y);
				Color color = pixelReader.getColor(x, y);
				// color = color.brighter();
				pixelWriter.setColor(hFullImgBarcodeEndPoint - hFullImgBarcodeStartPoint,
						vFullImgBarcodeEndPoint - vFullImgBarcodeStartPoint, color);
			}
		}
		this.barcodeImage = writableImage;
	}

	private boolean isPixelDark(PixelReader imagePixelReader, int hPixelIndex, int vPixelIndex) {
		return (imagePixelReader.getColor(hPixelIndex, vPixelIndex).getBrightness() < 0.3);
	}

	private boolean isPixelBright(PixelReader imagePixelReader, int hPixelIndex, int vPixelIndex) {
		return (imagePixelReader.getColor(hPixelIndex, vPixelIndex).getBrightness() > 0.7);
	}

	private int getLastHorizontalDarkPixelIndex(PixelReader pixelReader, int startHPixelIndex, int stopHPixelIndex,
			int vPixelIndex) {
		int resultIndex = 0;
		for (int index = startHPixelIndex; index < stopHPixelIndex - 1; index++) {
			if (isPixelDark(pixelReader, index, vPixelIndex)) {
				// System.out.println("method
				// getLastHorizontalBlackPixelIndex:::: index:" + index + "
				// vPixelIndex: " + vPixelIndex);
				resultIndex = index;
			}
		}
		return resultIndex;
	}

	private String decodeBars(PixelReader pixelReader, Image image) {
		// int oneLetterPixelLength = (3 * thickBarLenght) + (6 *
		// thinBarLenght);
		// exception if there are more bars than needed
		// if ((image.getWidth() % oneLetterPixelLength) != 0)
		// throw new IllegalArgumentException("image.getWidth() :" +
		// image.getWidth() + " oneLetterPixelLength: "+oneLetterPixelLength);

		StringBuffer stringBuffer = new StringBuffer();

		int letterCounter = 0;
		int pixelCounter = 0;
		for (int index = 0; index < image.getWidth(); index++) {
			letterCounter++;

			if (isPixelDark(pixelReader, index, (int) image.getHeight() / 2)) {
				pixelCounter++;
				if ((index + 1) == image.getWidth()) {
					stringBuffer.append(getBarName(pixelCounter, "Black"));
					// System.out.println("method decodeBars:::: index: " +
					// index + " pixelCounter: " + pixelCounter +" Last Black");
					pixelCounter = 0;
				} else if (isPixelBright(pixelReader, index + 1, 0) & (!((letterCounter + 1) == oneLetterPixelLength))) {
					// System.out.println("method decodeBars:::: index: " +
					// index + " pixelCounter: " + pixelCounter +" Black");
					stringBuffer.append(getBarName(pixelCounter, "Black"));
					pixelCounter = 0;
				}
			} else if (isPixelBright(pixelReader, index, (int) image.getHeight() / 2)) {
				pixelCounter++;
				if ((index + 1) == image.getWidth()) {
					stringBuffer.append(getBarName(pixelCounter, "White"));
					// System.out.println("method decodeBars:::: index: " +
					// index + " pixelCounter: " + pixelCounter +" Last White");
					pixelCounter = 0;
				} else if (isPixelDark(pixelReader, index + 1, 0) & (!((letterCounter + 1) == oneLetterPixelLength))) {
					stringBuffer.append(getBarName(pixelCounter, "White"));
					// System.out.println("method decodeBars:::: index: " +
					// index + " pixelCounter: " + pixelCounter +" White");
					pixelCounter = 0;
				}
			}
			if ((letterCounter + 1) == oneLetterPixelLength) {
				oneLetterPixelLength = 0;
				// stringbuffer.deleteCharAt(stringbuffer.length()-1);
			}

		}
		System.out.println(stringBuffer.toString());
		return stringBuffer.toString();

	}

	private char getBarName(int barWidth, String color) throws IllegalArgumentException {

		if (barWidth == thickBarLenght & color.equals("Black"))
			return 'W';
		else if (barWidth == thinBarLenght & color.equals("Black"))
			return 'N';
		else if (barWidth == thickBarLenght & color.equals("White"))
			return 'w';
		else if (barWidth == thinBarLenght & color.equals("White"))
			return 'n';
		else
			throw new IllegalArgumentException("Bad bar width.");
	}

	private void pullBarsWidths(PixelReader pixelReader) {
		int index = 0;
		while (isPixelDark(pixelReader, index, 0))
			index++;

		thinBarLenght = index;
		System.out.println("ciekie: " + thinBarLenght);

		while (isPixelBright(pixelReader, index, 0))
			index++;

		thickBarLenght = index - thinBarLenght;
		System.out.println("grube: " + thickBarLenght);

		// System.out.println("Szerokosc:" + barcodeImage.getWidth() + "
		// Grubosc: " + barcodeImage.getHeight());
		oneLetterPixelLength = (3 * thickBarLenght) + (6 * thinBarLenght);
	}
	
	private void checkIfBarsWidthsAreProper() throws IllegalArgumentException {
		double realDiff =thickBarLenght/thinBarLenght;
		
		
		if(realDiff>maxBarsWidthDiff | realDiff<minBarsWidthDiff)
			throw new IllegalArgumentException("Bad bars width." );
	}
	
	private String deleteWhiteShortBarSeparatorFilter(String string) {
		StringBuilder stringWithoutSeparators = new StringBuilder();

		for (int i = 1; i < string.length() + 1; i++) {
			if (i % 10 == 0)
				i++;

			stringWithoutSeparators.append(string.charAt(i - 1));
		}
		return stringWithoutSeparators.toString();
	}

	private int getBarsLastVerticalBlackPixelIndex(PixelReader pixelReader, int startVPixelIndex, int stopVPixelIndex,
			int hPixelIndex) {
		int resultIndex = 0;
		for (int index = startVPixelIndex; index < stopVPixelIndex - 1; index++) {
			if (isPixelDark(pixelReader, hPixelIndex, index))
				resultIndex = index;
			else
				return resultIndex;
		}
		return resultIndex;
	}
	
	public char getChecksum(){
		return barcode.getChecksum();
	}
	
	public String getPlainData(){
		return barcode.getPlainDecodedData();
	}

	public void setMinBarsWidthDiff(double minBarsWidthDiff) {
		this.minBarsWidthDiff = minBarsWidthDiff;
	}

	public void setMaxBarsWidthDiff(double maxBarsWidthDiff) {
		this.maxBarsWidthDiff = maxBarsWidthDiff;
	}

	
	
	/*
	 * private int getBarsLastVerticalWhitePixelIndex(PixelReader pixelReader,
	 * int startVPixelIndex, int stopVPixelIndex, int hPixelIndex) { int
	 * resultIndex = 0; for (int index = startVPixelIndex; index <
	 * stopVPixelIndex - 1; index++) { if (isPixelBlack(pixelReader,
	 * hPixelIndex, index)) resultIndex = index; else return resultIndex; }
	 * return resultIndex; }
	 */
	
	
	

}