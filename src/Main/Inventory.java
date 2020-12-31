package Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class to manage the inventory of parts and products
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/31/2020
 */
public class Inventory {
    /* list of all parts */
    private ObservableList<Part> allParts = FXCollections.observableArrayList();

    /* list of all products */
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * add a part
     * @param newPart the part to add
     */
    public void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * add a product
     * @param newProduct the product to add
     */
    public void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * lookup a part by part id
     * @param partId the id of the part to look for
     * @return Part that matches
     */
    public Part lookupPart(int partId) {
        return allParts.stream().filter(part -> part.getId() == partId).findFirst().orElse(null);
    }

    /**
     * lookup products by product id
     * @param productId the product id to look for
     * @return Product that matches
     */
    public Product lookupProduct(int productId) {
        return allProducts.stream().filter(product -> product.getId() == productId).findFirst().orElse(null);
    }

    /**
     * lookup parts by part name
     * @param partName part name to search for
     * @return allParts that match the part name
     */
    public ObservableList<Part> lookupPart(String partName) {
        return allParts.filtered(part -> part.getName().toLowerCase().contains(partName.toLowerCase()));
    }

    /**
     * lookup products by product name
     * @param productName product name to search for
     * @return allProducts that match the product name
     */
    public ObservableList<Product> lookupProduct(String productName) {
        return allProducts.filtered(product -> product.getName().toLowerCase().contains(productName.toLowerCase()));
    }

    /**
     * update a part
     * @param index the index of the part to be updated
     * @param selectedPart the updated part to replace the existing part
     */
    public void updatePart(int index, Part selectedPart) {
        // what if index isn't there?
        allParts.set(index, selectedPart);
    }

    /**
     * update a product
     * @param index the index of the product to be updated
     * @param selectedProduct the updated product to replace existing product
     */
    public void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    /**
     * delete a part from the parts list
     * @param selectedPart the part to delete
     * @return boolean success/failure on deletion
     */
    public boolean deletePart(Part selectedPart) {
        return allParts.removeIf(part -> part.getId() == selectedPart.getId());
    }

    /**
     * delete a product from the products list
     * @param selectedProduct the product to delete
     * @return boolean success/failure on deletion
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
     * @return all products
     */
    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}
