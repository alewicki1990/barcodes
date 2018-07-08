package view;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.BarcodeType;
import model.Scanner;

public class ViewController {

	Scanner scanner;

	@FXML
	private VBox menuPanelVBox;

	@FXML
	private ChoiceBox<BarcodeType> barcodeTypeChoiceBox;

	@FXML
	private Button chooseFileButton;

	@FXML
	private TextField filepathTextField;

	@FXML
	private TextField filenameTextField;

	@FXML
	private TextField minDiffBetweenBarsTextField;

	@FXML
	private TextField maxDiffBetweenBarsTextField;

	@FXML
	private Button getDataFromBarcodeButton;

	@FXML
	private TextField dataFullTextField;

	@FXML
	private TextField checksumTextField;

	@FXML
	private TextField dataPlainTextField;

	@FXML
	private ImageView fullImageImageView;

	@FXML
	private ImageView barcodeImageImageView;

	@FXML
	public void chooseFile(ActionEvent actionEvent) {

		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open File");
		chooser.getExtensionFilters()
				.addAll(new FileChooser.ExtensionFilter("Image Files", "*.bmp", "*.png", "*.jpg", "*.jpeg", "*.gif"));
		File file = chooser.showOpenDialog(new Stage());

		if (file != null) {
			menuPanelVBox.getChildren().filtered(node -> node instanceof TextField)
					.forEach(node -> ((TextField) node).setText(""));
			barcodeImageImageView.setImage(null);

			try {
				String imagepath = file.toURI().toURL().toString();
				Image fullImage = new Image(imagepath);
				scanner = new Scanner(fullImage);
				filepathTextField.setText(file.getPath());
				filenameTextField.setText(file.getName());
				adaptImageViewToImage(fullImageImageView, fullImage);
							
				barcodeTypeChoiceBox.setDisable(false);
				barcodeTypeChoiceBox.getItems().setAll(BarcodeType.values());
				barcodeTypeChoiceBox.setValue(BarcodeType.CODE39);
				
				minDiffBetweenBarsTextField.setDisable(false);
				minDiffBetweenBarsTextField.setText("2");
				
				maxDiffBetweenBarsTextField.setDisable(false);
				maxDiffBetweenBarsTextField.setText("3");
				
				getDataFromBarcodeButton.setDisable(false);
			} catch (IOException e) {
				e.printStackTrace();

				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Exception Info");
				alert.setHeaderText(e.getMessage());
				alert.setContentText("You cannot use this barcode scanner for decode this barcode!");
				alert.showAndWait();
			}
		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			//alert.setHeaderText("Please Select a File");
			alert.setContentText("You did not select a file!");
			alert.showAndWait();
		}
	}

	@FXML
	public void findAndTranslateBarcode(ActionEvent actionEvent) {

		scanner.setMinBarsWidthDiff(Double.parseDouble(minDiffBetweenBarsTextField.getText()));
		scanner.setMaxBarsWidthDiff(Double.parseDouble(maxDiffBetweenBarsTextField.getText()));

		adaptImageViewToImage(barcodeImageImageView, scanner.getBarcodeImage());
		dataFullTextField.setText(scanner.getTranslatedTextFromBarcode());
		checksumTextField.setText(Character.toString(scanner.getChecksum()));
		dataPlainTextField.setText(scanner.getPlainData());
	}

	public void adaptImageViewToImage(ImageView imageView, Image image) {
		imageView.setFitHeight(image.getHeight());
		imageView.setFitWidth(image.getWidth());
		imageView.setImage(image);
	}
}
