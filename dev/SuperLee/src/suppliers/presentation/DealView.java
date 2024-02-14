package suppliers.presentation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import suppliers.business.*;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.*;

public class DealView implements Menu{
    private String supplierId;
    private SuppliersController sc;
    private boolean exit;
    private Scanner scanner;
    private Gson gson;

    private final String[] SUPPORTED_DEAL_DISCOUNTS = new String[]{"percentage discount applied over a total amount of products in an order", "percentage discount applied over a total price of an order"};
    private final String[] SUPPORTED_PRODUCT_DISCOUNTS = new String[]{"percentage discount applied over an amount of the product in an order"};

    public DealView(String supplierId, SuppliersController sc){
        this.supplierId = supplierId;
        this.sc = sc;
        this.exit = false;
        this.scanner = new Scanner(System.in);
        this.gson = new Gson();
    }
    private void showDeal() {
        try {
            System.out.println(sc.generateDealInfo(supplierId));
            System.out.println("Products Supplied:");
            String productsJson = sc.getDealProducts(supplierId);
            Type listType = new TypeToken<LinkedList<DealProductInformation>>(){}.getType();
            List<DealProductInformation> products = gson.fromJson(productsJson, listType);
            for (DealProductInformation dpi : products) {
                String productName = sc.getProductName(dpi.getProductId());
                System.out.println(dpi.getCatalogueNum() + ": " + dpi.getAmount() + " " + productName + ", " + dpi.getPrice() + "₪ per " + productName);
            }
            System.out.println("\nDiscounts:");
            System.out.println(sc.getDiscountDescriptions(supplierId));
            System.out.println("\nPeriodic Orders:");
            System.out.println(sc.describePeriodicOrders(supplierId));
            System.out.println("\nOptions:");
            System.out.println("1. Return to supplier view");
            System.out.println("2. Add product to deal");
            System.out.println("3. Edit deal discounts");
            System.out.println("4. Edit discounts on a product");
            System.out.println("5. Update product information");
            System.out.println("6. Remove product from deal");
            System.out.println("7. Add a periodic order from this supplier");
            System.out.println("8. Update a periodic order from this supplier");
            System.out.println("9. Remove a periodic order from this supplier");
            boolean accepted = false;
            while(!accepted) {
                String command = scanner.nextLine();
                switch (command) {
                    case "1":
                        accepted = true;
                        exit = true;
                        break;
                    case "2":
                        accepted = true;
                        addProduct();
                        break;
                    case "3":
                        accepted = true;
                        addDealDiscount();
                        break;
                    case "4":
                        accepted = true;
                        addProductDiscount();
                        break;
                    case "5":
                        accepted = true;
                        updateProduct();
                        break;
                    case "6":
                        accepted = true;
                        removeProduct();
                        break;
                    case "7":
                        accepted = true;
                        makePeriodicOrder();
                        break;
                    case "8":
                        accepted = true;
                        updatePeriodicOrder();
                        break;
                    case "9":
                        accepted = true;
                        removePeriodicOrder();
                        break;
                    default:
                        System.out.println("Unknown command");
                        break;
                }
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void updatePeriodicOrder() throws SQLException {
        System.out.println("What branch's periodic order would you like to update?");
        int branchId = Integer.parseInt(scanner.nextLine());
        System.out.println("What would you like to update?");
        List<String> options = new LinkedList<>();
        options.add("Products");
        if(!sc.supplierHasFixedDays(supplierId)){
            options.add("Deployment Days");
        }
        if(sc.hasContacts(supplierId)){
            options.add("Assigned Contact");
        }
        for(int i = 0; i < options.size(); i++){
            System.out.println((i+1) + ". " + options.get(i));
        }
        boolean accepted = false;
        while(!accepted) {
            try {
                int choiceIndex = Integer.parseInt(scanner.nextLine());
                if (choiceIndex < 1 || choiceIndex > options.size()) {
                    System.out.println("Please choose an option from the list");
                    break;
                }
                if(options.get(choiceIndex-1).equals("Products")){
                    accepted = true;
                    updatePoProducts(branchId);
                } else if (options.get(choiceIndex-1).equals("Deployment Days")) {
                    accepted = true;
                    updatePoDays(branchId);
                } else if (options.get(choiceIndex-1).equals("Assigned Contact")) {
                    accepted = true;
                    updatePoContact(branchId);
                }
            } catch (NumberFormatException e) {
                System.out.println("not a a number");
            }
        }
    }

    private void removePeriodicOrder() throws SQLException {
        System.out.println("What branch's periodic order would you like to remove?");
        int branchId = Integer.parseInt(scanner.nextLine());
        sc.removePeriodicOrder(supplierId,branchId);
    }

    private void updatePoProducts(int branchId) throws SQLException {
        Map<Integer, Integer> productsMap = new HashMap<>();
        boolean done = false;
        System.out.println("Please specify the product ID, amount and suppliers of the products you want to order. Type 'Done' when you're done.");
        while(!done){
            System.out.println("Product ID?");
            String product = scanner.nextLine();
            if(product.toLowerCase().equals("done")){
                done = true;
                break;
            }
            try {
                int productInt = Integer.parseInt(product);
                if(!sc.productExists(productInt)){
                    System.out.println("Product with ID " + productInt + " does not exist");
                    break;
                }
                System.out.println("Amount?");
                String amount = scanner.nextLine();
                if(amount.toLowerCase().equals("done")){
                    done = true;
                    break;
                }
                try {
                    int amountInt = Integer.parseInt(amount);
                    if(productsMap.containsKey(productInt)){
                        productsMap.put(productInt, productsMap.get(productInt)+amountInt);
                    }else{
                        productsMap.put(productInt, amountInt);
                    }
                }
                catch (Exception e) {
                    System.out.println("Not a number.");
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        sc.setPeriodicOrderProducts(supplierId,branchId,productsMap, false);
    }

    private void updatePoDays(int branchId) throws SQLException {
        boolean[] deployDays = new boolean[]{false, false, false, false, false, false, false};
        System.out.println("What days of the week will the order deploy on?");
        System.out.println("Write the numbers of the days, as listed, separated by commas");
        String[] days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (int i = 0; i < 7; i++) {
            System.out.println((i + 1) + ". " + days[i]);
        }
        System.out.println("Example: 1,4 ==> Order is deployed on sundays and wednesdays");
        boolean accepted = false;
        while(!accepted) {
            String daysResponse = scanner.nextLine();
            if (!daysResponse.isEmpty()) {
                String[] numbers = daysResponse.split(",");
                try {
                    for (String number : numbers) {
                        deployDays[Integer.parseInt(number) - 1] = true;
                    }
                    accepted = true;
                }catch (Exception e){
                    System.out.println("Please follow the described format");
                }
            }else{
                System.out.println("You must specify deployment days for this supplier");
            }
        }
        sc.setPeriodicOrderDays(supplierId,branchId,deployDays);
    }

    private void updatePoContact(int branchId) throws SQLException {
        System.out.println("Please choose a contact of the supplier to assign to this periodic order:");
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
        sc.setPeriodicOrderContact(supplierId,branchId,chosen.getContactId());
    }

    private void makePeriodicOrder() throws SQLException {
        Map<Integer, Integer> productsMap = new HashMap<>();
        boolean done = false;
        System.out.println("What branch is the periodic order to?");
        int branchId = Integer.parseInt(scanner.nextLine());
        System.out.println("Please specify the product ID, amount and suppliers of the products you want to order. Type 'Done' when you're done.");
        while(!done){
            System.out.println("Product ID?");
            String product = scanner.nextLine();
            if(product.toLowerCase().equals("done")){
                done = true;
                break;
            }
            try {
                int productInt = Integer.parseInt(product);
                if(!sc.productExists(productInt)){
                    System.out.println("Product with ID " + productInt + " does not exist");
                    break;
                }
                System.out.println("Amount?");
                String amount = scanner.nextLine();
                if(amount.toLowerCase().equals("done")){
                    done = true;
                    break;
                }
                try {
                    int amountInt = Integer.parseInt(amount);
                    if(productsMap.containsKey(productInt)){
                        productsMap.put(productInt, productsMap.get(productInt)+amountInt);
                    }else{
                        productsMap.put(productInt, amountInt);
                    }
                }
                catch (Exception e) {
                    System.out.println("Not a number.");
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        sc.setPeriodicOrderProducts(supplierId,branchId,productsMap, true);
        if(!sc.supplierHasFixedDays(supplierId)){
            boolean[] deployDays = new boolean[]{false, false, false, false, false, false, false};
            System.out.println("What days of the week will the order deploy on?");
            System.out.println("Write the numbers of the days, as listed, separated by commas");
            String[] days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            for (int i = 0; i < 7; i++) {
                System.out.println((i + 1) + ". " + days[i]);
            }
            System.out.println("Example: 1,4 ==> Order is deployed on sundays and wednesdays");
            boolean accepted = false;
            while(!accepted) {
                String daysResponse = scanner.nextLine();
                if (!daysResponse.isEmpty()) {
                    String[] numbers = daysResponse.split(",");
                    try {
                        for (String number : numbers) {
                            deployDays[Integer.parseInt(number) - 1] = true;
                        }
                        accepted = true;
                    }catch (Exception e){
                        System.out.println("Please follow the described format");
                    }
                }else{
                    System.out.println("You must specify deployment days for this supplier");
                }
            }
            sc.setPeriodicOrderDays(supplierId,branchId,deployDays);
        }
        if(sc.hasContacts(supplierId)){
            System.out.println("Please choose a contact of the supplier to assign to this periodic order:");
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
            sc.setPeriodicOrderContact(supplierId,branchId,chosen.getContactId());
        }
    }

    private void addProduct(){
        System.out.println("What is the ID of the product in the store (not the catalogue number)?");
        int productId = Integer.parseInt(scanner.nextLine());
        System.out.println("Supplier's Catalogue Number?");
        int catalogue = Integer.parseInt(scanner.nextLine());
        System.out.println("Price? (from the supplier)");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.println("How much of this product can the supplier supply?");
        int amount = Integer.parseInt(scanner.nextLine());
        try {
            sc.addProductToDeal(supplierId,catalogue,productId,price,amount);
            System.out.println("Product Added!");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void updateProduct(){
        try {
            String productsJson = sc.getDealProducts(supplierId);
            Type listType = new TypeToken<LinkedList<DealProductInformation>>(){}.getType();
            List<DealProductInformation> products = gson.fromJson(productsJson, listType);
            System.out.println("What product would you like to update?");
            int index = 1;
            for (DealProductInformation dpi : products) {
                System.out.println(index + ". " + sc.getProductName(dpi.getProductId()) + "(catalogue: " + dpi.getCatalogueNum() + ")");
                index++;
            }
            DealProductInformation dpi = products.get(Integer.parseInt(scanner.nextLine()) - 1);
            System.out.println("New price?");
            double price = Double.parseDouble(scanner.nextLine());
            System.out.println("New amount supplied by the supplier?");
            int amount = Integer.parseInt(scanner.nextLine());
            sc.updateDealProduct(supplierId,dpi.getCatalogueNum(),price,amount);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void removeProduct(){
        try {
            String productsJson = sc.getDealProducts(supplierId);
            Type listType = new TypeToken<LinkedList<DealProductInformation>>(){}.getType();
            List<DealProductInformation> products = gson.fromJson(productsJson, listType);
            System.out.println("What product would you like to remove?");
            int index = 1;
            for (DealProductInformation dpi : products) {
                System.out.println(index + ". " + sc.getProductName(dpi.getProductId()) + "(catalogue: " + dpi.getCatalogueNum() + ")");
                index++;
            }
            DealProductInformation dpi = products.get(Integer.parseInt(scanner.nextLine()) - 1);
            sc.removeProductFromDeal(supplierId,dpi.getCatalogueNum());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void addDealDiscount(){
        try {
            System.out.println("Please choose one of the following options:");
            for (int i = 0; i < SUPPORTED_DEAL_DISCOUNTS.length; i++) {
                System.out.println((i + 1) + ". Add a " + SUPPORTED_DEAL_DISCOUNTS[i]);
            }
            System.out.println((SUPPORTED_DEAL_DISCOUNTS.length + 1) + ". Clear all deal discounts");
            boolean accepted = false;
            while(!accepted) {
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        accepted = true;
                        addTotalProductDiscount();
                        break;
                    case "2":
                        accepted = true;
                        addTotalPriceDiscount();
                        break;
                    case "3":
                        accepted = true;
                        sc.clearDealDiscounts(supplierId);
                        System.out.println("Discounts cleared!");
                        break;
                    default:
                        System.out.println("Unknown command");
                        break;
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void addProductDiscount(){
        try {
            String productsJson = sc.getDealProducts(supplierId);
            Type listType = new TypeToken<LinkedList<DealProductInformation>>(){}.getType();
            List<DealProductInformation> products = gson.fromJson(productsJson, listType);
            System.out.println("What product would you like to update?");
            int index = 1;
            for (DealProductInformation dpi : products) {
                System.out.println(index + ". " + sc.getProductName(dpi.getProductId()) + "(catalogue: " + dpi.getCatalogueNum() + ")");
                index++;
            }
            DealProductInformation dpi = products.get(Integer.parseInt(scanner.nextLine()) - 1);
            System.out.println("Please choose one of the following options:");
            for (int i = 0; i < SUPPORTED_PRODUCT_DISCOUNTS.length; i++) {
                System.out.println((i + 1) + ". Add a " + SUPPORTED_PRODUCT_DISCOUNTS[i]);
            }
            System.out.println((SUPPORTED_PRODUCT_DISCOUNTS.length + 1) + ". Clear all discounts on this product");
            boolean accepted = false;
            while(!accepted) {
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        accepted = true;
                        addProductAmountDiscount(dpi.getCatalogueNum());
                        break;
                    case "2":
                        accepted = true;
                        sc.clearProductDiscounts(supplierId,dpi.getCatalogueNum());
                        System.out.println("Product Discounts cleared!");
                        break;
                    default:
                        System.out.println("Unknown command");
                        break;
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //DEAL DISCOUNTS
    private void addTotalProductDiscount() throws SQLException {
        System.out.println("Minimum amount of products to apply the discount above?");
        int amount = Integer.parseInt(scanner.nextLine());
        System.out.println("What percentage should the discount subtract from the price?");
        double percentage = Double.parseDouble(scanner.nextLine().replaceAll("%",""))/100d;
        sc.addTotalProductsDiscount(supplierId,percentage,amount);
        System.out.println("Discount added!");
    }
    private void addTotalPriceDiscount() throws SQLException {
        System.out.println("Minimum price to apply the discount above?");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.println("What percentage should the discount subtract from the price?");
        double percentage = Double.parseDouble(scanner.nextLine().replaceAll("%",""))/100d;
        sc.addTotalPriceDiscount(supplierId,percentage,price);
        System.out.println("Discount added!");
    }

    //PRODUCT DISCOUNT
    private void addProductAmountDiscount(int catalogueNum) throws SQLException {
        System.out.println("Minimum amount of the product to apply the discount above?");
        int amount = Integer.parseInt(scanner.nextLine());
        System.out.println("What percentage should the discount subtract from the price of the product?");
        double percentage = Double.parseDouble(scanner.nextLine().replaceAll("%",""))/100d;
        sc.addProductAmountDiscount(supplierId,catalogueNum,percentage,amount);
        System.out.println("Product discount added!");;
    }


    @Override
    public void start() {
        while(!exit){
            System.out.println(MainView.BAR);
            showDeal();
        }
    }
}
