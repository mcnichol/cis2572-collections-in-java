package org.mcnichol.driver;

import java.util.Currency;
import java.util.ResourceBundle;

/**
 * <h1>Loan Schedule Driver</h1>
 * <p>
 * Logic from H/W Ch36 translated as driver for user interface.
 */
public class LoanScheduleDriver {

    private final double loanAmount;
    private final int numOfYears;
    private final double annualInterestRate;

    private double monthlyInterestRate;
    private double monthlyPayment;

    public LoanScheduleDriver(String loanAmount, String numberYears, String annualInterest) {
        this.loanAmount = Double.parseDouble(loanAmount);
        this.numOfYears = Integer.parseInt(numberYears);
        this.annualInterestRate = Double.parseDouble(annualInterest);
    }

    private void calculatePaymentRates() {
        monthlyInterestRate = annualInterestRate / 1200;
        monthlyPayment = loanAmount * monthlyInterestRate / (1 - (Math.pow(1 / (1 + monthlyInterestRate), numOfYears * 12)));
    }

    public String getLoanSchedule(ResourceBundle bundle) {
        calculatePaymentRates();
        String currencySymbol = Currency.getInstance(bundle.getLocale()).getSymbol();

        StringBuilder sb = new StringBuilder();

        sb.append(bundle.getString("paySchedule.monthlyPayment"))
                .append(": \t")
                .append(currencySymbol)
                .append(getMonthlyPayment())
                .append("\n")
                .append(bundle.getString("paySchedule.totalPayment"))
                .append(": \t")
                .append(currencySymbol)
                .append(getTotalPayment())
                .append("\n");

        String format = String.format("%-15s%-15s%-15s%-15s\n",
                bundle.getString("paySchedule.payment"),
                bundle.getString("paySchedule.interest"),
                bundle.getString("paySchedule.principal"),
                bundle.getString("paySchedule.balance")
        );
        sb.append(format);

        double balance = loanAmount;

        for (int i = 1; i <= numOfYears * 12; i++) {
            double interest = monthlyInterestRate * balance;
            double principal = monthlyPayment - interest;
            balance = balance - principal;

            sb.append(String.format("%-15d%s%-15.2f%s%-15.2f%s%-15.2f\n", i,
                    currencySymbol,
                    interest,
                    currencySymbol,
                    principal,
                    currencySymbol,
                    balance));
        }

        return sb.toString();
    }

    public String getTotalPayment() {
        return String.valueOf((int) (monthlyPayment * 12 * numOfYears * 100) / 100.0);
    }

    public String getMonthlyPayment() {
        return String.valueOf((int) (monthlyPayment * 100) / 100.0);
    }
}
