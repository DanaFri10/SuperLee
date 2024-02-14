package suppliers.business;

import com.google.gson.Gson;
import stock.business.Branch;
import stock.business.Product;
import stock.business.StockController;
import stock.dal.BranchDAO;
import suppliers.dal.ContactsDAO;
import suppliers.dal.OrdersDAO;
import suppliers.dal.SuppliersDAO;
import stock.dal.ProductDAO;

import javax.mail.MessagingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

import static suppliers.business.Utils.*;

public class SuppliersController {
    private OrdersDAO ordersDAO;
    private SuppliersDAO suppliersDAO;
    private BranchDAO branchDAO;
    private ProductDAO productsDAO;
    private ContactsDAO contactsDAO;

    private Timer timer;

    private final String EMAIL_STRING_ORDER_MADE = """
                Hello %s,
                
                This is an automated message from the SuperLee Suppliers Module.
                SuperLee would like to make an order from %s, which has registered you as a contact,
                to branch number %s.
                
                The Order is:
                %s
                
                If this is a mistake or if you are unable to supply this order, 
                please contact a SuperLee executive.
                """;
    private final String EMAIL_STRING_DEAL_ALTERED =
            """
                Hello %s,
                
                This is an automated message from the SuperLee Suppliers Module.
                The deal with the company %s, which has registered you as a contact, has been altered.
                
                The details of the new deal are:
                %s
                
                If this is a mistake, please contact a SuperLee executive.
                """;;
    private static Gson gson = new Gson();

    public SuppliersController(StockController stc, ProductDAO productsDAO, BranchDAO branchDAO) throws Exception {
        this.productsDAO = productsDAO;
        this.branchDAO = branchDAO;
        this.contactsDAO = new ContactsDAO();
        this.suppliersDAO = new SuppliersDAO(contactsDAO);
        this.ordersDAO = new OrdersDAO(suppliersDAO);
        timer = new Timer();
        scheduleTimer(stc);
        refillStock(stc);
    }

    public String createCustomOrder(Map<String, Map<Integer, Integer>> suppliersProductsAmount, int branchId) throws SQLException{
        //branchDAO.getBranch(branchId);//check branch exists
        if(branchId < 0 | branchId > 9)
            throw new IllegalArgumentException("This branch does not exist.");
        for(String supplierId : suppliersProductsAmount.keySet())
        {
            Deal supplierDeal = suppliersDAO.getSupplier(supplierId).getDeal();
            Map<Integer, Integer> supplierProductIds = suppliersProductsAmount.get(supplierId);
            Map<Integer, Integer> supplierCatNum = supplierDeal.productIdsToCatalogueNums(supplierProductIds);
            suppliersProductsAmount.put(supplierId, supplierCatNum);
        }

        return gson.toJson(ordersDAO.addOrder(suppliersProductsAmount, branchId)); // checks if the amounts are legal
    }

    public String createOptimalOrderJson (Map<Integer, Integer> productAmounts, int branchId) throws SQLException {
        return gson.toJson(createOptimalOrder(productAmounts, branchId));
    }

    public Order createOptimalOrder (Map<Integer, Integer> productAmounts, int branchId) throws SQLException {
        //branchDAO.getBranch(branchId);//check branch exists
        if(branchId < 0 | branchId > 9)
            throw new IllegalArgumentException("This branch does not exist.");
        return ordersDAO.addOrder(getOptimalDeals(productAmounts, suppliersDAO.getAllSuppliers()), branchId);
    }

    private void scheduleTimer(StockController stc) throws SQLException {
        long day = 1000*60*60*24;
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long howManyToMidnight = (c.getTimeInMillis()-System.currentTimeMillis());
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    deployPeriodicOrders();
                    refillStock(stc);
                } catch (Exception e) {
                    //log
                }
            }
        }, howManyToMidnight,day);
    }

    public void stopTimer(){
        timer.cancel();
    }

    public void setPeriodicOrderProducts(String supplierId, int branchId, Map<Integer, Integer> productsMap, boolean newOrder) throws SQLException {
        Branch branch = branchDAO.getBranch(branchId);
        for(int productId : productsMap.keySet()){
            if(branch.getProduct(productId) == null){
                throw new IllegalArgumentException("Branch with ID " + branchId + " does not sell product with ID " + productId);
            }
        }
        suppliersDAO.getSupplier(supplierId).getDeal().setPeriodicOrderProducts(branchId, productsMap, newOrder);
    }

    public void removePeriodicOrder(String supplierId, int branchId) throws SQLException {
        suppliersDAO.getSupplier(supplierId).getDeal().removePeriodicOrder(branchId);
    }

    public boolean supplierHasFixedDays(String supplierId) throws SQLException {
        return suppliersDAO.getSupplier(supplierId).getDeal().hasFixedDays();
    }

    public void setPeriodicOrderDays(String supplierId, int branchId, boolean[] days) throws SQLException {
        Deal deal = suppliersDAO.getSupplier(supplierId).getDeal();
        if(deal.hasFixedDays()){
            throw new IllegalArgumentException("Cant set days for periodic order with a supplier who has fixed delivery days");
        }
        if(!deal.hasPeriodicOrderAgreement(branchId)){
            throw new IllegalArgumentException("There is no periodic order agreement for this supplier with this branch");
        }
        deal.setPeriodicOrderDays(branchId, days);
    }

    public void setPeriodicOrderContact(String supplierId, int branchId, String contactId) throws SQLException {
        Supplier supplier = suppliersDAO.getSupplier(supplierId);
        if(!supplier.isContact(contactId)){
            throw new IllegalArgumentException("Supplier with ID " + supplierId + " has no contact with ID " + contactId);
        }
        if(!supplier.getDeal().hasPeriodicOrderAgreement(branchId)){
            throw new IllegalArgumentException("There is no periodic order agreement for this supplier with this branch");
        }
        supplier.getDeal().setPeriodicOrderContact(branchId, contactId);
    }

    private Map<String, Map<Integer, Integer>> getOptimalDeals(Map<Integer, Integer> productAmounts, List<Supplier> supplierSubset) throws SQLException {
        Map<String, Map<Integer, Integer>> deals = new HashMap<>();
        Map<Integer, Set<Set<Set<Integer>>>> allDivisions = findAllPartitions(productAmounts.keySet());
        double minDivisionPrice = Double.POSITIVE_INFINITY;

        for (int i = 1; i <= productAmounts.size(); i++) {
            for (Set<Set<Integer>> division : allDivisions.get(i)) {
                Map<String, Map<Integer, Integer>> currDeals = splitDivisionToDeals(division, productAmounts, supplierSubset);
                if (currDeals != null) {
                    double divisionPrice = divisionPrice(currDeals);
                    if (divisionPrice < minDivisionPrice) {
                        deals = new HashMap<>(currDeals);
                        minDivisionPrice = divisionPrice;
                    }
                }
            }
            if (!deals.isEmpty())
                return deals;
        }
        throw new IllegalArgumentException("No supplier is possible to supply these products.");
    }

    private Map<String, Map<Integer, Integer>> splitDivisionToDeals(Set<Set<Integer>> division, Map<Integer, Integer> productAmounts, List<Supplier> supplierSubest) throws SQLException {
        Map<String, Map<Integer, Integer>> currDeals = new HashMap<>();

        for (Set<Integer> productSubset : division) {
            Map<Integer, Integer> subsetAmounts = subsetAmounts(productSubset, productAmounts);
            Supplier subsetSupplier = minPriceSupplier(subsetAmounts, supplierSubest);
            if (subsetSupplier == null)
                return null;

            String subsetSupplierId = subsetSupplier.getCompanyId();
            Map<Integer, Integer> subsetCatNumsToAmounts = subsetSupplier.getDeal().productIdsToCatalogueNums(subsetAmounts);
            if (currDeals.containsKey(subsetSupplierId))
                subsetAmounts.putAll(currDeals.get(subsetSupplierId));
            currDeals.put(subsetSupplierId, subsetCatNumsToAmounts);
        }
        return currDeals;
    }

    private Map<Integer, Integer> subsetAmounts(Set<Integer> productIds, Map<Integer, Integer> productAmounts) {
        Map<Integer, Integer> subsetAmounts = new HashMap<>();
        for (int productId : productIds)
            subsetAmounts.put(productId, productAmounts.get(productId));
        return subsetAmounts;
    }

    private double divisionPrice(Map<String, Map<Integer, Integer>> deals) throws SQLException {
        double price = 0;
        for (String supplierId : deals.keySet()) {
            price += suppliersDAO.getSupplier(supplierId).getDeal().calculatePrice(deals.get(supplierId));
        }
        return price;
    }

    private Supplier minPriceSupplier(Map<Integer, Integer> productIdsAmounts, List<Supplier> suppliersSubset) throws SQLException {
        Supplier suppliesEverything = null;
        double minPrice = Double.POSITIVE_INFINITY;
        //List<Supplier> suppliers = suppliersDAO.getAllSuppliers();
        for (Supplier s : suppliersSubset) {
            Deal d = s.getDeal();
            if (d.canSupplyEverythingById(productIdsAmounts)) {
                double sPrice = d.calculatePrice(d.productIdsToCatalogueNums(productIdsAmounts));
                if (sPrice < minPrice) {
                    suppliesEverything = s;
                    minPrice = sPrice;
                }
            }
        }
        return suppliesEverything;
    }

    public String makeShortageOrder(Map<Integer, Integer> productAmounts, Map<Integer, Integer> extras, int branchId) throws SQLException
    {
        if(branchId < 0 | branchId > 9)
            throw new IllegalArgumentException("This branch does not exist.");
        Date now = new Date(System.currentTimeMillis());
        productAmounts = mergeProductsMap(productAmounts, extras);
        return gson.toJson(ordersDAO.addOrder(getFastestOrder(productAmounts, now), branchId, now));
    }

    public String createFastestOrder(Map<Integer, Integer> productAmounts, int branchId) throws SQLException
    {
        if(branchId < 0 | branchId > 9)
            throw new IllegalArgumentException("This branch does not exist.");
        Date now = new Date(System.currentTimeMillis());
        return gson.toJson(ordersDAO.addOrder(getFastestOrder(productAmounts, now), branchId, now));
    }

    public Map<String, Map<Integer, Integer>>  getFastestOrder(Map<Integer, Integer> productAmounts, Date now) throws SQLException {
        Map<Integer, List<Supplier>> daysSuppliers = new HashMap<>();
        Map<Integer, Set<Integer>> daysProducts = new HashMap<>();

        for(int productId : productAmounts.keySet())
        {
            List<Supplier> fastestSuppliers = fastestSuppliers(productId, productAmounts.get(productId), now);
            int days = fastestSuppliers.get(0).getDeal().getArrivalTime(now);
            daysSuppliers.put(days, fastestSuppliers);

            if(!daysProducts.containsKey(days))
                daysProducts.put(days, new HashSet<>());
            daysProducts.get(days).add(productId);
        }

        Map<String, Map<Integer, Integer>> deals = new HashMap<>();
        for(int days: daysSuppliers.keySet())
        {
            Set<Integer> productIds = daysProducts.get(days);
            List<Supplier> suppliers = daysSuppliers.get(days);
            deals = mergeMap(deals, getOptimalDeals(subsetAmounts(productIds, productAmounts), suppliers));
        }

        return deals;
    }

    private List<Supplier> fastestSuppliers(int productId,int amount, Date now) throws SQLException
    {
        List<Supplier> suppliers = suppliersDAO.getAllSuppliers();
        List<Supplier> res = new ArrayList<>();
        double fastestDays = Double.POSITIVE_INFINITY;
        for(Supplier s : suppliers)
        {
            if(s.getDeal().hasEnoughOfProduct(productId, amount))
            {
                int days = s.getDeal().getArrivalTime(now);
                if(days <= fastestDays)
                    fastestDays = days;
            }
        }

        if(fastestDays == Double.POSITIVE_INFINITY)
            throw new IllegalArgumentException("No supplier can supply this product.");

        for(Supplier s : suppliers)
        {
            if(s.getDeal().hasEnoughOfProduct(productId, amount))
            {
                if(s.getDeal().getArrivalTime(now) == fastestDays)
                    res.add(s);
            }
        }

        return res;
    }

    /*
    private Supplier getFastestSupplier(int productId, int amount, Date now) throws SQLException {
        List<Supplier> suppliers = suppliersDAO.getAllSuppliers();
        Supplier fastestSupplier = null;

        for(Supplier s : suppliers)
        {
            if(s.getDeal().hasEnoughOfProduct(productId, amount))
            {
                if(fastestSupplier == null)
                    fastestSupplier = s;
                else
                {
                    if(s.getDeal().getArrivalDate(now).before(fastestSupplier.getDeal().getArrivalDate(now)))
                        fastestSupplier = s;
                }
            }
        }

        if(fastestSupplier == null)
            throw new IllegalArgumentException("No supplier can supply this product.");
        return fastestSupplier;
    }*/


    public double getPriceFromSupplier(int orderId, String companyId) throws SQLException {
        Order order = ordersDAO.getOrder(orderId); // Checks if order exists
        return order.getPriceForSupplier(companyId);
    }

    public double getPriceTotal(int orderId) throws SQLException {
        Order order = ordersDAO.getOrder(orderId); // Checks if order exists
        return order.getPriceTotal();
    }

    protected void refillStock(StockController stc) throws Exception {
        List<Order> orders = ordersDAO.getOrderHistory();
        for(Order order : orders){
            for(String supplierId: order.getProductsMap().keySet()){
                if(!order.getArrive(supplierId)){
                    Supplier supplier = suppliersDAO.getSupplier(supplierId);
                    Date arriveDate = supplier.getDeal().getArrivalDate(order.getOrderDate());
                    Date now = new Date();
                    if(arriveDate.equals(now) || now.after(arriveDate)){
                        for(int catNum : order.getProductsMap().get(supplierId).keySet()){
                            DealProductInformation dpi = supplier.getDeal().getDealProductInfo(catNum);
                            int amount = order.getProductsMap().get(supplierId).get(catNum);
                            double pricePerOne = order.getPriceMapForSupplier(supplierId).get(catNum)/amount;
                            stc.submitAmount(order.getBranchId(),""+amount,pricePerOne,""+dpi.getProductId(),arriveDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());
                            ordersDAO.setArrived(order.getOrderId(),supplierId);
                        }
                    }
                }
            }
        }
    }

    public void removeDealProducts(int productId) throws SQLException {
        List<Supplier> suppliers = suppliersDAO.getAllSuppliers();
        for(Supplier supplier : suppliers){
            if(supplier.getDeal().hasEnoughOfProduct(productId,1)) {
                int catNum = supplier.getDeal().getDealProductByProductId(productId).getCatalogueNum();
                removeProductFromDeal(supplier.getCompanyId(), catNum);
            }
        }
    }

    public String describePeriodicOrders(String supplierId) throws SQLException {
        Map<Integer, PeriodicOrderAgreement> periodics = suppliersDAO.getSupplier(supplierId).getDeal().getPeriodics();
        String desc = "";
        for(int branchId : periodics.keySet()){
            desc += "For branch with number " + branchId + ":\n";
            String[] days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            desc += "\tEvery: ";
            for(int i = 0; i < 7; i++){
                if(periodics.get(branchId).getOrderDays()[i]){
                    desc += days[i] +", ";
                }
            }
            desc = desc.substring(0,desc.length()-2);
            desc += "\n\tSupplies:\n";
            for(int catNum : periodics.get(branchId).getProductAmounts().keySet()){
                DealProductInformation dpi = suppliersDAO.getSupplier(supplierId).getDeal().getDealProductInfo(catNum);
                Product product = productsDAO.getProduct(dpi.getProductId());
                desc += "\t\t" + periodics.get(branchId).getProductAmounts().get(catNum) + "x" +product.getName() + "\n";
            }
        }
        return desc;
    }

    public double getPriceFromSupplierBeforeDiscount(int orderId, String companyId) throws SQLException {
        Order order = ordersDAO.getOrder(orderId); // Checks if order exists
        return order.getPriceForSupplierBeforeDiscount(companyId);
    }

    public double getPriceTotalBeforeDiscount(int orderId) throws SQLException {
        Order order = ordersDAO.getOrder(orderId); // Checks if order exists
        return order.getPriceTotalBeforeDiscount();
    }

    public void addContactToOrder(int orderId, String companyId, String contactId) throws SQLException{
        contactsDAO.getContact(contactId); //this will throw error if contact doesn't exist
        if (!suppliersDAO.getSupplier(companyId).isContact(contactId))
            throw new IllegalArgumentException("A contact with ID " + contactId + " is not in the supplier's contacts list.");
        ordersDAO.assignContactToOrder(orderId, companyId, contactId);
        ordersDAO.getOrder(orderId).setContactForSupplier(companyId, contactId); //this will throw error if order doesn't exist
    }

    public String getOrderHistory() throws SQLException {
        List<Order> orders = ordersDAO.getOrderHistory();
        return gson.toJson(orders);
    }

    public String getEarliestSupplyTime(int productId, int amount) throws SQLException {
        List<Supplier> allSuppliers = suppliersDAO.getAllSuppliers();
        int timeDays = Integer.MAX_VALUE;
        boolean found = false;
        for(Supplier supplier : allSuppliers){
            if(supplier.getDeal().hasEnoughOfProduct(productId,amount)){
                found = true;
                int supplierTime = supplier.getDeal().getArrivalTime(new Date());
                if(supplierTime < timeDays){
                    timeDays = supplierTime;
                }
            }
        }
        if(!found){
            return "Product is not currently supplied";
        }
        return "" + timeDays + " days";
    }

    public void deployPeriodicOrders() throws SQLException {
        List<Supplier> suppliers = suppliersDAO.getAllSuppliers();
        for(Supplier supplier : suppliers){
            Map<Integer, PeriodicOrderAgreement> dueTodayPoas = supplier.getDeal().getDueToday();
            for(int branchId : dueTodayPoas.keySet()){
                Map<String, Map<Integer, Integer>> orderMap = new HashMap<>();
                orderMap.put(supplier.getCompanyId(),dueTodayPoas.get(branchId).getProductAmounts());
                Order order = ordersDAO.addOrder(orderMap,branchId);
                String contactId = dueTodayPoas.get(branchId).getAssignedContactId();
                if(contactId != null) {
                    Contact contact = contactsDAO.getContact(contactId);
                    ordersDAO.assignContactToOrder(order.getOrderId(),supplier.getCompanyId(),contactId);
                    String subject = "No Reply - New order from SuperLee to " + supplier.getCompanyName();
                    String content = orderEmailString(supplier,order,contact);
                    try {
                        contact.sendEmail(subject, content);
                    }
                    catch (MessagingException me){
                        //this is where a log might happen
                    }
                }
            }
        }
    }

    private String orderEmailString(Supplier supplier, Order order,Contact contact) throws SQLException {
        Map<Integer,Integer> productsMap = order.getProductsMap().get(supplier.getCompanyId());
        String wouldLikeToOrder = "";
        for(int catNum : productsMap.keySet()){
            DealProductInformation dpi = supplier.getDeal().getDealProductInfo(catNum);
            Product product = productsDAO.getProduct(dpi.getProductId());
            wouldLikeToOrder += productsMap.get(catNum) + " x " + product.getName() + ", Catalogue Num " + catNum + "\n";
        }
        return String.format(EMAIL_STRING_ORDER_MADE,contact.getName(),supplier.getCompanyName(),""+order.getBranchId(),wouldLikeToOrder);
    }

    public void clearOrderData() throws SQLException {
        ordersDAO.clearData();
    }

    public String getOrderContacts(int orderId) throws SQLException{
        Order order = ordersDAO.getOrder(orderId); // Checks if order exists
        return gson.toJson(order.getContactsForSuppliers());
    }

    public String getOrderProducts(int orderId) throws SQLException{
        Order order = ordersDAO.getOrder(orderId); // Checks if order exists
        return gson.toJson(order.getProductsMap());
    }

    public boolean canUpdatePeriodicOrder(String supplierId, int branchId) throws SQLException {
        return suppliersDAO.getSupplier(supplierId).getDeal().getPeriodics().get(branchId).canUpdate();
    }

    public String addSupplier(String companyId, String companyName, String location, String bankAccount, PaymentMethod paymentMethod, List<String> supplyAreas, boolean delivers, int daysToDeliver, boolean[] deliveryDays) throws SQLException {
        return gson.toJson(suppliersDAO.addSupplier(companyId, companyName, location, bankAccount, supplyAreas, paymentMethod, delivers, daysToDeliver, deliveryDays));
    }

    public void removeSupplier(String companyId) throws SQLException {
        suppliersDAO.removeSupplier(companyId);
    }

    public void updateSupplierInfo(String companyId, String companyName, String location, String bankAccount, List<String> supplyAreas) throws SQLException {
        suppliersDAO.getSupplier(companyId).updateSupplierInfo(companyName,location,bankAccount,supplyAreas);
        suppliersDAO.updateSupplierInfo(companyId, companyName, location, bankAccount, supplyAreas);
    }

    public void addNewSupplierContact(String companyId, String contactId, String name, String phoneNumber, String email, String address) throws SQLException {
        Supplier s = suppliersDAO.getSupplier(companyId);
        suppliersDAO.addNewSupplierContact(companyId, contactId, name, phoneNumber, email, address);
        s.addSupplierContact(contactId);
    }

    public void addExistingSupplierContact(String companyId, String contactId) throws SQLException{
        Supplier s = suppliersDAO.getSupplier(companyId); // checks if exists
        suppliersDAO.addExistingSupplierContact(companyId, contactId); // checks if exists
        s.addSupplierContact(contactId);
    }

    public void removeContact(String contactId) throws SQLException
    {
        for(Supplier s: suppliersDAO.getAllSuppliers())
            s.removeSupplierContact(contactId);
        for(Order o : ordersDAO.getOrderHistory())
            o.removeContact(contactId);
        //remove from periodicOrders

        contactsDAO.removeContact(contactId);
    }

    public void removeSupplierContact(String companyId, String contactId) throws SQLException {
        suppliersDAO.removeSupplierContact(companyId, contactId);
        suppliersDAO.getSupplier(companyId).removeSupplierContact(contactId);
        for(Order o : ordersDAO.getOrderHistory())
            o.removeSupplierContact(companyId);
    }

    public void updateContact(String contactId, String name, String phoneNumber, String email, String address) throws SQLException {
        contactsDAO.updateContactInfo(contactId, name, phoneNumber, email, address);
    }

    public String getContact(String contactId) throws SQLException {
        return gson.toJson(contactsDAO.getContact(contactId));
    }

    public String getSupplierContacts(String companyId) throws SQLException{
        List<String> contactIds = suppliersDAO.getSupplier(companyId).getSupplierContacts();
        List<Contact> contacts = new LinkedList<>();
        for (String contact : contactIds) {
            contacts.add(contactsDAO.getContact(contact));
        }
        return gson.toJson(contacts);
    }

    public String generateDealInfo(String supplierId) throws SQLException {
        Deal deal = suppliersDAO.getSupplier(supplierId).getDeal();
        String info = "";
        String[] days = new String[]{"Sunday", "Monday", "Tuesday","Wednesday", "Thursday", "Friday", "Saturday"};
        boolean[] deliveryDays = deal.getDeliveryDays();
        String daysString = "";
        boolean delivers = false;
        for(int i = 0; i < 7; i++){
            if(deliveryDays[i]){
                delivers = true;
                daysString += days[i] + ", ";
            }
        }
        if(delivers){
            info += "Supplier supplies on " + daysString.substring(0, daysString.length()-2) + "\n";
        }
        if(deal.getDelivers()){
            info += "Supplier delivers order\n";
        }else{
            info += "Supplier requires pickup of order\n";
        }
        if(!delivers){
            info += "Supplier order is ready within " + deal.getDaysToDeliver() + " days of order deployment\n";
        }
        info += "Payment method: " + deal.getPaymentMethod().name() + "\n";
        return info;
    }

    public void sendEmailToContact(String companyId, String contactId, String subject, String content) throws MessagingException, SQLException {
        contactsDAO.getContact(contactId); //will throw exception if contact doesn't exist
        if (!suppliersDAO.getSupplier(companyId).isContact(contactId))
            throw new IllegalArgumentException("A contact with ID " + contactId + " is not in the supplier's contacts list.");
        contactsDAO.getContact(contactId).sendEmail(subject, content);
    }

    public void updateDeal(String companyId, boolean delivers, int daysToDeliver, PaymentMethod paymentMethod, boolean[] deliveryDays)throws SQLException {
        suppliersDAO.getSupplier(companyId).getDeal().updateDeal(delivers, daysToDeliver, paymentMethod, deliveryDays);
        suppliersDAO.updateDeal(companyId, delivers, daysToDeliver, paymentMethod, deliveryDays);
    }

    public void addProductToDeal(String companyId, int catalogue, int productId, double price, int amount)throws SQLException {
        if(productsDAO.getProduct(productId) == null)
            throw new IllegalArgumentException("This product does not exist");
        Deal deal = suppliersDAO.getSupplier(companyId).getDeal(); // Checks if supplier and deal exist.
        deal.addProduct(catalogue, productId, price, amount);
    }

    public void updateDealProduct(String companyId, int catalogue, double price, int amount) throws SQLException {
        Deal deal = suppliersDAO.getSupplier(companyId).getDeal(); // Checks if supplier and deal exist.
        deal.updateProductInfo(catalogue, price, amount);
    }

    public void removeProductFromDeal(String companyId, int catalogue) throws SQLException{
        suppliersDAO.getSupplier(companyId).getDeal().removeProduct(catalogue);
    }

    public void addTotalPriceDiscount(String companyId, double percentage, double minimumPrice) throws SQLException{
        suppliersDAO.getSupplier(companyId).getDeal().addTotalPriceDiscount(new TotalPriceDiscount(percentage,minimumPrice));
    }

    public void addTotalProductsDiscount(String companyId, double percentage, int minimumAmount) throws SQLException{
        suppliersDAO.getSupplier(companyId).getDeal().addTotalProductsDiscount(new TotalProductsDiscount(percentage,minimumAmount));
    }

    private void addDiscountToDealProduct(String companyId, int catalogueNum, ProductAmountDiscount discount) throws SQLException
    {
        suppliersDAO.getSupplier(companyId).getDeal().getDealProductInfo(catalogueNum).addProductAmountDiscount(discount);
    }

    public void addProductAmountDiscount(String companyId, int catalogue, double percentage, int minimumAmount) throws SQLException{
        addDiscountToDealProduct(companyId, catalogue, new ProductAmountDiscount(percentage,minimumAmount));
    }

    public String getAllSuppliers() throws SQLException{
        return gson.toJson(suppliersDAO.getAllSuppliers());
    }

    public String getSuppliersThatSupplyProduct(int productId, int amount) throws SQLException{
        List<Supplier> suppliers = suppliersDAO.getAllSuppliers();
        List<Supplier> suppliersThatSupply = new LinkedList<>();
        for(Supplier supplier : suppliers){
            if(supplier.hasDeal()){
                if(supplier.getDeal().hasEnoughOfProduct(productId,amount)){
                    suppliersThatSupply.add(supplier);
                }
            }
        }
        return gson.toJson(suppliersThatSupply);
    }

    public String describeOrderSupplier(String supplierId, int orderId) throws SQLException {
        Order order = ordersDAO.getOrder(orderId);
        String desc = "";
        Map<Integer, Integer> productsMap = order.getProductsMap().get(supplierId);
        Map<Integer, Double> pricesMap = order.getPriceMapForSupplier(supplierId);
        Map<Integer, Double> pricesBeforeDiscountMap = order.getPriceForSupplierMapBeforeDiscount(supplierId);
        for (int catNum : productsMap.keySet()) {
            try {
                DealProductInformation dpi = suppliersDAO.getSupplier(supplierId).getDeal().getDealProductInfo(catNum);
                Product product = productsDAO.getProduct(dpi.getProductId());
                double priceBeforeDiscount = pricesBeforeDiscountMap.get(catNum);
                double priceFinal = pricesMap.get(catNum);
                desc += "\t" + productsMap.get(catNum) + " x " + product.getName() + ", Catalogue Num " + catNum + "\n";
                desc += "\t\t" + "Price before discount: " + priceBeforeDiscount + "₪, Discount: " + (priceBeforeDiscount - priceFinal) + "₪, Final Price: " + priceFinal + "₪\n";
            }catch (Exception e){
                double priceBeforeDiscount = pricesBeforeDiscountMap.get(catNum);
                double priceFinal = pricesMap.get(catNum);
                desc += "\t" + productsMap.get(catNum) + " x " + "Former product with Catalogue Num " + catNum + "\n";
                desc += "\t\t" + "Price before discount: " + priceBeforeDiscount + "₪, Discount: " + (priceBeforeDiscount - priceFinal) + "₪, Final Price: " + priceFinal + "₪\n";
            }
        }
        if(order.getContactsForSuppliers().containsKey(supplierId)){
            try {
                Contact contact = contactsDAO.getContact(order.getContactsForSuppliers().get(supplierId));
                desc += "\tAssigned Contact: " + contact.getName() + ", " + contact.getPhoneNumber() + ", " + contact.getEmail() + "\n";
            }catch (Exception e){
                desc += "\tAssigned Contact: Former contact with ID " + order.getContactsForSuppliers().get(supplierId) + "\n";
            }
        }
        desc += "\tTotal Price before discount: " + order.getPriceForSupplierBeforeDiscount(supplierId) + "₪\n";
        desc += "\tTotal Final Price: " + order.getPriceForSupplier(supplierId) + "₪";
        return desc;
    }

    public String orderHistoryForSupplier(String supplierId) throws SQLException {
        List<Order> orders = ordersDAO.getOrderHistory();
        String historyDesc = "";
        for(Order order : orders){
            if(order.ordersToSupplier(supplierId)) {
                historyDesc += "Order made on " + order.getOrderDate() + "\n";
                historyDesc += describeOrderSupplier(supplierId,order.getOrderId()) + "\n";
                historyDesc += "To branch number " + order.getBranchId();
            }
        }
        return historyDesc;
    }

    public String describeOrder(int orderId) throws SQLException {
        Order order = ordersDAO.getOrder(orderId);
        String description = "Order made on " + order.getOrderDate() + "\n";
        for(String supplierId : order.getProductsMap().keySet()){
            try {
                Supplier supplier = suppliersDAO.getSupplier(supplierId);
                description += "From " + supplier.getCompanyName() + " (" + supplierId + "):\n";
            }
            catch (Exception e){
                description += "From former supplier with ID " + supplierId + ":\n";
            }
            description += describeOrderSupplier(supplierId,orderId) + "\n";
        }
        description += "\nTo branch number " + order.getBranchId();
        return description;
    }

    public String branchOrderHistory(int branchId) throws SQLException{
        List<Order> orders = ordersDAO.getOrderHistory();
        String history = "";
        for(Order order : orders) {
            if (order.getBranchId() == branchId) {
                history += describeOrder(order.getOrderId()) + "\n";
            }
        }
        return history;
    }

    public String filterBySupplyArea(String supplyArea) throws SQLException {
        return gson.toJson(suppliersDAO.filterBySupplyArea(supplyArea));
    }

    public void clearSuppliersData() throws SQLException {
        suppliersDAO.clearData();
    }

    public String getSupplier(String companyId) throws SQLException{
        return gson.toJson(suppliersDAO.getSupplier(companyId));
    }

    public String getAllContacts() throws SQLException {
        return gson.toJson(contactsDAO.getAllContacts());
    }

    public String getDeal(String supplierId) throws SQLException {
        return gson.toJson(suppliersDAO.getSupplier(supplierId).getDeal());
    }

    public String getDealProducts(String supplierId) throws SQLException {
        return gson.toJson(suppliersDAO.getSupplier(supplierId).getDeal().getAllProducts());
    }

    public String getDealProduct(String supplierId, int catalogueNum) throws SQLException {
        return gson.toJson(suppliersDAO.getSupplier(supplierId).getDeal().getDealProductInfo(catalogueNum));
    }

    public boolean productExists(int productId){
        try{
            productsDAO.getProduct(productId);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void clearDealDiscounts(String companyId) throws SQLException {
        suppliersDAO.getSupplier(companyId).getDeal().clearDealDiscounts();
    }

    public void clearProductDiscounts(String companyId, int catalogueNum) throws SQLException {
        suppliersDAO.getSupplier(companyId).getDeal().clearProductDiscounts(catalogueNum);
    }

    public boolean hasDeal(String companyId) throws SQLException {
        return suppliersDAO.getSupplier(companyId).hasDeal();
    }

    public String getProductName(int productId) throws SQLException {
        return productsDAO.getProduct(productId).getName();
    }

    public String getSupplierIdsInOrder(int orderId) throws SQLException {
        Order order = ordersDAO.getOrder(orderId);
        List<String> ids = new LinkedList<>();
        for(String supplierId : order.getProductsMap().keySet()){
            ids.add(supplierId);
        }
        return gson.toJson(ids);
    }

    public String getDiscountDescriptions(String supplierId) throws SQLException {
        Deal deal = suppliersDAO.getSupplier(supplierId).getDeal();
        String descriptions = "";
        for(DealDiscount discount : deal.getAllDealDiscounts()){
            descriptions += discount.toString() + "\n";
        }
        for(DealProductInformation dpi : deal.getAllProducts()){
            for(ProductDiscount discount : dpi.getDiscounts()){
                descriptions += discount.toString(getProductName(dpi.getProductId()));
            }
        }
        return descriptions;
    }

    public void updateContactDealUpdated(String supplierId, String contactId) throws MessagingException, SQLException {
        Contact contact = contactsDAO.getContact(contactId);
        Supplier supplier = suppliersDAO.getSupplier(supplierId);
        String subject = "No Reply - SuperLee Deal with " + supplier.getCompanyName() + " has been altered";
        String content = String.format(EMAIL_STRING_DEAL_ALTERED,contact.getName(),supplier.getCompanyName(),generateDealInfo(supplierId));
        contact.sendEmail(subject,content);
    }

    public void updateContactOrderMade(String supplierId, String contactId, int orderId) throws MessagingException, SQLException {
        Contact contact = contactsDAO.getContact(contactId);
        Supplier supplier = suppliersDAO.getSupplier(supplierId);
        Order order = ordersDAO.getOrder(orderId);
        String subject = "No Reply - New order from SuperLee to " + supplier.getCompanyName();
        String content = orderEmailString(supplier,order,contact);
        contact.sendEmail(subject,content);
    }

    public boolean hasContacts(String supplierId) throws SQLException {
        return suppliersDAO.getSupplier(supplierId).hasContacts();
    }

    // functions i added

    public String orderIdsAndDatesSupplier(String supplierId) throws SQLException {
        List<Order> orders = ordersDAO.getOrderHistory();
        List<String> idsAndDates = new ArrayList<>();
        for(Order order : orders){
            if(order.ordersToSupplier(supplierId)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String date = dateFormat.format(order.getOrderDate());
                idsAndDates.add(order.getOrderId() + " - " + date);
            }
        }
        return gson.toJson(idsAndDates);
    }

    public String getSupplierContactsComplement(String companyId) throws SQLException{
        List<Contact> allContacts = contactsDAO.getAllContacts();
        List<String> supplierContactsIds = suppliersDAO.getSupplier(companyId).getSupplierContacts();
        List<String> contacts = new LinkedList<>();
        for (Contact contact : allContacts) {
            if(!supplierContactsIds.contains(contact.getContactId()))
                contacts.add(contact.getContactId() + " - " + contact.getName());
        }
        return gson.toJson(contacts.toArray(new String[0]));
    }

    public String getUnusedDealProducts(String companyId, List<Integer> used) throws SQLException{
        List<DealProductInformation> allProducts = suppliersDAO.getSupplier(companyId).getDeal().getAllProducts();
        List<DealProductInformation> unused = new LinkedList<>();

        for (DealProductInformation product : allProducts) {
            if(!used.contains(product.getCatalogueNum()))
                unused.add(product);
        }
        return gson.toJson(unused);
    }

    public String getDealProductsComplement(String supplierId) throws SQLException {
        List<Product> allProducts = productsDAO.getAll();
        List<DealProductInformation> dealProducts = suppliersDAO.getSupplier(supplierId).getDeal().getAllProducts();
        List<String> complement = new ArrayList<>();
        for(Product p : allProducts)
        {
            boolean exists = false;
            for(DealProductInformation pi : dealProducts)
            {
                if(p.getProductId() == pi.getProductId())
                    exists = true;
            }
            if(!exists)
            {
                complement.add(p.getProductId() + " - " + p.getName());
            }
        }

        return gson.toJson(complement.toArray(new String[0]));
    }

    public String getPeriodicOrderProductsDescription(String supplierId, int branchId) throws SQLException {
        String description = "";
        Map<Integer, Integer> products = suppliersDAO.getSupplier(supplierId).getDeal().getPeriodicOrder(branchId).getProductAmounts();
        for(int catNum : products.keySet())
        {
            int productId = suppliersDAO.getSupplier(supplierId).getDeal().getDealProductInfo(catNum).getProductId();
            description += products.get(catNum) + " X " + getProductName(productId)  + "\n";
        }
        return description;
    }

    public String getPeriodicOrders(String supplierId) throws SQLException {
        Map<Integer, PeriodicOrderAgreement> periodics = suppliersDAO.getSupplier(supplierId).getDeal().getPeriodics();
        return gson.toJson(periodics);
    }

    public String getPeriodicOrder(String supplierId, int branch) throws SQLException {
        Map<Integer, PeriodicOrderAgreement> periodics = suppliersDAO.getSupplier(supplierId).getDeal().getPeriodics();
        if(periodics.containsKey(branch))
            return gson.toJson(periodics.get(branch));
        throw new IllegalArgumentException("Supplier " + supplierId + " does not have a periodic order to branch " + branch);
    }

    public String getProductDiscountsDescriptions(String supplierId, int catNum) throws SQLException {
        List<ProductDiscount> discounts = suppliersDAO.getSupplier(supplierId).getDeal().getDealProductInfo(catNum).getDiscounts();
        List<String> descriptions = new ArrayList<>();
        int productId = suppliersDAO.getSupplier(supplierId).getDeal().getDealProductInfo(catNum).getProductId();
        Product product = productsDAO.getProduct(productId);
        for(ProductDiscount discount : discounts)
        {
            descriptions.add(discount.toString(product.getName()));
        }
        return gson.toJson(descriptions);
    }

    public String describeSingleOrder(String supplierId, int orderId) throws SQLException {
        Order order = ordersDAO.getOrder(orderId);
        String desc = "";
        desc += "Order made on " + order.getOrderDate() + "\n";
        desc += describeOrderSupplier(supplierId,order.getOrderId()) + "\n";
        desc += "To branch number " + order.getBranchId();
        return desc;
    }

    public String filterByName(String name) throws SQLException {
        List<Supplier> allSuppliers = suppliersDAO.getAllSuppliers();
        List<Supplier> filtered = new ArrayList<>();
        for(Supplier supplier : allSuppliers)
            if(supplier.getCompanyName().toLowerCase().contains(name.toLowerCase()))
                filtered.add(supplier);
        return gson.toJson(filtered);
    }

    public String getDealDiscountsDescriptions(String supplierId) throws SQLException {
        List<DealDiscount> discounts = suppliersDAO.getSupplier(supplierId).getDeal().getAllDealDiscounts();
        List<String> descriptions = new ArrayList<>();
        for(DealDiscount discount : discounts)
            descriptions.add(discount.toString());
        return gson.toJson(descriptions);
    }
}