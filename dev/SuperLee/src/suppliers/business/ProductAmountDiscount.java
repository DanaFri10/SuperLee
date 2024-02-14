package suppliers.business;

import java.util.Map;

public class ProductAmountDiscount extends ProductDiscount {
    private int amountOfProducts;
    private double percentage;

    public ProductAmountDiscount(double percentage, int amountOfProducts){
        if(percentage < 0 | percentage > 1)
            throw new IllegalArgumentException("Discount percentage must be a positive number.");
        if(amountOfProducts < 0)
            throw new IllegalArgumentException("Discount minimal amount of products must be a positive number.");

        this.amountOfProducts = amountOfProducts;
        this.percentage = percentage;
    }

    @Override
    public double getPrice(double price, int amount) {
        if(amount >= this.amountOfProducts){
            return price*(1-percentage);
        }
        return price;
    }

    @Override
    public String toString(String productName) {
        return "Above " + amountOfProducts +" " + productName +", a " +(double)Math.round(percentage*1000d)/10d + "% discount will apply on " + productName;
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof ProductAmountDiscount)) return false;
        return this.amountOfProducts == ((ProductAmountDiscount)other).amountOfProducts &&
                this.percentage == ((ProductAmountDiscount)other).percentage;
    }

    @Override
    public int getType() {
        return 0;
    }

    public int getAmountOfProducts()
    {
        return amountOfProducts;
    }

    public double getPercentage()
    {
        return percentage;
    }
}
