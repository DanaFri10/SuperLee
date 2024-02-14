package suppliers.business;

import suppliers.dal.ProductDiscountsDAO;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DealProductInformation {
    private int catalogueNum;
    private double price;
    private int amount;
    private int productId;
    private transient List<ProductDiscount> discounts;
    private transient ProductDiscountsDAO discountsDAO;

    public DealProductInformation(int catalogue, int productId, double price, int amount, String supplierId) throws SQLException {
        if(catalogue < 0 | productId < 0 | price < 0)
            throw new IllegalArgumentException("Fields must be positive!");
        this.catalogueNum = catalogue;
        this.productId = productId;
        this.price = price;
        this.amount = amount;
        this.discountsDAO = new ProductDiscountsDAO(supplierId, productId);
    }

    public boolean hasEnoughOfProduct(int amount)
    {
        return amount <= this.amount;
    }

    public double calculatePrice(int amount) throws SQLException {
        if(discounts == null){
            discounts = discountsDAO.getAllDiscounts();
        }
        if(amount > this.amount)
            throw new IllegalArgumentException("The supplier can not supply this amount.");
        double initialPrice = amount*price;

        if(discounts == null || discounts.isEmpty())
            return initialPrice;

        double discountPrice = initialPrice;
        for(ProductDiscount discount : discounts){
            double newPrice = discount.getPrice(initialPrice,amount);
            if(newPrice < discountPrice) {
                discountPrice = newPrice;
            }
        }
        return discountPrice;
    }

    public double calculatePriceBeforeDiscount(int amount){
        if(amount > this.amount)
            throw new IllegalArgumentException("The supplier can not supply this amount.");
        return amount*price;
    }

    public void update(double price, int amount){
        if(price <= 0 || amount <=0)
            throw new IllegalArgumentException("Price and amount must be bigger than 0.");
        this.price = price;
        this.amount = amount;
    }

    public boolean discountExists(ProductDiscount discount) throws SQLException {
        if(discounts == null){
            discounts = discountsDAO.getAllDiscounts();
        }
        for(ProductDiscount d : discounts)
            if(d.equals(discount))
                return true;
        return false;
    }

    public int getCatalogueNum() {
        return catalogueNum;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public void addProductAmountDiscount(ProductAmountDiscount discount) throws SQLException {
        if(discounts == null){
            discounts = discountsDAO.getAllDiscounts();
        }
        if(discountExists(discount))
            throw new IllegalArgumentException("This discount already exists.");
        discounts.add(discount);
        discountsDAO.addProductAmountDiscount(discount);
    }

    public int getProductId(){
        return productId;
    }

    public List<ProductDiscount> getDiscounts() throws SQLException {
        if(discounts == null){
            discounts = discountsDAO.getAllDiscounts();
        }
        return discounts;
    }

    public void clearDiscounts() throws SQLException {
        this.discounts.clear();
        discountsDAO.removeAllDiscounts();
    }
}
