package test.task.ao1;

public class DataItem {

    private int productID;
    private String name;
    private String condition;
    private String state;
    private double price;

    public DataItem(int productID, String name, String condition, String state, double price) {
        this.productID = productID;
        this.name = name;
        this.condition = condition;
        this.state = state;
        this.price = price;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String toCsvRow() {
        return productID + "," + name + "," + condition + "," + state + "," + price;
    }
}
