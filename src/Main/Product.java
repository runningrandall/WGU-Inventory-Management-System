package Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class for managing products
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/31/2020
 */
public class Product {
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
        this.associatedParts = FXCollections.observableArrayList();
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
     * @param price the price to set
     */
    public void setPrice (double price) {
        this.price = price;
    }

    /**
     * getter for stock
     * @return product stock
     */
    public int getStock () {
        return stock;
    }

    /**
     * setter for stock
     * @param stock the stock value to set
     */
    public void setStock (int stock) {
        this.stock = stock;
    }

    /**
     * getter for min
     * @return product min
     */
    public int getMin () {
        return min;
    }

    /**
     * setter for min
     * @param min the min to set
     */
    public void setMin (int min) {
        this.min = min;
    }

    /**
     * getter for max
     * @return product max
     */
    public int getMax () {
        return max;
    }

    /**
     * setter for max
     * @param max the max value
     */
    public void setMax (int max) {
        this.max = max;
    }


    /**
     * method to add a part to the product
     * @param part the part to add
     */
    public void addAssociatedPart(Part part) {
        AtomicBoolean partAlreadyAssociated = new AtomicBoolean(false);
        associatedParts.forEach(aPart -> {
            if (aPart.getId() == part.getId()) {
                partAlreadyAssociated.set(true);
            }
        });

        if (!partAlreadyAssociated.get()) {
            associatedParts.add(part);
        }
    }

    /**
     * method to delete a part from the associatedParts list
     * @param selectedAssociatedPart
     * @return boolean regarding success of deletion
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.removeIf(part -> part.getId() == selectedAssociatedPart.getId());
    }

    /**
     * method to get all the associated parts from parts list
     * @return all parts associated with product
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
