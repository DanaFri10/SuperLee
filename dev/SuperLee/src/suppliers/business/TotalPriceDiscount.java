package suppliers.business;

import java.util.Map;

public class TotalPriceDiscount extends PercentageBasedDealDiscount {
    private double minPrice;

    public TotalPriceDiscount(double percentage, double minPrice){
        super(percentage);
        if(percentage < 0)
            throw new IllegalArgumentException("Discount percentage must be a positive number.");
        if(percentage > 1)
            throw new IllegalArgumentException("Discount percentage must be a less than 100.");
        if(minPrice < 0)
            throw new IllegalArgumentException("Discount minimal price must be a positive number.");
        this.minPrice = minPrice;
    }

    public boolean equals(Object other)
    {
        if(!(other instanceof TotalPriceDiscount))
            return false;
        return this.minPrice == ((TotalPriceDiscount)other).minPrice &
                this.getPercentage() == ((TotalPriceDiscount)other).getPercentage();
    }

    @Override
    public int getType() {
        return 0;
    }

    public double getMinPrice() {return minPrice;}

    @Override
    public String toString() {
        return "Orders priced above " + minPrice +" shekels will receive a " + (double)Math.round(getPercentage()*1000d)/10d + "% discount";
    }

    public boolean discountApplies(double price, int amountOfProducts)
    {
        return price >= this.minPrice;
    }
}
