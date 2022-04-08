package model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    private int id;
    private String name;
    private int stock;
    private double price;
    private int min;
    private int max;



    /**Product constructor*/
    public Product(int productId, String productName, int productInv, double productPrice, int min, int max) {
        this.id = productId;
        this.name = productName;
        this.stock = productInv;
        this.price = productPrice;
        this.min = min;
        this.max = max;
    }





    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        for (int i = 0; i < getAllAssociatedParts().size(); i++) {
            if (associatedParts.get(i) == selectedAssociatedPart) {
                associatedParts.remove(i);
                return true;
            }
        }
        return false;
    }

    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }


    public void addAssociatedParts(Part part){
        associatedParts.add(part);
    }



    /**Getters and Setters*/
    public int getProductId() {
        return id;
    }

    public void setProductId(int productId) {
        this.id = productId;
    }

    public String getProductName() {
        return name;
    }

    public void setProductName(String productName) {
        this.name = productName;
    }

    public int getProductInv() {
        return stock;
    }

    public void setProductInv(int productInv) {
        this.stock = productInv;
    }

    public double getProductPrice() {
        return price;
    }

    public void setProductPrice(double productPrice) {
        this.price = productPrice;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}

