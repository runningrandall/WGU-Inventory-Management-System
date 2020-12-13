package Main;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    /* list of all parts */
    private ObservableList<Part> allParts = FXCollections.observableArrayList();

    /* list of all products */
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();

    // create constructor with some default parts and products?

    /**
     * add a part
     * @param newPart
     */
    public void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * add a product
     * @param newProduct
     */
    public void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * lookup a part by part id
     * @param partId
     * @return allParts
     */
    public Part lookupPart(int partId) {
        return allParts.stream().filter(part -> part.getId() == partId).findFirst().orElse(null);
    }

    /**
     * lookup products by product id
     * @param productId
     * @return allProducts
     */
    public Product lookupProduct(int productId) {
        return allProducts.stream().filter(product -> product.getId() == productId).findFirst().orElse(null);
    }

    /**
     * lookup parts by part name
     * @param partName
     * @return allParts
     */
    public Part lookupPart(String partName) {
        return allParts.stream().filter(part -> part.getName() == partName).findFirst().orElse(null);
    }

    /**
     * lookup products by product name
     * @param productName
     * @return allProducts
     */
    public Product lookupProduct(String productName) {
        return allProducts.stream().filter(product -> product.getName() == productName).findFirst().orElse(null);
    }

    /**
     * update a part
     * @param index
     * @param selectedPart
     */
    public void updatePart(int index, Part selectedPart) {
        // what if index isn't there?
        allParts.set(index, selectedPart);
    }

    /**
     * update a product
     * @param index
     * @param selectedProduct
     */
    public void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    /**
     * delete a part from the parts list
     * @param selectedPart
     * @return boolean
     */
    public boolean deletePart(Part selectedPart) {
        return allParts.removeIf(part -> part.getId() == selectedPart.getId());
    }

    /**
     * delete a product from the products list
     * @param selectedProduct
     * @return boolean
     */
    public boolean deleteProduct(Product selectedProduct) {
        return allProducts.removeIf(product -> product.getId() == selectedProduct.getId());
    }

    /**
     * get all parts
     * @return all parts
     */
    public ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * get all products
     * @return
     */
    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}
