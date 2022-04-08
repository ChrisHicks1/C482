package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.Stage;
import model.InHouse;
import model.OutSourced;
import model.Inventory;
import javafx.scene.Node;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**Controller for Add Part Screen*/
public class AddPartController {
    @FXML
    private TextField txtAddPartId;
    @FXML
    private Button addSave;
    @FXML
    private Label bottomLabel;
    @FXML
    private TextField bottomField;
    @FXML
    private TextField txtAddPartName;
    @FXML
    private TextField txtAddPartInv;
    @FXML
    private TextField txtAddPartPrice;
    @FXML
    private TextField txtAddPartMin;
    @FXML
    private TextField txtAddPartMax;
    @FXML
    private TextField txtMachineOrCo;
    @FXML
    private RadioButton addPartHouse;
    @FXML
    private RadioButton addPartOutsourced;
    @FXML
    private ToggleGroup addPartTg;


    /**Cancel button returns to main screen*/
    public void toMain(ActionEvent actionEvent) throws IOException {goToMain(actionEvent);}


    /**Radio button changes to in-house and changes label to machine ID*/
    @FXML
    public void onAddInHouse(ActionEvent actionEvent) {
        bottomLabel.setText("Machine ID");
    }

    /**Radio button changes to outsourced and changes label to company name*/
    @FXML
    public void onAddOutsourced(ActionEvent actionEvent) {
        bottomLabel.setText("Company Name");
    }


    /**saves part to part table and returns to main screen*/
    @FXML
    void onSave(ActionEvent actionEvent) throws IOException {
        int id = 1;
        for (int i = 0; i < Inventory.getAllParts().size(); i++) {
            if (id <= Inventory.getAllParts().get(i).getId())
                id = Inventory.getAllParts().get(i).getId() + 1;
        }
        try {
            String name = txtAddPartName.getText();
            int stock = Integer.parseInt(txtAddPartInv.getText());
            double price = Double.parseDouble(txtAddPartPrice.getText());
            int min = Integer.parseInt(txtAddPartMin.getText());
            int max = Integer.parseInt(txtAddPartMax.getText());


            if (min > max) /**Checks minimum is not greater than maximum*/
                throw new Exception("minGreater");
            if (min == max) /**Checks the min and max are not equal*/
                throw new Exception("maxMin");
            if (stock > max || stock < min) /**Checks that inv is less than max and greater than min*/
                throw new Exception("inventory");


            /**checks if inhouse is selected*/
            if (addPartHouse.isSelected()) {
                int machineId = Integer.parseInt(txtMachineOrCo.getText());
                InHouse newPart = new InHouse(id, name, stock, price, min, max, machineId);
                Inventory.addPart(newPart);

                /**Checks if outsourced is selected*/
            } else if (addPartOutsourced.isSelected()) {
                String companyName = txtMachineOrCo.getText();
                OutSourced newOutSource = new OutSourced(id, name, stock, price, min, max, companyName);
                Inventory.addPart(newOutSource);

            }
            goToMain(actionEvent);
        } catch (Exception exception) {
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

    /**Used to return to main screen*/
    void goToMain(ActionEvent actionEvent) throws IOException {
        Parent addPartCancel = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(addPartCancel, 850, 430);
        stage.setTitle("Back to Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    public void addMachineId(InputMethodEvent inputMethodEvent) {
    }
}



