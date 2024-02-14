# IDs
Eyal Shapiro: 213958804

Dana Friedman: 213967599

Nir Aharoni: 213161953

Idan Hefetz: 213633332

# Libraries Used
GSON - for serializing objects to JSON

javax.activation and sun.mail.javax - For sending emails to contacts

sqlite-jbdc - For communication with SQLite Database

# Instructions
### Running the program
1. Locate the `adss2023_v03.jar` file. It is located in the release. Make sure a `SuperLee.db` SQLite database file with the correct tables is located in the same folder.
2. On a windows machine, open the Command Prompt and navigate to the folder where the jar file is located using the cd command.
3. Run the command: `java –jar adss2023_v03.jar <CLI|GUI> <StoreManager|WarehouseWorker|SuppliersManager>`
### Login/Register screen
Upon starting the program, you will be greeted with a Login screen. You can login with one of the following usernames/passwords (depending on chosen role):
1. Login with username `EyalShap` and password `eyal123` to have the Stock Manager role
2. Login with username `Niro` and password `nir123` to have the Warehouse Worker role
3. Login with username `Danaf` and password `dana123` or username `HefIdan` and password `idan123` to have the Suppliers Connections Manager role
Each role has access to a different menu.

## Instructions for CLI

### Stock Manager Menu
As a Stock Manager, you can do the following things:

1. Create a discount on a product or category .
2. See the products in a category.
3. Set a time periodic between recieving defective product reports.
4. See data on inventory and the different amounts of each product.
5. See a shortage report of the branch as well as order the products in shortage.
6. See info on a product sold in the branch.
7. Update a product universally.
8. Delete a product universally.
9. Access the Product Explorer (explained later).
10. Access the Supplier Module Menu (explained later).
11. Access the Warehouse Worker Menu (explained later).

### Warehouse Worker Menu
As a Warehouse Worker, you can do the following things:

1. Report missing or bought instances of products.
2. Report defective instances of products.
3. Add new instances of a product to the branch.
4. Update the location of an instance of a product.
5. Update or assign an expiration date for an instance of a product.
6. See a shortage report of the branch as well as order the products in shortage.
7. Make an order

### Product Explorer
The product explorer allows the Stock Manager to browse and see information about the different products sold in the branch. The products are sorted by category.
You can also add, remove or update different products. By following the instructions on the screen, you can traverse the different products and categories as if they were folders on a file system.
To add a product to a category that does not yet exist, simply follow the future path into the category. The product explorer will treat this as an empty category and you will be able to add a product there.

## Suppliers Module Menu
As a Suppliers Manager or Stock Manager, you can access the Suppliers Module Menu. the Suppliers Module Menu allows you to handle the different suppliers and deals.
The Suppliers Module Menu is made up of the Suppliers List menu.

### Suppliers List Menu
In the suppliers menu, you can view info about any supplier that has a deal with the company. You can choose a supplier from the list to see info about it or update its details, or add a new supplier.

When viewing a supplier, you can perform actions such as update the deal with the supplier, add a product to the deal, add a periodic order from the supplier, add a contact to the supplier, add discounts to the supplier, remove the supplier, etc.

# Example Information
The Database supplied includes example information so that the functionality of the system can be tested.

The tables of the example information are:
### Suppliers
![image](https://github.com/Group-F1/ADSS_Group_F/assets/102603028/30de5c33-796f-4926-b1c2-a7d1da148dfd)
### SupplyAreas
![image](https://github.com/Group-F1/ADSS_Group_F/assets/102603028/8d9367ac-c85a-4c73-b31d-3dcaf2d32d61)
### Contacts
![image](https://github.com/Group-F1/ADSS_Group_F/assets/102603028/c4d12c1a-e419-4138-b3d3-f33c2c2937e0)
### SupplierContacts
![image](https://github.com/Group-F1/ADSS_Group_F/assets/102603028/1e26c9e6-05dc-4155-bc09-3b208641297c)
### Products
![image](https://github.com/Group-F1/ADSS_Group_F/assets/102603028/a0a4add0-6727-45ec-9ede-9e95d36739ab)
### DealProducts
![image](https://github.com/Group-F1/ADSS_Group_F/assets/102603028/72145aeb-4f51-4714-9bf1-6b33372bf8ff)
### DealDiscounts
![image](https://github.com/Group-F1/ADSS_Group_F/assets/102603028/604cfd72-1819-4688-82b3-f49f10562a04)
### TotalProductsDiscount
![image](https://github.com/Group-F1/ADSS_Group_F/assets/102603028/7f09beae-7cc6-4ae9-91b3-24ddac1e582b)
### TotalPriceDiscounts
![image](https://github.com/Group-F1/ADSS_Group_F/assets/102603028/85b8de71-fa3e-4ecc-ad3f-988179953e1f)
### DealProductsDiscounts
![image](https://github.com/Group-F1/ADSS_Group_F/assets/102603028/67532d62-6f81-4835-96f7-3b4e20be1b15)
### ProductAmountDiscounts
![image](https://github.com/Group-F1/ADSS_Group_F/assets/102603028/6c970447-fb81-4062-af36-3ffaeaa5c62f)
### PeriodicOrders
![image](https://github.com/Group-F1/ADSS_Group_F/assets/102603028/71798d9b-3cd4-4d2b-8db2-6d124662686f)
### PeriodicOrderProducts
![image](https://github.com/Group-F1/ADSS_Group_F/assets/102603028/2016cb53-f8fa-4df6-9cc5-71f6477fd394)
### Branches
![image](https://github.com/Group-F1/ADSS_Group_F/assets/102603028/a90d1ff4-5ca0-463c-8d43-b939e85667ed)
### BranchProducts
![image](https://github.com/Group-F1/ADSS_Group_F/assets/102603028/808fc899-dd40-45f8-ad09-866c519235a4)
### BranchProductInstances
![image](https://github.com/Group-F1/ADSS_Group_F/assets/102603028/b547a012-21a7-4dea-8241-97bf83fd25a9)
### CategoryDiscounts
![image](https://github.com/Group-F1/ADSS_Group_F/assets/102603028/a2b9e438-1e32-4c7d-9970-17c77146d495)
