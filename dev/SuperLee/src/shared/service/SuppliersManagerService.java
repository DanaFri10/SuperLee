package shared.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import shared.ControllerFactory;
import shared.LocalDateAdapter;
import stock.business.StockController;
import stock.business.User;
import stock.business.UserRole;
import suppliers.business.PaymentMethod;
import suppliers.business.PeriodicOrderAgreement;
import suppliers.business.Supplier;
import suppliers.business.SuppliersController;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public class SuppliersManagerService extends AuthenticatedService {
    private StockController stockController;
    private SuppliersController suppliersController;
    private Gson gson;
    static SuppliersManagerService service = null;

    private SuppliersManagerService(StockController stc, SuppliersController spc) {
        super(stc);
        this.stockController = stc;
        this.suppliersController = spc;
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe()).create();
    }

    public static SuppliersManagerService getService() throws Exception {
        if(service == null){
            service = new SuppliersManagerService(ControllerFactory.getStc(), ControllerFactory.getSpc());
        }
        return service;
    }

    @Override
    protected void authenticate(String username) throws Exception {
        if(stockController.getRole(username) != UserRole.SupplierManager && stockController.getRole(username) != UserRole.StoreManager){
            throw new UnsupportedOperationException("Only Supplier Connection Managers and Store Managers are allowed to use these functions");
        }
    }

    //main view - list of suppliers
    public String listSuppliers(String username) {
        try {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.getAllSuppliers(), false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    //supplier card
    public String getSupplier(String username, String supplierId) {
        try {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.getSupplier(supplierId), false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String getOrdersHistory(String username, String supplierId) {
        try {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.orderHistoryForSupplier(supplierId), false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String getOrderIdsAndDates(String username, String supplierId) {
        try {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.orderIdsAndDatesSupplier(supplierId), false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String listContacts(String username, String supplierId) {
        try {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.getSupplierContacts(supplierId), false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String listContactsComplement(String username, String supplierId) {
        try {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.getSupplierContactsComplement(supplierId), false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String getUnusedDealProducts(String username, String supplierId, List<Integer> used) {
        try {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.getUnusedDealProducts(supplierId, used), false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String addExistingContact(String username, String supplierId, String contactId) {
        try {
            authenticate(username);
            suppliersController.addExistingSupplierContact(supplierId, contactId);
            return gson.toJson(new Response("Contact added successfully", false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String addNewContact(String username, String companyId, String contactId, String name, String phoneNumber, String email, String address) {
        try {
            authenticate(username);
            suppliersController.addNewSupplierContact(companyId, contactId, name, phoneNumber, email, address);
            return gson.toJson(new Response("Contact added successfully", false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String removeContactFromSupplier(String username, String supplierId, String contactId) {
        try {
            authenticate(username);
            suppliersController.removeSupplierContact(supplierId, contactId);
            return gson.toJson(new Response("Contact removed successfully", false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String updateContact(String username, String contactId, String name, String phoneNumber, String email, String address) {
        try {
            authenticate(username);
            suppliersController.updateContact(contactId, name, phoneNumber, email, address);
            return gson.toJson(new Response("Contact updated successfully", false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String removeSupplier(String username, String supplierId) {
        try {
            authenticate(username);
            suppliersController.removeSupplier(supplierId);
            return gson.toJson(new Response("Supplier removed successfully", false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    //create / update supplier
    public String updateSupplier(String username, String companyId, String companyName, String location, String bankAccount, PaymentMethod paymentMethod, List<String> supplyAreas, boolean delivers, int daysToDeliver, boolean[] deliveryDays) {
        try {
            authenticate(username);
            suppliersController.updateSupplierInfo(companyId, companyName, location, bankAccount, supplyAreas);
            suppliersController.updateDeal(companyId, delivers, daysToDeliver, paymentMethod, deliveryDays);
            return gson.toJson(new Response("Supplier updated successfully", false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String addSupplier(String username, String companyId, String companyName, String location, String bankAccount, PaymentMethod paymentMethod, List<String> supplyAreas, boolean delivers, int daysToDeliver, boolean[] deliveryDays) {
        try {
            authenticate(username);
            String res = suppliersController.addSupplier(companyId, companyName, location, bankAccount, paymentMethod, supplyAreas, delivers, daysToDeliver, deliveryDays);
            return gson.toJson(new Response(res, false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    //deal view
    public String getDeal(String username, String supplierId) {
        try {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.getDeal(supplierId), false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String getDealProducts(String username, String supplierId) {
        try {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.getDealProducts(supplierId), false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String getProductsComplement(String username, String supplierId) {
        try {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.getDealProductsComplement(supplierId), false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String getProductName(String username, int productId)
    {
        try
        {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.getProductName(productId), false, null));
        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String getPeriodicOrderProductsDescription(String username, String supplier, int catNum)
    {
        try
        {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.getPeriodicOrderProductsDescription(supplier, catNum), false, null));
        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String getPeriodicOrders(String username, String supplierId)
    {
        try
        {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.getPeriodicOrders(supplierId), false, null));
        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String getPeriodicOrder(String username, String supplierId, int branchId)
    {
        try
        {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.getPeriodicOrder(supplierId, branchId), false, null));
        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String getDealDiscounts(String username, String supplierId) {
        try {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.getDiscountDescriptions(supplierId), false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String getProductDiscounts(String username, String supplierId, int catNum) {
        try {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.getProductDiscountsDescriptions(supplierId, catNum), false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String addTotalPriceDiscount(String username, String supplierId, double percentage, double minPrice) {
        try {
            authenticate(username);
            suppliersController.addTotalPriceDiscount(supplierId, percentage, minPrice);
            return gson.toJson(new Response("Total price discount was added successfully", false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String addTotalProductsDiscount(String username, String supplierId, double percentage, int minAmount) {
        try {
            authenticate(username);
            suppliersController.addTotalProductsDiscount(supplierId, percentage, minAmount);
            return gson.toJson(new Response("Total products discount was added successfully", false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String clearDealDiscounts(String username, String supplierId) {
        try {
            authenticate(username);
            suppliersController.clearDealDiscounts(supplierId);
            return gson.toJson(new Response("Deal discounts were cleared successfully", false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }


    //deal product view
    public String getDealProduct(String username, String supplierId, int catNum) {
        try {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.getDealProduct(supplierId, catNum), false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String addDealProduct(String username, String supplierId, int catNum, int productId, double price, int amount) {
        try {
            authenticate(username);
            suppliersController.addProductToDeal(supplierId, catNum, productId, price, amount);
            return gson.toJson(new Response("Deal product added successfully", false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String updateDealProduct(String username, String supplierId, int catNum, double price, int amount) {
        try {
            authenticate(username);
            suppliersController.updateDealProduct(supplierId, catNum, price, amount);
            return gson.toJson(new Response("Deal product updated successfully", false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String removeDealProduct(String username, String supplierId, int catNum) {
        try {
            authenticate(username);
            suppliersController.removeProductFromDeal(supplierId, catNum);
            return gson.toJson(new Response("Deal product removed successfully", false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String addProductDiscount(String username, String supplierId, int catNum, double percentage, int minAmount) {
        try {
            authenticate(username);
            suppliersController.addProductAmountDiscount(supplierId, catNum, percentage, minAmount);
            return gson.toJson(new Response("A discount was added to the deal product successfully", false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String clearProductDiscounts(String username, String supplierId, int catNum) {
        try {
            authenticate(username);
            suppliersController.clearProductDiscounts(supplierId, catNum);
            return gson.toJson(new Response("Product discounts were cleared successfully", false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String getProductDiscountsDescription(String username, String supplierId, int catNum)
    {
        try {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.getProductDiscountsDescriptions(supplierId, catNum), false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String getDealDiscountsDescription(String username, String supplierId)
    {
        try {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.getDealDiscountsDescriptions(supplierId), false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }


    public String setPeriodicOrderProducts(String username, String supplierId, int branchId, Map<Integer, Integer> productsMap, boolean newOrder) {
        try {
            authenticate(username);
            suppliersController.setPeriodicOrderProducts(supplierId, branchId, productsMap, newOrder);
            return gson.toJson(new Response("Periodic order products were set successfully", false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String setPeriodicOrderOrderDays(String username, String supplierId, int branchId, boolean[] days) {
        try {
            authenticate(username);
            suppliersController.setPeriodicOrderDays(supplierId, branchId, days);
            return gson.toJson(new Response("Periodic order days were set successfully", false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String setPeriodicOrderContact(String username, String supplierId, int branchId, String contactId) {
        try {
            authenticate(username);
            suppliersController.setPeriodicOrderContact(supplierId, branchId, contactId);
            return gson.toJson(new Response("Periodic order contact was set successfully", false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    // contact view
    public String getContact(String username, String contactId) {
        try {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.getContact(contactId), false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    // order view
    public String orderDescription(String username, String supplierId, int orderId)
    {
        try
        {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.describeSingleOrder(supplierId, orderId), false, null));
        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }




}





