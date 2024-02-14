package stock.presentation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import stock.business.Branch;
import stock.business.BranchProduct;
import stock.business.Product;
import stock.business.StockController;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ProductExplorer {
    private int branchId;
    private StockController stc;
    private String currPath;
    private boolean done;
    private Gson gson;
    public ProductExplorer(StockController stc, int branchId){
        this.stc = stc;
        this.branchId = branchId;
        currPath = "";
        done = false;
        gson = new Gson();
    }

    public void start(){
        while(!done){
            showCategory();
        }
    }
    private void showCategory(){
        try {
            System.out.println("Current Path: " + currPath);
            String json = stc.getImmidiateSubCategories(branchId, currPath);
            Type listType = new TypeToken<LinkedList<String>>(){}.getType();
            List<String> categoryList = gson.fromJson(json, listType);
            for(String cat : categoryList){
                System.out.println(cat);
            }
            String productsJson = stc.getProductOnlyIn(branchId, currPath);
            Type pListType = new TypeToken<LinkedList<BranchProduct>>(){}.getType();
            List<BranchProduct> productList = gson.fromJson(productsJson, pListType);
            int i = 0;
            for(BranchProduct bp : productList){
                i++;
                System.out.print(i + ". ");
                String productJson = stc.getProduct(bp.getId());
                Product product = gson.fromJson(productJson, Product.class);
                System.out.println(product.getName());
            }
            System.out.println("Type \"add\" to add a new product to the branch, type the next category path to view products in it");
            System.out.println("Type .. to go back to the parent category. To select a product from the list, type ~ followed by the product's list number");
            System.out.println("Type . to exit");
            String nextPath = Reader.read("");
            if(nextPath.equals(".")){
                done = true;
                return;
            }
            if(nextPath.startsWith("~")){
                int index = Integer.parseInt(nextPath.substring(1))-1;
                BranchProduct chosen = productList.get(index);
                System.out.println(stc.getBranchProductReport(branchId, chosen.getId()));
                System.out.println("Would you like to...?");
                System.out.println("1. Update product info");
                System.out.println("2. Remove product from branch");
                System.out.println("Other: Go back to explorer");
                String choice = Reader.read("");
                switch (choice){
                    case "1":
                        String newPath = chosen.getPath();
                        if(Reader.read("Would you like to change the product's category? y/n").toLowerCase().charAt(0) == 'y'){
                            newPath = Reader.read("");
                        }
                        stc.updateProductInfo(branchId,""+chosen.getId(),Reader.read("New selling price? "),newPath,Reader.read("Demand? "),Reader.read("Place in store? "), Reader.read("Minimum amount? "));
                        break;
                    case "2":
                        stc.removeBranchProduct(branchId,chosen.getId());
                        break;
                }
            }
            else if(nextPath.equals("add")){
                addBranchProduct();
            }
            else if(nextPath.equals("..")){
                if(!currPath.contains("/")){
                    currPath = "";
                }else {
                    currPath = currPath.substring(0, currPath.lastIndexOf('/'));
                }
            }else {
                if(!currPath.isEmpty()){
                    currPath += "/";
                }
                currPath += nextPath;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void addBranchProduct() throws Exception {
        boolean recentlyAdded = false;
        String productId = "";
        if(Reader.read("Would you like to add a universal product? (y/n): ").toLowerCase().charAt(0) == 'y'){
            productId = Reader.read("product ID?: ");
            stc.addProduct(productId, Reader.read("Product Name?: "), Reader.read("Manufacturer?: "));
            recentlyAdded = true;
        }
        Writer.print("press enter to start and press enter between submits\n");
        Writer.print("products id (only if you have not inserted now his universal product)\n");
        Writer.print("selling price, demand and place in store, amount in store\n");
        Writer.print("Remainder.\n");
        Writer.print("To go back to the last menu write back\n");
        Writer.print("\n");
        String answer = Reader.read("");
        switch(answer){
            case("back"):
                return;
            default:
                if(!recentlyAdded) {
                    String productsJson = stc.getAllProducts();
                    Type pListType = new TypeToken<LinkedList<Product>>(){}.getType();
                    List<Product> productList = gson.fromJson(productsJson, pListType);
                    System.out.println("Please choose a universal product's product ID");
                    for(Product product: productList){
                        System.out.println(product.getName() + ", ID: " + product.getProductId() + ". Manufactured by " + product.getManufacturer());
                    }
                    productId = Reader.read("product id: ");
                }
                String sellingPrice = Reader.read("selling price: ");
                String demand = Reader.read("demand: ");
                String placeInStore = Reader.read("place in store: ");
                String minimum = Reader.read("mimimum amount: ");
                try{
                    stc.submitBranchProduct(branchId, productId, sellingPrice, currPath, demand, placeInStore, minimum);
                }
                catch (Exception e) {
                    Writer.print(e.getMessage());
                }
        }
    }
}
