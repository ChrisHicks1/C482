package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static model.Inventory.searchPartsName;


/**Controller for Add Products screen*/
public class AddProductController implements Initializable{
    @FXML
    private Button addAssoc;
    @FXML
    private Button removeAssoc;
    @FXML
    private Button saveProduct;
    @FXML
    private TextField queryPart;
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
    private TableView associatedProductTable;
    @FXML
    private TableColumn assocPartIdCol;
    @FXML
    private TableColumn assocPartNameCol;
    @FXML
    private TableColumn assocPartInvCol;
    @FXML
    private TableColumn assocPartPriceCol;


    @FXML
    private Button partsSearch;
    @FXML
    private TextField addProductName;
    @FXML
    private TextField addProductInv;
    @FXML
    private TextField addProductPrice;
    @FXML
    private TextField addProductMin;
    @FXML
    private TextField addProductMax;



     ObservableList<Part> allPartsTable = FXCollections.observableArrayList();
     ObservableList<Part> assocPartsTable = FXCollections.observableArrayList();



    /**Adds part to associated part list
     *
     * FUTURE ENHANCEMENT would stop user from adding parts to the Associated Parts table if the collective price of the parts exceeded
     * the price of the product*/
    public void onAdd(ActionEvent actionEvent) throws IOException{
        Part sp = (Part) partsTable.getSelectionModel().getSelectedItem();
        if(sp != null && !assocPartsTable.contains(sp)){
            assocPartsTable.add(sp);
            associatedProductTable.setItems(assocPartsTable);
        }
    }

    /**Removes part from associated part list*/
    public void onRemove(ActionEvent actionEvent) throws IOException{
        Part sp = (Part)associatedProductTable.getSelectionModel().getSelectedItem();
        if(sp == null)
            return;
        assocPartsTable.remove(sp);
        associatedProductTable.setItems(assocPartsTable);
    }



    /**Saves Product to Product Table on Main Screen*/
    @FXML
    private void onSave(ActionEvent actionEvent) throws IOException{
        int id = 1;
        for (int i = 0; i < Inventory.getAllProducts().size(); i++) {
            if (id <= Inventory.getAllProducts().get(i).getProductId())
                id = Inventory.getAllProducts().get(i).getProductId() + 1;
        }
        try {
            String name = addProductName.getText();
            int stock = Integer.parseInt(addProductInv.getText());
            double price = Double.parseDouble(addProductPrice.getText());
            int min = Integer.parseInt(addProductMin.getText());
            int max = Integer.parseInt(addProductMax.getText());


            if (min > max) /**Checks minimum is not greater than maximum*/
                throw new Exception("minGreater");
            if (min == max) /**Checks the min and max are not equal*/
                throw new Exception("maxMin");
            if (stock > max || stock < min) /**Checks inv is less than max and greater than min*/
                throw new Exception("inventory");


            Product newProduct = new Product(id, name, stock, price, min, max);
            for(int i = 0; i < assocPartsTable.size(); i++){
                newProduct.addAssociatedParts(assocPartsTable.get(i));
            }
            Inventory.addProduct(newProduct);
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

/**Initializes Parts table from main screen to Add Product screen
 * Initializes Associated Parts tables*/
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
        associatedProductTable.setItems(assocPartsTable);


    }


    /**Cancel Button returns to Main screen*/
    public void toMain(ActionEvent actionEvent) throws IOException{
        goToMain(actionEvent);
    }

/**Used to return to Main screen*/
    void goToMain(ActionEvent actionEvent) throws IOException {
        Parent addPartCancel = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(addPartCancel, 850, 430);
        stage.setTitle("Back to Main Screen");
        stage.setScene(scene);
        stage.show();
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