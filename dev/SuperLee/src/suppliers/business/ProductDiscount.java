package suppliers.business;

public abstract class ProductDiscount {
    public abstract double getPrice(double price, int amount);

    public abstract String toString(String productName);

    public abstract boolean equals(Object other);

    public abstract int getType();
}
