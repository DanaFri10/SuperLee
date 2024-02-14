package suppliers.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static suppliers.business.Utils.*;

public class Supplier {
    private String companyId;
    private String companyName;
    private String location;
    private String bankAccount;
    private List<String> supplierContacts;
    private transient Deal deal;
    private List<String> supplyAreas;

    public Supplier(String companyId, String companyName, String location, String bankAccount, List<String> supplyAreas)
    {
        if(!isLegalId(companyId))
            throw new IllegalArgumentException("Company ID (" + companyId + ") is illegal.");
        verifySupplierInfo(companyName, location, bankAccount);

        this.companyId = companyId;
        this.companyName = companyName;
        this.location = location;
        this.bankAccount = bankAccount;
        this.supplierContacts = new ArrayList<>();
        this.deal = null;
        if(supplyAreas == null) this.supplyAreas = new ArrayList<>();
        else {
            this.supplyAreas = supplyAreas;
            for (int i = 0; i < supplyAreas.size(); i++)
                supplyAreas.set(i, supplyAreas.get(i).toLowerCase());
        }
    }

    public Supplier()
    {}


    private void verifySupplierInfo(String companyName, String location, String bankAccount)
    {
        if(isFieldEmpty(companyName) | isFieldEmpty(bankAccount) )
            throw new IllegalArgumentException("All fields must not be empty.");
        if(!isStringLengthBetween(companyName, 3, 50))
            throw new IllegalArgumentException("Company name must have between 3 and 50 characters.");
        if(!isStringLengthBetween(location, 3, 50))
            throw new IllegalArgumentException("Company location must have between 3 and 50 characters.");
        if(!isLegalBankAccountNumber(bankAccount))
            throw new IllegalArgumentException("Bank account number (" + bankAccount + ") is illegal.");
    }

    public boolean hasDeal(){
        return deal != null;
    }

    public Deal getDeal()
    {
        if(!hasDeal())
            throw new IllegalArgumentException(String.format("The supplier with ID %s doesn't have a deal.", companyId));

        return deal;
    }

    public void addDeal(boolean delivers, int daysToDeliver, boolean[] deliveryDays, PaymentMethod paymentMethod) throws SQLException {
        if(hasDeal())
            throw new IllegalArgumentException(String.format("The supplier with ID %s already has a deal.", companyId));

        this.deal = new Deal(delivers, daysToDeliver, deliveryDays, paymentMethod, this);
    }

    public void updateSupplierInfo(String companyName, String location, String bankAccount, List<String> supplyAreas)
    {
        verifySupplierInfo(companyName, location, bankAccount);

        this.companyName = companyName;
        this.location = location;
        this.bankAccount = bankAccount;
        if(supplyAreas == null) this.supplyAreas = new ArrayList<>();
        else {
            this.supplyAreas = supplyAreas;
            for (int i = 0; i < supplyAreas.size(); i++)
                supplyAreas.set(i, supplyAreas.get(i).toLowerCase());
        }
    }

    public boolean isContact(String contactId)
    {
        return supplierContacts.contains(contactId);
    }

    public void addSupplierContact(String contactId)
    {
        if(!isContact(contactId))
            supplierContacts.add(contactId);
    }

    public void removeSupplierContact(String contactId)
    {
        supplierContacts.remove(contactId);
    }

    public boolean supplyAreaExists(String supplyArea)
    {
        return supplyAreas.contains(supplyArea.toLowerCase());
    }

    public boolean hasContacts(){
        return !supplierContacts.isEmpty();
    }

    /*
    public void addSupplyArea(String supplyArea)
    {
        if(supplyAreaExists(supplyArea))
            throw new IllegalArgumentException(String.format("This supply area (%s) already exists.", supplyArea));
        if(!isStringLengthBetween(supplyArea, 3, 50))
            throw new IllegalArgumentException("Supply area must contain 3-50 characters.");

        supplyAreas.add(supplyArea.toLowerCase());
    }

    public void removeSupplyArea(String supplyArea)
    {
        if(!supplyAreaExists(supplyArea))
            throw new IllegalArgumentException(String.format("This supply area (%s) doesn't exist.", supplyArea));

        supplyAreas.remove(supplyArea);
    }*/

    public String getCompanyId()
    {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getLocation() {
        return location;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public List<String> getSupplierContacts() {
        return supplierContacts;
    }

    public List<String> getSupplyAreas() {
        return supplyAreas;
    }

    public boolean equals(Object other)
    {
        if(!(other instanceof Supplier)) return false;
        if((deal == null & ((Supplier)other).deal!=null) | (deal != null & ((Supplier)other).deal==null)) return false;
        return companyId == ((Supplier)other).companyId &&
                companyName == ((Supplier)other).companyName &&
                location == ((Supplier)other).location &&
                bankAccount == ((Supplier)other).bankAccount &&
                supplierContacts.equals(((Supplier)other).supplierContacts) &&
                ((deal == null && ((Supplier)other).deal==null) || deal.equals(((Supplier)other).deal)) &&
                supplyAreas.equals(((Supplier)other).supplyAreas);

    }
}

