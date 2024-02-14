package suppliers.business;

public abstract class PercentageBasedDealDiscount implements DealDiscount{
    private double percentage;
    public PercentageBasedDealDiscount(double percentage){
        this.percentage = percentage;
    }
    @Override
    public double getPrice(double price, int amountOfProducts) {
        if(discountApplies(price,amountOfProducts)){
            return price*(1-percentage);
        }
        return price;
    }

    @Override
    public double getPriceForProduct(double totalPrice, int totalAmount, double productPrice) {
        if(discountApplies(totalPrice,totalAmount)){
            return productPrice*(1-percentage);
        }
        return productPrice;
    }

    public double getPercentage(){
        return percentage;
    }
}
