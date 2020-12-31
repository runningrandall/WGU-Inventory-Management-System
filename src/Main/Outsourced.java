package Main;

/**
 * Outsourced class that extends the Part class
 * outsourced is a type of part manufactured by another company and requires company name
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/31/2020
 */
public class Outsourced extends Part {

    private String companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * getter for companyName
     * @return the company name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * setter for companyName
     * @param companyName the company outsourced the part
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
