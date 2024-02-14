package suppliers.business;

import suppliers.dal.DealDiscountsDAO;
import suppliers.dal.DealProductsDAO;
import suppliers.dal.PeriodicOrderAgreementsDAO;

import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


public class Deal {
    private boolean[] deliveryDays;
    private boolean delivers;
    private int daysToDeliver;
    private PaymentMethod paymentMethod;
    private transient Map<Integer, DealProductInformation> products;
    private transient List<DealDiscount> discounts;
    private transient Supplier supplier;

    private transient DealDiscountsDAO discountsDAO;
    private transient DealProductsDAO productsDAO;
    private transient Map<Integer, PeriodicOrderAgreement> periodicsMap;
    private transient PeriodicOrderAgreementsDAO periodicOrderAgreementsDAO;

    public Deal(boolean delivers, int daysToDeliver, boolean[] deliveryDays, PaymentMethod paymentMethod, Supplier s) throws SQLException {
        verifyDealDetails(delivers, daysToDeliver, deliveryDays);
        this.supplier = s;
        this.delivers = delivers;
        this.daysToDeliver = daysToDeliver;
        this.paymentMethod = paymentMethod;
        this.deliveryDays = Arrays.copyOf(deliveryDays,7);
        this.discountsDAO = new DealDiscountsDAO(supplier.getCompanyId());
        this.productsDAO = new DealProductsDAO(supplier.getCompanyId());
        this.periodicOrderAgreementsDAO = new PeriodicOrderAgreementsDAO(supplier.getCompanyId());
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    private void verifyDealDetails(boolean delivers, int daysToDeliver, boolean[] deliveryDays)
    {
        boolean hasTrue = false;
        for(boolean b : deliveryDays)
            if(b) hasTrue = true;
        if(!hasTrue && daysToDeliver < 0)
            throw new IllegalArgumentException("Deal's day to deliver must be a positive number.");
        if(deliveryDays == null || deliveryDays.length !=7)
            throw new IllegalArgumentException("Deal's delivery days must be an array in length 7.");
        if(!delivers &  hasTrue)
            throw new IllegalArgumentException("Supplier does not deliver but has a delivery day.");
    }

    public DealProductInformation getDealProductInfo(int catalogue) throws SQLException {
        if(products == null) {
            products = productsDAO.getAllProducts();
        }
        if(!productExistsByCatalogueNum(catalogue))
            throw new IllegalArgumentException(String.format("A product with catalogue number %d doesn't exist in this deal.", catalogue));
        return products.get(catalogue);
    }


    public List<DealProductInformation> getAllProducts() throws SQLException{
        if(products == null){
            products = productsDAO.getAllProducts();
        }
        return products.values().stream().collect(Collectors.toList());
    }

    public boolean discountExists(DealDiscount discount) throws SQLException {
        if(discounts == null){
            discounts = discountsDAO.getAllDiscounts();
        }
        for(DealDiscount dealDiscount : discounts)
            if(dealDiscount.equals(discount))
                return true;
        return false;
    }

    public List<DealDiscount> getAllDealDiscounts() throws SQLException {
        if(discounts == null){
            discounts = discountsDAO.getAllDiscounts();
        }
        return discounts;
    }

    public void addTotalPriceDiscount(TotalPriceDiscount discount) throws SQLException {
        if(discounts == null){
            discounts = discountsDAO.getAllDiscounts();
        }
        if(discountExists(discount))
            throw new IllegalArgumentException("This discount already exists in the deal.");
        discounts.add(discount);
        discountsDAO.addTotalPriceDiscount(discount);
    }

    public void addTotalProductsDiscount(TotalProductsDiscount discount) throws SQLException {
        if(discounts == null){
            discounts = discountsDAO.getAllDiscounts();
        }
        if(discountExists(discount))
            throw new IllegalArgumentException("This discount already exists in the deal.");
        discounts.add(discount);
        discountsDAO.addTotalProductsDiscount(discount);
    }

    public boolean productExistsByCatalogueNum(int catalogueNum) throws SQLException
    {
        if(products == null){
            products = productsDAO.getAllProducts();
        }
        return products.containsKey(catalogueNum);
    }

    public void addProduct(int catalogue, int productId, double price, int amount) throws SQLException {
        if(productExistsByCatalogueNum(catalogue))
            throw new IllegalArgumentException(String.format("A product with catalogue number %d already exists.", catalogue));
        if(getDealProductByProductId(productId) != null)
            throw new IllegalArgumentException(String.format("The product with ID %d already exists in the deal.", productId));
        DealProductInformation dpi = new DealProductInformation(catalogue,productId,price,amount,supplier.getCompanyId());
        products.put(catalogue, dpi);
        productsDAO.addProduct(catalogue,productId,price,amount);
    }

    public double calculatePrice(Map<Integer, Integer> catalogueNumToAmount) throws SQLException {
        if(products == null){
            products = productsDAO.getAllProducts();
        }
        if(discounts == null){
            discounts = discountsDAO.getAllDiscounts();
        }
        double initialPrice = 0;
        int amountOfProducts = 0;
        for(int catNum : catalogueNumToAmount.keySet()){
            if(!products.containsKey(catNum)){
                throw new IllegalArgumentException("The supplier can not supply all of the products.");
            }
            initialPrice += this.getDealProductInfo(catNum).calculatePrice(catalogueNumToAmount.get(catNum));
            amountOfProducts += catalogueNumToAmount.get(catNum);
        }

        List<DealDiscount> discountsThatApply = new ArrayList<>();
        for(DealDiscount discount : discounts)
            if(discount.discountApplies(initialPrice, amountOfProducts))
                discountsThatApply.add(discount);

        double discountPrice = initialPrice;
        for(DealDiscount discount : discounts){
            discountPrice = discount.getPrice(discountPrice, amountOfProducts);
        }

        return discountPrice;
    }

    public Map<Integer, Double> calculatePriceProductMap(Map<Integer, Integer> catalogueNumToAmount) throws SQLException {
        if(products == null){
            products = productsDAO.getAllProducts();
        }
        if(discounts == null){
            discounts = discountsDAO.getAllDiscounts();
        }
        Map<Integer, Double> productToPriceMap = new HashMap<>();
        double totalPrice = 0;
        int totalAmount = 0;
        for(int catNum : catalogueNumToAmount.keySet()){
            if(!products.containsKey(catNum)){
                throw new IllegalArgumentException("The supplier can not supply all of the products.");
            }
            productToPriceMap.put(catNum,this.getDealProductInfo(catNum).calculatePrice(catalogueNumToAmount.get(catNum)));
            totalPrice += productToPriceMap.get(catNum);
            totalAmount += catalogueNumToAmount.get(catNum);
        }
        for(int catNum : catalogueNumToAmount.keySet()){
            for(DealDiscount discount : discounts){
                productToPriceMap.put(catNum,discount.getPriceForProduct(totalPrice,totalAmount,productToPriceMap.get(catNum)));
            }
        }

        return productToPriceMap;
    }

    public Date getArrivalDate(Date orderDate){
        Calendar c = Calendar.getInstance();
        c.setTime(orderDate);
        if(!hasFixedDays()){
            c.add(Calendar.DAY_OF_MONTH, daysToDeliver);
            return c.getTime();
        }
        int orderDay = c.get(Calendar.DAY_OF_WEEK)-1;
        int arrivalDay = orderDay;
        int arrivalTime = 0;
        while(!deliveryDays[arrivalDay]){
            arrivalTime += 1;
            arrivalDay = (arrivalDay+1)%7;
        }
        c.add(Calendar.DAY_OF_MONTH, arrivalTime);
        return c.getTime();
    }

    public int getArrivalTime(Date orderDate){
        if(!hasFixedDays()){
            return daysToDeliver;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(orderDate);
        int orderDay = c.get(Calendar.DAY_OF_WEEK)-1;
        int arrivalDay = orderDay;
        int arrivalTime = 0;
        while(!deliveryDays[arrivalDay]){
            arrivalTime += 1;
            arrivalDay = (arrivalDay+1)%7;
        }
        return arrivalTime;
    }

    public boolean hasFixedDays(){
        for(int i = 0; i < 7; i++){
            if(deliveryDays[i]){
                return true;
            }
        }
        return false;
    }

    public void addPeriodicOrder(int branchId, Map<Integer,Integer> catalogueAmount) throws SQLException {
        if(branchId < 0 || branchId > 9)
            throw new IllegalArgumentException("This branch does not exist.");


        if(periodicsMap == null || !periodicOrderAgreementsDAO.isGotAll()){
            periodicsMap = periodicOrderAgreementsDAO.getAllPeriodicOrders();
        }
        if(periodicsMap.containsKey(branchId))
            throw new IllegalArgumentException("This supplier already has a periodic order to this branch.");
        PeriodicOrderAgreement poa = new PeriodicOrderAgreement(catalogueAmount);
        if(hasFixedDays()){
            poa.setOrderDays(Arrays.copyOf(deliveryDays,7));
        }
        periodicOrderAgreementsDAO.addPeriodicOrder(poa,branchId);
        periodicsMap.put(branchId,poa);
    }

    public void setPeriodicOrderProducts(int branchId, Map<Integer,Integer> productsMap, boolean newOrder) throws SQLException {
        if(branchId < 0 || branchId > 9)
            throw new IllegalArgumentException("This branch does not exist.");

        if(periodicsMap == null || !periodicOrderAgreementsDAO.isGotAll()){
            periodicsMap = periodicOrderAgreementsDAO.getAllPeriodicOrders();
        }

        if(newOrder && periodicsMap.containsKey(branchId))
            throw new IllegalArgumentException("This supplier already has a periodic order to this branch.");

        if(!newOrder && !periodicsMap.containsKey(branchId))
            throw new IllegalArgumentException("This supplier does not have a periodic order to this branch.");


        for(int productId : productsMap.keySet()){
            if(!hasEnoughOfProduct(productId,productsMap.get(productId))){
                throw new IllegalArgumentException("Supplier is unable to supply this periodic order");
            }
        }
        Map<Integer, Integer> catalogueAmount = productIdsToCatalogueNums(productsMap);
        if(periodicsMap.containsKey(branchId)){
            if(!periodicsMap.get(branchId).canUpdate()){
                throw new IllegalArgumentException("Cannot update this periodic order as it is due tomorrow");
            }
            periodicsMap.get(branchId).setProductAmounts(catalogueAmount);
            periodicOrderAgreementsDAO.setPeriodicOrderProducts(branchId, catalogueAmount);
            return;
        }
        addPeriodicOrder(branchId, catalogueAmount);
    }

    public boolean hasPeriodicOrderAgreement(int branchId) throws SQLException {
        if(periodicsMap == null || !periodicOrderAgreementsDAO.isGotAll()){
            periodicsMap = periodicOrderAgreementsDAO.getAllPeriodicOrders();
        }
        return periodicsMap.containsKey(branchId);
    }


    public Map<Integer, PeriodicOrderAgreement> getPeriodics() throws SQLException {
        if(periodicsMap == null || !periodicOrderAgreementsDAO.isGotAll()){
            periodicsMap = periodicOrderAgreementsDAO.getAllPeriodicOrders();
        }
        return periodicsMap;
    }

    public PeriodicOrderAgreement getPeriodicOrder(int branchId) throws SQLException {
        Map<Integer, PeriodicOrderAgreement> periodics = getPeriodics();
        if(!periodics.containsKey(branchId))
            throw new IllegalArgumentException("This periodic order does not exist.");
        return periodics.get(branchId);
    }

    public void setPeriodicOrderDays(int branchId, boolean[] days) throws SQLException {
        if(periodicsMap == null || !periodicOrderAgreementsDAO.isGotAll()){
            periodicsMap = periodicOrderAgreementsDAO.getAllPeriodicOrders();
        }
        if(!periodicsMap.get(branchId).canUpdate()){
            throw new IllegalArgumentException("Cannot update this periodic order as it is due tomorrow");
        }
        periodicsMap.get(branchId).setOrderDays(days);
        periodicOrderAgreementsDAO.setPeriodicOrderDays(branchId, days);
    }

    public void setPeriodicOrderContact(int branchId, String contactId) throws SQLException {
        if(periodicsMap == null || !periodicOrderAgreementsDAO.isGotAll()){
            periodicsMap = periodicOrderAgreementsDAO.getAllPeriodicOrders();
        }
        periodicsMap.get(branchId).setAssignedContactId(contactId);
        periodicOrderAgreementsDAO.setPeriodicOrderContact(branchId, contactId);
    }

    public Map<Integer, PeriodicOrderAgreement> getDueToday() throws SQLException {
        if(periodicsMap == null || !periodicOrderAgreementsDAO.isGotAll()){
            periodicsMap = periodicOrderAgreementsDAO.getAllPeriodicOrders();
        }
        Map<Integer, PeriodicOrderAgreement> dueTodayMap = new HashMap<>();
        for(int branchId : periodicsMap.keySet()){
            if(periodicsMap.get(branchId).isDueToday()){
                dueTodayMap.put(branchId,periodicsMap.get(branchId));
            }
        }
        return dueTodayMap;
    }

    public double calculatePriceNoDiscount(Map<Integer, Integer> catalogueNumToAmount) throws SQLException {
        if(products == null){
            products = productsDAO.getAllProducts();
        }
        if(!canSupplyEverythingByCat(catalogueNumToAmount))
            throw new IllegalArgumentException("The supplier can not supply all of the products.");

        double initialPrice = 0;
        for(int catNum : catalogueNumToAmount.keySet()){
            initialPrice += this.getDealProductInfo(catNum).calculatePriceBeforeDiscount(catalogueNumToAmount.get(catNum));
        }
        return initialPrice;
    }


    public DealProductInformation getDealProductByProductId(int productId) throws SQLException{
        if(products == null){
            products = productsDAO.getAllProducts();
        }
        for(DealProductInformation productInformation : products.values()){
            if(productInformation.getProductId() == productId){
                return productInformation;
            }
        }
        return null;
    }

    public void removePeriodicOrder(int branchId) throws SQLException{
        if(periodicsMap == null || !periodicOrderAgreementsDAO.isGotAll()){
            periodicsMap = periodicOrderAgreementsDAO.getAllPeriodicOrders();
        }

        if(!periodicsMap.containsKey(branchId))
        {
            throw new IllegalArgumentException("The is no periodic order to this branch.");
        }

        if(!periodicsMap.get(branchId).canUpdate()){
            throw new IllegalArgumentException("Cannot remove this periodic order as it is due tomorrow.");
        }
        periodicOrderAgreementsDAO.removePeriodicOrder(branchId);
        periodicsMap.remove(branchId);
    }

    public void removeProduct(int catalogueNumber) throws SQLException{
        if(products == null){
            products = productsDAO.getAllProducts();
        }
        if(!products.containsKey(catalogueNumber))
            throw new IllegalArgumentException("This supplier does not have a product with catalouge number " + catalogueNumber);
        this.products.remove(catalogueNumber);
        productsDAO.removeProduct(catalogueNumber);
        if(periodicsMap == null || !periodicOrderAgreementsDAO.isGotAll()){
            periodicsMap = periodicOrderAgreementsDAO.getAllPeriodicOrders();
        }
        for(int branchId : periodicsMap.keySet()){
            periodicsMap.get(branchId).removeProduct(catalogueNumber);
            periodicOrderAgreementsDAO.removeProduct(catalogueNumber, branchId);
            if(periodicsMap.get(branchId).getProductAmounts().size() == 0){
                removePeriodicOrder(branchId);
            }
        }
    }

    public void addDiscountToProduct(ProductAmountDiscount discount, int catalogueNum) throws SQLException{
        if(products == null){
            products = productsDAO.getAllProducts();
        }
        DealProductInformation dealProductInformation = getDealProductInfo(catalogueNum); // Checks if product exists.
        if(products.get(catalogueNum).discountExists(discount))
            throw new IllegalArgumentException("This discount already exists.");
        dealProductInformation.addProductAmountDiscount(discount);
    }

    public boolean hasEnoughOfProduct(int productId, int amount) throws SQLException
    {
        DealProductInformation dealProductInformation = getDealProductByProductId(productId);
        return dealProductInformation != null && dealProductInformation.getAmount() >= amount;
    }

    public boolean canSupplyEverythingById(Map<Integer, Integer> productIdAmounts) throws SQLException
    {
        for(int productId : productIdAmounts.keySet())
        {
            DealProductInformation dealProductInformation = getDealProductByProductId(productId);
            if(dealProductInformation == null)
                return false;
            if(!dealProductInformation.hasEnoughOfProduct(productIdAmounts.get(productId)))
                return false;
        }
        return true;
    }

    public boolean canSupplyEverythingByCat(Map<Integer, Integer> catalogueNumAmounts) throws SQLException
    {
        for(int catNum : catalogueNumAmounts.keySet())
        {
            DealProductInformation dealProductInformation = getDealProductInfo(catNum);
            if(dealProductInformation == null)
                return false;
            if(!dealProductInformation.hasEnoughOfProduct(catalogueNumAmounts.get(catNum)))
                return false;
        }
        return true;
    }

    public Map<Integer, Integer> productIdsToCatalogueNums(Map<Integer, Integer> productIdAmounts) throws SQLException
    {
        Map<Integer, Integer> catalogueNums = new HashMap<>();
        for(int productId : productIdAmounts.keySet())
        {
            DealProductInformation dealProductInformation = getDealProductByProductId(productId);
            if(dealProductInformation == null)
                throw new IllegalArgumentException("This product does not exist.");
            catalogueNums.put(dealProductInformation.getCatalogueNum(), productIdAmounts.get(productId));
        }
        return catalogueNums;
    }

    public void updateDeal(boolean delivers, int daysToDeliver, PaymentMethod paymentMethod, boolean[] deliveryDays) throws SQLException {
        verifyDealDetails(delivers, daysToDeliver, deliveryDays);
        this.delivers = delivers;
        this.deliveryDays = deliveryDays;
        if(hasFixedDays()){
            if(periodicsMap == null || !periodicOrderAgreementsDAO.isGotAll()){
                periodicsMap = periodicOrderAgreementsDAO.getAllPeriodicOrders();
            }
            for(int branchId : periodicsMap.keySet()){
                periodicsMap.get(branchId).setOrderDays(deliveryDays);
                periodicOrderAgreementsDAO.setPeriodicOrderDays(branchId, deliveryDays);
            }
        }
        this.paymentMethod = paymentMethod;
        this.daysToDeliver = daysToDeliver;
    }

    public void setSupplier(Supplier supplier){
        this.supplier = supplier;
    }

    public Supplier getSupplier(){
        return supplier;
    }

    public boolean[] getDeliveryDays() {
        return deliveryDays;
    }

    public Map<Integer, DealProductInformation> getProducts() {
        return products;
    }

    public boolean isDelivers() {
        return delivers;
    }

    public String getSupplierId(){
        return supplier.getCompanyId();
    }

    public void updateProductInfo(int catalogue, double price, int amount) throws SQLException{
        getDealProductInfo(catalogue).update(price,amount);
        productsDAO.updateProduct(catalogue,price,amount);
    }

    public int getDaysToDeliver() {
        return daysToDeliver;
    }

    public boolean getDelivers() {
        return delivers;
    }

    public void clearDealDiscounts() throws SQLException {
        this.discounts = new LinkedList<>();
        discountsDAO.removeAllDiscounts();
    }

    public void clearProductDiscounts(int catNum) throws SQLException {
        getDealProductInfo(catNum).clearDiscounts();
    }

    public boolean equals(Object other)
    {
        if(!(other instanceof Deal)) return false;
        if (!(Arrays.equals(deliveryDays, ((Deal)other).getDeliveryDays()) &&
                delivers == ((Deal)other).getDelivers() &&
                daysToDeliver == ((Deal)other).getDaysToDeliver() &&
                paymentMethod.equals(((Deal)other).getPaymentMethod()))) return false;
        try {
            if(getProducts() == null)
            {
                if(((Deal)other).getProducts() != null) return false;
            }
            else if(!(getProducts().equals(((Deal)other).getProducts()))) return false;

            if(getAllDealDiscounts() == null)
            {
                if(((Deal)other).getAllDealDiscounts() != null) return false;
            }
            else if(!(getAllDealDiscounts().equals(((Deal)other).getAllDealDiscounts()))) return false;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
