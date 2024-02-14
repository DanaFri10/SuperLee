package stock.business;

public class Product {
    private int productId;
    private String name;
    private String manufacturer;

    public Product(int productId, String name, String manufacturer) {
        if(productId < 0)
            throw new IllegalArgumentException("Product ID must be a positive number.");
        this.productId = productId;
        this.name = name;
        this.manufacturer = manufacturer;
    }

    public void verifyProductInfo(String name, String manufacturer)
    {
        if(!Utils.isStringLengthBetween(name,3, 50))
            throw new IllegalArgumentException("Product name must have between 3 and 50 characters.");
        if(!Utils.isStringLengthBetween(manufacturer,3, 50))
            throw new IllegalArgumentException("Manufacturer name must have between 3 and 50 characters.");
    }

    public void updateProductInfo(String name, String manufacturer)
    {
        verifyProductInfo(name, manufacturer);
        this.name = name;
        this.manufacturer = manufacturer;
    }

    public boolean equals(Object other)
    {
        if(!(other instanceof Product))
            return false;
        return ((Product)other).getProductId() == productId &
                ((Product)other).getName() == name &
                ((Product)other).getManufacturer() == manufacturer;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

}
