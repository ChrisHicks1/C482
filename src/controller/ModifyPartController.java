package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Inventory;
import model.OutSourced;
import model.InHouse;
import javafx.scene.Node;
import model.Part;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**Controller for Modify Parts screen*/
public class ModifyPartController {

    int partIndex;

    @FXML
    private RadioButton modifyInHouse;
    @FXML
    private RadioButton modifyOutSource;
    @FXML
    private ToggleGroup tgroup;
    @FXML
    private TextField txtModPartId;
    @FXML
    private AnchorPane toMain;
    @FXML
    private TextField txtModName;
    @FXML
    private TextField txtModInv;
    @FXML
    private TextField txtModPrice;
    @FXML
    private TextField txtModMin;
    @FXML
    private TextField txtModMax;
    @FXML
    private TextField txtModMachOrCo;
    @FXML
    private Button modSave;
    @FXML
    private Label modBottomLabel;


    boolean isInHouse = true;

    /**Initiate data from selected item on main screen*/
    public void init(Part part){
        partIndex = Inventory.getAllParts().indexOf(part);
        if(part instanceof InHouse) {
            modifyInHouse.setSelected(true);
            modBottomLabel.setText("Machine ID");
            txtModMachOrCo.setText(Integer.toString(((InHouse) part).getMachineId()));
        }
        else if(part instanceof OutSourced) {
            modifyOutSource.setSelected(true);
            modBottomLabel.setText("Company Name");
            txtModMachOrCo.setText(((OutSourced) part).getCompanyName());
        }
        txtModPartId.setText(Integer.toString(part.getId()));
        txtModName.setText(part.getName());
        txtModInv.setText(Integer.toString(part.getStock()));
        txtModPrice.setText(Double.toString(part.getPrice()));
        txtModMin.setText(Integer.toString(part.getMin()));
        txtModMax.setText(Integer.toString(part.getMax()));
    }




    /**Replaces Part on main menu with new part
     *
     *
     * LOGICAL ERROR occurred when selecting "Cancel" during a modification,
     * it would still delete the part from the Parts table on the main screen.
     *
     * I solved this by replacing the Part in the table rather than deleting
     * and replacing the part.
     *
     * */
    @FXML
    void onModSave(ActionEvent actionEvent) throws IOException {
        try {
            int id = Integer.parseInt(txtModPartId.getText());
            String name = txtModName.getText();
            int stock = Integer.parseInt(txtModInv.getText());
            double price = Double.parseDouble(txtModPrice.getText());
            int min = Integer.parseInt(txtModMin.getText());
            int max = Integer.parseInt(txtModMax.getText());


            if (min > max) /**Checks minimum is not greater than maximum*/
                throw new Exception("minGreater");
            if (min == max) /**Checks the min and max are not equal*/
                throw new Exception("maxMin");
            if (stock > max || stock < min) /**Checks that inv is less than max and greater than min*/
                throw new Exception("inventory");


            /**If inhouse is selected*/
            if (modifyInHouse.isSelected()) {
                int machineId = Integer.parseInt(txtModMachOrCo.getText());
                InHouse newPart = new InHouse(id, name, stock, price, min, max, machineId);
                Inventory.modifyPart(partIndex, newPart);

                /**if outsourced is selected*/
            } else if (modifyOutSource.isSelected()) {
                String companyName = txtModMachOrCo.getText();
                OutSourced newOutSource = new OutSourced(id, name, stock, price, min, max, companyName);
                Inventory.modifyPart(partIndex, newOutSource);

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
            } else if (exception.getMessage().equals("maxMin")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Minimum must be smaller than maximum");
                alert.showAndWait();
            }

        }

    }



    /**Cancel Button returns to main screen*/
    public void toMain(ActionEvent actionEvent) throws IOException {
        goToMain(actionEvent);
    }

    void goToMain(ActionEvent actionEvent) throws IOException {
        Parent modifyPartCancel = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(modifyPartCancel, 850, 430);
        stage.setTitle("Back to Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**Radio button changes to in-house and changes label to machine ID*/
    @FXML
    public void onModInHouse(ActionEvent actionEvent) {
        modBottomLabel.setText("Machine ID");
    }


    /**Radio button changes to outsourced and changes label to company name*/
    @FXML
    public void onModOutSource(ActionEvent actionEvent) {
        modBottomLabel.setText("Company Name");
    }
}