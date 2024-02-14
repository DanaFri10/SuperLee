package stock.business;

import java.time.LocalDate;

public class BranchDiscount {
    private LocalDate beginningDate;
    private LocalDate expiredDate;
    private double discountNumericValue;
    public BranchDiscount(LocalDate beginningDate, LocalDate expiredDate, double percentage) {
        this.beginningDate = beginningDate;
        this.expiredDate = expiredDate;
        this.discountNumericValue = percentage;
    }

    public double getDiscountNumericValue() {
        return discountNumericValue;
    }

    public LocalDate getBeginningDate() {
        return beginningDate;
    }


    public LocalDate getExpiredDate() {
        return expiredDate;
    }



}
