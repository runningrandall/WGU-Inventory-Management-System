package Main;
/**
 * Class for Product.java
 */

/**
 *
 * @author Randall Adams
 */
import javafx.collections.ObservableList;

public abstract class Product {
    private ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product (int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @return the product id
     */
    public int getId () {
        return id;
    }
    /**
     * method to set a product id
     * @param id the id to set
     */
    public void setId (int id) {
        this.id = id;
    }

    /**
     * getter for name
     * @return the product name
     */
    public String getName () {
        return name;
    }

    /**
     * setter for name
     * @param name the name to set
     */
    public void setName (String name) {
        this.name = name;
    }

    /**
     * getter for price
     * @return product price
     */
    public double getPrice () {
        return price;
    }

    /**
     * setter for price
     * @param price to set
     */
    public void setPrice (double price) {
        this.price = price;
    }

    /**
     * getter for stock
     * @return stock
     */
    public int getStock () {
        return stock;
    }

    /**
     * setter for stock
     * @param stock
     */
    public void setStock (int stock) {
        this.stock = stock;
    }

    /**
     * getter for min
     * @return min
     */
    public int getMin () {
        return min;
    }

    /**
     * setter for min
     * @param min
     */
    public void setMin (int min) {
        this.min = min;
    }

    /**
     * getter for max
     * @return max
     */
    public int getMax () {
        return max;
    }

    /**
     * setter for max
     * @param max
     */
    public void setMax (int max) {
        this.max = max;
    }


    /**
     * method to add a part to the product
     * @param part
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * method to delete a part from the associatedParts list
     * @param selectedAssociatedPart
     * @return
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.removeIf(part -> part.getId() == selectedAssociatedPart.getId());
    }

    /**
     * method to get all the associated parts from parts list
     * @return
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
