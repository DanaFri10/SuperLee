package suppliers.presentation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import suppliers.business.Contact;
import suppliers.business.Order;
import suppliers.business.Supplier;
import suppliers.business.SuppliersController;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.*;

public class CreateOrderView implements Menu{
    private Map<Integer, Integer> allProductsAmount;
    private Map<String, Map<Integer, Integer>> suppliersProductsAmount;
    private SuppliersController sc;
    private boolean done;
    private Scanner scanner;
    private Gson gson;
    private int branchId;

    public CreateOrderView(SuppliersController sc, int branchId)
    {
        this.branchId = branchId;
        this.allProductsAmount = new HashMap<>();
        this.suppliersProductsAmount = new HashMap<>();
        this.sc = sc;
        this.done = false;
        scanner = new Scanner(System.in);
        gson = new Gson();
    }

    private void selectProductForOptimalOrder()
    {
        System.out.println("Product ID?");
        String product = scanner.nextLine();
        if(product.toLowerCase().equals("done")){
            done = true;
            return;
        }
        try {
            int productInt = Integer.parseInt(product);
            if(!sc.productExists(productInt)){
                System.out.println("Product with ID " + productInt + " does not exist");
                return;
            }
            System.out.println("Amount?");
            String amount = scanner.nextLine();
            if(amount.toLowerCase().equals("done")){
                done = true;
                return;
            }
            try {
                int amountInt = Integer.parseInt(amount);
                if(allProductsAmount.containsKey(productInt)){
                    allProductsAmount.put(productInt, allProductsAmount.get(productInt)+amountInt);
                }else{
                    allProductsAmount.put(productInt, amountInt);
                }
            }
            catch (Exception e) {
                System.out.println("Not a number.");
            }
        }
        catch (Exception e){
            System.out.println("No supplier can supply this order.");
        }
    }

    private void selectProductForCustomOrder()
    {
        System.out.println("Product ID?");
        String product = scanner.nextLine();
        if(product.toLowerCase().equals("done")){
            done = true;
            return;
        }
        try {
            int productInt = Integer.parseInt(product);
            if(!sc.productExists(productInt)){
                System.out.println("Product with ID " + productInt + " does not exist");
                return;
            }
            System.out.println("Amount?");
            String amount = scanner.nextLine();
            if(amount.toLowerCase().equals("done")){
                done = true;
                return;
            }
            int amountInt = Integer.parseInt(amount);

            System.out.println("Supplier ID?");
            String supplierId = scanner.nextLine();
            if(amount.toLowerCase().equals("done")){
                done = true;
                return;
            }

            if(suppliersProductsAmount.containsKey(supplierId)){
                if(suppliersProductsAmount.get(supplierId).containsKey(productInt))
                    suppliersProductsAmount.get(supplierId).put(productInt, suppliersProductsAmount.get(supplierId).get(productInt).intValue()+amountInt);
                else
                    suppliersProductsAmount.get(supplierId).put(productInt, amountInt);
            }
            else{
                Map<Integer, Integer> supAmounts = new HashMap<>();
                supAmounts.put(productInt, amountInt);
                suppliersProductsAmount.put(supplierId, supAmounts);
            }
        }
        catch (Exception e){
            System.out.println("Not all suppliers can provide this order.");
        }
    }

    @Override
    public void start() {
        System.out.println(MainView.BAR);
        System.out.println("Would you like to create an order manually or an optimal order?");
        System.out.println("1. Manual order");
        System.out.println("2. Optimal order (Cheapest way to order as many products as possible from a single supplier)");
        System.out.println("3. Fastest order");
        boolean accepted = false;
        while(!accepted) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice == 1) {
                    accepted = true;
                    System.out.println("Please specify the product ID, amount and suppliers of the products you want to order. Type 'Done' when you're done.");
                    while(!done){
                        selectProductForCustomOrder();
                    }
                    if(!suppliersProductsAmount.isEmpty()) {
                        try {
                            String orderString = sc.createCustomOrder(suppliersProductsAmount,branchId);
                            Order order = gson.fromJson(orderString, Order.class);
                            addContacts(order.getOrderId());
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            return;
                        }
                    }
                }

                else if (choice == 2) {
                    accepted = true;
                    System.out.println("Please specify the product ID and amount of the products you want to order. Type 'Done' when you're done.");
                    while(!done){
                        selectProductForOptimalOrder();
                    }
                    if(!allProductsAmount.isEmpty()) {
                        try {
                            String orderString = sc.createOptimalOrderJson(allProductsAmount,branchId);
                            Order order = gson.fromJson(orderString, Order.class);
                            System.out.println("The system has calculated the following order to be the most optimal:");
                            System.out.println(sc.describeOrder(order.getOrderId()));
                            addContacts(order.getOrderId());
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            return;
                        }
                    }
                }

                else if (choice == 3) {
                    accepted = true;
                    System.out.println("Please specify the product ID and amount of the products you want to order. Type 'Done' when you're done.");
                    while(!done){
                        selectProductForOptimalOrder();
                    }
                    if(!allProductsAmount.isEmpty()) {
                        try {
                            String orderString = sc.createFastestOrder(allProductsAmount, branchId);
                            Order order = gson.fromJson(orderString, Order.class);
                            System.out.println("The system has calculated the following order to be the most optimal:");
                            System.out.println(sc.describeOrder(order.getOrderId()));
                            addContacts(order.getOrderId());
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            return;
                        }
                    }
                }

                else{
                    System.out.println("Unknown command");
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void addContacts(int orderId) throws SQLException {
        String json = sc.getSupplierIdsInOrder(orderId);
        Type listType = new TypeToken<LinkedList<String>>(){}.getType();
        List<String> supplierIds = gson.fromJson(json, listType);
        for(String supplierId : supplierIds){
            Supplier supplier = gson.fromJson(sc.getSupplier(supplierId), Supplier.class);
            if(sc.hasContacts(supplierId)){
                System.out.println("Please choose a contact of " + supplier.getCompanyName() + " to add to this order:");
                int index = 1;
                String contactsJson = sc.getSupplierContacts(supplierId);
                Type contactsListType = new TypeToken<LinkedList<Contact>>(){}.getType();
                List<Contact> contacts = gson.fromJson(contactsJson, contactsListType);
                for (Contact cm : contacts) {
                    System.out.println(index + ". " + cm.getName() + ", " + cm.getPhoneNumber() + " via " + cm.getEmail());
                    index++;
                }
                boolean accepted = false;
                int chosenIndex = 0;
                while (!accepted){
                    try {
                        chosenIndex = Integer.parseInt(scanner.nextLine()) - 1;
                        if(chosenIndex < 0 || chosenIndex >= contacts.size()){
                            System.out.println("Please choose a contact from the list");
                        }else{
                            accepted = true;
                        }
                    }catch (Exception e){
                        System.out.println("Not a number");
                    }
                }
                Contact chosen = contacts.get(chosenIndex);
                try {
                    sc.addContactToOrder(orderId,supplierId,chosen.getContactId());
                    System.out.println("Would you like to update this contact about the order via Email? Y/N");
                    if(scanner.nextLine().toLowerCase().charAt(0) == 'y'){
                        sc.updateContactOrderMade(supplierId,chosen.getContactId(),orderId);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
