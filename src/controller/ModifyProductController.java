package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static model.Inventory.searchPartsName;



/**Controller for Modify Product screen*/
public class ModifyProductController implements Initializable{

    int productIndex;

    @FXML
    private TextField modProductId;
    @FXML
    private TableView partsTable;
    @FXML
    private TableColumn partIdCol;
    @FXML
    private TableColumn partNameCol;
    @FXML
    private TableColumn partInvCol;
    @FXML
    private TableColumn partPriceCol;


    @FXML
    private TableView assocProductTable;
    @FXML
    private TableColumn assocPartIdCol;
    @FXML
    private TableColumn assocPartNameCol;
    @FXML
    private TableColumn assocPartInvCol;
    @FXML
    private TableColumn assocPartPriceCol;


    @FXML
    private TextField modProductName;
    @FXML
    private TextField modProductInv;
    @FXML
    private TextField modProductPrice;
    @FXML
    private TextField modProductMin;
    @FXML
    private TextField modProductMax;
    @FXML
    private TextField queryPart;


    @FXML
    private Button addAssoc;
    @FXML
    private Button removeAssoc;
    @FXML
    private Button saveProduct;
    @FXML
    private Button partsSearch;




    ObservableList<Part> allPartsTable = FXCollections.observableArrayList();
    ObservableList<Part> assocPartsTable = FXCollections.observableArrayList();

/**Initiates data from main screen for selected product*/
    public void init(Product selectedProduct) {
        assocPartsTable.addAll(selectedProduct.getAllAssociatedParts());
        assocProductTable.setItems(assocPartsTable);


        //Set text fields
        modProductId.setText(Integer.toString(selectedProduct.getProductId()));
        modProductName.setText(selectedProduct.getProductName());
        modProductInv.setText(Integer.toString(selectedProduct.getProductInv()));
        modProductPrice.setText(Double.toString(selectedProduct.getProductPrice()));
        modProductMin.setText(Integer.toString(selectedProduct.getMin()));
        modProductMax.setText(Integer.toString(selectedProduct.getMax()));
    }



    /**Replaces Product on Main Screen with new product*/
    public void onSave(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(modProductId.getText());
            String name = modProductName.getText();
            int stock = Integer.parseInt(modProductInv.getText());
            double price = Double.parseDouble(modProductPrice.getText());
            int min = Integer.parseInt(modProductMin.getText());
            int max = Integer.parseInt(modProductMax.getText());


            if (min > max) /**Checks minimum is not greater than maximum*/
                throw new Exception("minGreater");
            if (min == max) /**Checks the min and max are not equal*/
                throw new Exception("maxMin");
            if (stock > max || stock < min) /**Checks that inv is less than max and greater than min*/
                throw new Exception("inventory");

            Product newProduct = new Product(id, name, stock, price, min, max);
            for(int i = 0; i < assocPartsTable.size(); i++){
                newProduct.addAssociatedParts(assocPartsTable.get(i));
            }
            Inventory.modifyProduct(productIndex, newProduct);
            goToMain(actionEvent);
        }
        catch (Exception exception) {
            if (exception.getMessage().equals("minGreater")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Minimum is greater than maximum");
                alert.showAndWait();
            } else if (exception.getMessage().equals("inventory")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Inventory must be between minimum and maximum");
                alert.showAndWait();
            }
            else if (exception.getMessage().equals("maxMin")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Minimum must be smaller than maximum");
                alert.showAndWait();
            }
        }
    }


    /**Adds part to associated part list*/
    public void onAdd(ActionEvent actionEvent) throws IOException{
        Part sp = (Part) partsTable.getSelectionModel().getSelectedItem();
        if(sp != null && !assocPartsTable.contains(sp)){
            assocPartsTable.add(sp);
            assocProductTable.setItems(assocPartsTable);
        }
    }

    /**Removes part from associated part list*/
    public void onRemove(ActionEvent actionEvent) throws IOException{
        Part sp = (Part)assocProductTable.getSelectionModel().getSelectedItem();
        if(sp == null)
            return;
        assocPartsTable.remove(sp);
        assocProductTable.setItems(assocPartsTable);
    }




    /**Cancel Button returns to Main*/
    public void toMain(ActionEvent actionEvent) throws IOException{
        goToMain(actionEvent);
    }

/**Used to return to main screen*/
    void goToMain(ActionEvent actionEvent) throws IOException{
        Parent ModifyProductCancel = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(ModifyProductCancel, 850, 430);
        stage.setTitle("Back to Main Screen");
        stage.setScene(scene);
        stage.show();
    }

/**Initializes Part table from main screen to Modify Product screen
 * Initializes associated parts of product to associated parts table
 * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partsTable.setItems(allPartsTable);
        partsTable.setItems(Inventory.getAllParts());


        assocPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        assocProductTable.setItems(assocPartsTable);
    }



    /**Searches Parts table*/
    @FXML
    private void searchByPartsAction(ActionEvent actionEvent) {
        String text = queryPart.getText();
        ObservableList<Part> parts = searchPartsName(text);

        try {
            int id = Integer.parseInt(text);
            ObservableList<Part> parts1 = FXCollections.observableArrayList();
            parts1.add(Inventory.searchParts(id));
            if(parts1.get(0) != null)
                partsTable.setItems(parts1);
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Not Found");
                alert.setContentText("That item does not exist");
                alert.showAndWait();
            }
            return;
        }
        catch (Exception e){}
        if (Inventory.searchPartsName(text).size() == 0){
            queryPart.setText("");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Not Found");
            alert.setContentText("That item does not exist");
            alert.showAndWait();
        }
        else{
            partsTable.setItems(Inventory.searchPartsName(text));
        }
    }
}