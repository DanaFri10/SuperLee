package suppliers.business;

import java.util.Map;

public class TotalProductsDiscount extends PercentageBasedDealDiscount {
    private int amountOfProducts;

    public TotalProductsDiscount(double percentage, int amountOfProducts){
        super(percentage);
        if(percentage < 0)
            throw new IllegalArgumentException("Discount percentage must be a positive number.");
        if(percentage > 1)
            throw new IllegalArgumentException("Discount percentage must be a less than 100.");
        if(amountOfProducts < 0)
            throw new IllegalArgumentException("Discount minimal amount of products must be a positive number.");
        this.amountOfProducts = amountOfProducts;
    }
    public boolean equals(Object other)
    {
        if(!(other instanceof TotalProductsDiscount))
            return false;
        return this.amountOfProducts == ((TotalProductsDiscount)other).amountOfProducts &
                this.getPercentage() == ((TotalProductsDiscount)other).getPercentage();
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public String toString() {
        return "Above " + amountOfProducts + " Products, a " +(double)Math.round(getPercentage()*1000d)/10d + "% discount will apply";
    }

    public int getAmountOfProducts()
    {
        return amountOfProducts;
    }

    public boolean discountApplies(double price, int amountOfProducts)
    {
        return amountOfProducts >= this.amountOfProducts;
    }
}
