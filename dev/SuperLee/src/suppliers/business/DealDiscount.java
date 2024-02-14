package suppliers.business;

import java.util.Map;

public interface DealDiscount {
    double getPrice(double price, int amountOfProducts);

    double getPriceForProduct(double totalPrice, int totalAmount, double productPrice);

    boolean discountApplies(double price, int amountOfProducts);

    boolean equals(Object other);

    int getType();

    String toString();
}
