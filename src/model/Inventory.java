package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**Class contains Inventory details*/
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();






    /**Add Part*/
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**Modify or "Update" Part*/
    public static void modifyPart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }
    /**Delete Part*/
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**Search or "lookup" Part by int*/
    public static Part searchParts(int partId) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId() == partId)
                return allParts.get(i);
        }
        return null;
    }
    /**Search or "lookup" Part by String*/
    public static ObservableList<Part> searchPartsName(String partialName) {
        ObservableList<Part> namedParts = FXCollections.observableArrayList();
        for(int i = 0; i < getAllParts().size(); i++){
            if(getAllParts().get(i).getName().toLowerCase().contains(partialName.toLowerCase())){
                namedParts.add(getAllParts().get(i));
            }
        }
        return namedParts;
    }





    /**Add Product*/
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**Modify or "Update" Product*/
    public static void modifyProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    /**Delete Product*/
    public static boolean deleteProduct(Product selectedProduct){
        return allProducts.remove(selectedProduct);
    }

    /**Search or "lookup" Product by int*/
    public static Product searchProducts(int productId) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getProductId() == productId)
                return allProducts.get(i);
        }
        return null;
    }

    /**Search or "lookup" Product by String*/
    public static ObservableList<Product> searchProductsName(String partialName) {
        ObservableList<Product> namedProducts = FXCollections.observableArrayList();
        for(int i = 0; i < getAllProducts().size(); i++){
            if(getAllProducts().get(i).getProductName().toLowerCase().contains(partialName.toLowerCase())){
                namedProducts.add(getAllProducts().get(i));
            }
        }

        return namedProducts;
    }




    /**Returns list of all Parts*/
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**Returns list of all Products*/
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }


}
