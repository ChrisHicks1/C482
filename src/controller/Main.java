package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import model.Part;
import model.Product;
import model.Inventory;
import static model.Inventory.getAllProducts;
import static model.Inventory.getAllParts;
import static model.Inventory.searchParts;
import static model.Inventory.searchPartsName;
import static model.Inventory.searchProducts;
import static model.Inventory.searchProductsName;



import javafx.scene.control.*;

import java.io.IOError;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**Controller for Main screen */
public class Main implements Initializable {


    @FXML
    private Button partDelete;
    @FXML
    private Button productDelete;
    @FXML
    private TableView <Part> partsTable;
    @FXML
    private TableColumn <Part, Integer> partIdCol;
    @FXML
    private TableColumn <Part, String> partNameCol;
    @FXML
    private TableColumn <Part, Integer> partInvCol;
    @FXML
    private TableColumn <Part, Double> partPriceCol;


    @FXML
    private TextField queryPartTF;
    @FXML
    private Button partSearch;

    @FXML
    private TableView <Product> productsTable;
    @FXML
    private TableColumn <Product, Integer> productIdCol;
    @FXML
    private TableColumn <Product, String> productNameCol;
    @FXML
    private TableColumn <Product, Integer> productInvCol;
    @FXML
    private TableColumn <Product, Double> productPriceCol;


    @FXML
    private TextField queryProductTF;
    @FXML
    private Button productSearch;

    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();

/**initializing parts and products tables*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partsTable.setItems(Inventory.getAllParts());


        productIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("productInv"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        productsTable.setItems(Inventory.getAllProducts());
    }



//Delete Buttons

    /**button deletes from parts table.
     * checks for part associated with product before delete*/
    public void onPartDelete(ActionEvent actionEvent) {
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            Alert newAlert = new Alert(Alert.AlertType.ERROR);
            newAlert.setTitle("Part Deletion Error");
            newAlert.setHeaderText("Part Not Deleted");
            newAlert.setContentText("No Part Selected");
            newAlert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Part");
            alert.setHeaderText("Are you sure you want to delete?");
            alert.setContentText("Press OK to delete this part. \nPress Cancel to cancel.");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                partsTable.setItems(Inventory.getAllParts());
                for (int i = 0; i < Inventory.getAllProducts().size(); i++) {
                    if (Inventory.getAllProducts().get(i).getAllAssociatedParts().contains(selectedPart)) {
                        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                        alert1.setTitle("Part Associated");
                        alert1.setHeaderText("Part Associated With Product");
                        alert1.setContentText("Press OK to delete this part from Associated Products. \nPress Cancel to cancel;");
                        alert1.showAndWait();
                        if (alert1.getResult() == ButtonType.OK) {
                            Inventory.getAllProducts().get(i).deleteAssociatedPart(selectedPart);
                            Inventory.deletePart(selectedPart);
                        }
                        else{
                        return;
                        }
                    }
                }
            }

        }
    }

    /**button deletes from product table*/
    public void onProductDelete(ActionEvent actionEvent) {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null){
                Alert newAlert = new Alert(Alert.AlertType.ERROR);
                newAlert.setTitle("Product Deletion Error");
                newAlert.setHeaderText("Product Not Deleted");
                newAlert.setContentText("No Product Selected");
                newAlert.showAndWait();
            }
        else if(selectedProduct != null && selectedProduct.getAllAssociatedParts().size() > 0) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Part Associated");
            alert1.setHeaderText("Product has Associated Parts");
            alert1.setContentText("Can not delete products with associated parts");
            alert1.showAndWait();
        }
        else if (selectedProduct != null && selectedProduct.getAllAssociatedParts().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Product");
            alert.setHeaderText("Are you sure you want to delete?");
            alert.setContentText("Press OK to delete this product. \nPress Cancel to cancel.");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                Inventory.deleteProduct(selectedProduct);
                productsTable.setItems(Inventory.getAllProducts());
            }
        }
    }




//Add Buttons

    /**button moves to Add Part screen*/
    public void toAddPart(ActionEvent actionEvent) throws IOException{

        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 500, 500);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();

    }


    /**button moves to Add Product screen*/
    public void toAddProduct(ActionEvent actionEvent) throws IOException{

        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 500);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();

    }


//Modify Buttons
    /**button checks for selected part and moves to Modify Part screen*/
    public void toModifyPart(ActionEvent actionEvent) throws IOException {
        selectedPart = partsTable.getSelectionModel().getSelectedItem();
        selectedPartIndex = getAllParts().indexOf(selectedPart);
        if (selectedPart == null) {
            Alert nullAlert = new Alert(Alert.AlertType.ERROR);
            nullAlert.setTitle("Part Modification Error");
            nullAlert.setHeaderText("Part is NOT able to be modified");
            nullAlert.setContentText("No part selected");
            nullAlert.showAndWait();
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyPart.fxml"));
                Parent modifyPart = loader.load();

                ModifyPartController modifiedPart = loader.getController();
                modifiedPart.init(selectedPart);

                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(modifyPart, 500, 500);
                stage.setTitle("Modify Part");
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException e){}
        }
    }

    /**button checks for selected product and moves to Modify Product screen*/
    public void toModifyProduct(ActionEvent actionEvent) throws IOException{
        selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        selectedProductIndex = getAllProducts().indexOf(selectedProduct);
        if(selectedProduct == null){
            Alert nullAlert = new Alert(Alert.AlertType.ERROR);
            nullAlert.setTitle("Product Modification Error");
            nullAlert.setHeaderText("Product is NOT able to be modified");
            nullAlert.setContentText("No product selected");
            nullAlert.showAndWait();
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyProduct.fxml"));
                Parent modifyProduct = loader.load();

                ModifyProductController modifiedProduct = loader.getController();
                modifiedProduct.init(selectedProduct);


                Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(modifyProduct, 800, 500);
                stage.setTitle("Modify Product");
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException e) {}
        }
    }


    /**button exits app*/
    public void onExitApp(ActionEvent actionEvent){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Application");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Press OK to exit. \nPress Cancel to stay.");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
        else{
            alert.close();
        }
    }



    private static Part selectedPart;

    private static int selectedPartIndex;

    private static Product selectedProduct;

    private static int selectedProductIndex;


    /**searches parts tabel*/
    @FXML
    private void searchByPartsAction(ActionEvent actionEvent) {
        String text = queryPartTF.getText();
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
            queryPartTF.setText("");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Not Found");
            alert.setContentText("That item does not exist");
            alert.showAndWait();
        }
        else{
            partsTable.setItems(Inventory.searchPartsName(text));
        }
    }


/**searches product table*/
    @FXML
    private void searchByProductsAction(ActionEvent actionEvent) {
        String text = queryProductTF.getText();
        ObservableList<Product> product = searchProductsName(text);

        try {
            int productId = Integer.parseInt(text);
            ObservableList<Product> product1 = FXCollections.observableArrayList();
            product1.add(Inventory.searchProducts(productId));
            if(product1.get(0) != null)
                productsTable.setItems(product1);
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Not Found");
                alert.setContentText("That item does not exist");
                alert.showAndWait();
            }
            return;
        }
        catch (Exception e){}
        if (Inventory.searchProductsName(text).size() == 0) {
            queryProductTF.setText("");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Not Found");
            alert.setContentText("That item does not exist");
            alert.showAndWait();
        }
        else{
            productsTable.setItems(Inventory.searchProductsName(text));
        }
    }
}