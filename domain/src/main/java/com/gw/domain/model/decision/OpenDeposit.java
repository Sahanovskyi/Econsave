package com.gw.domain.model.decision;

import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

/**
 * Created by vadym on 02.05.17.
 */

public class OpenDeposit extends Decision {
    private int mDepositDuration;
    private double mInterestRate;
    private DepositType mDepositType;

    @Override
    public TreeMap<Date, Double> getPaymentMap() {
        switch (mDepositType) {
            case MONTHLY_CAPITALIZATION_INTEREST:
                return monthlyCapitalization();
            case QUARTERLY_CAPITALIZATION_INTEREST:
                return quarterlyCapitalization();
            case MONTHLY_PAYMENT_INTEREST:
                return monthlyPayment();
            case QUARTERLY_PAYMENT_INTEREST:
                return quarterlyPayment();
            case PAYMENT_OF_INTEREST_AT_MATURITY:
                return maturityPayment();
        }
        return new TreeMap<>();
    }


    private TreeMap<Date, Double> monthlyCapitalization() {
        TreeMap<Date, Double> map = new TreeMap<>();

        Calendar calendar = Calendar.getInstance();
        map.put(calendar.getTime(), 0.0);
        calendar.add(Calendar.DATE, 1);
        map.put(calendar.getTime(), getAmount() * (-1));

        double percent = mInterestRate * 1 / 12;
        double final_amount = (getAmount() * Math.pow((1 + percent), mDepositDuration)) - getAmount() ;

        calendar.add(Calendar.MONTH, mDepositDuration);

        map.put(calendar.getTime(), final_amount);
        calendar.add(Calendar.DATE, 2);
        map.put(calendar.getTime(), final_amount);

        return map;
    }

    private TreeMap<Date, Double> quarterlyCapitalization() {
        TreeMap<Date, Double> map = new TreeMap<>();

        Calendar calendar = Calendar.getInstance();
        map.put(calendar.getTime(), 0.0);
        calendar.add(Calendar.DATE, 1);

        map.put(calendar.getTime(), getAmount() * (-1));

        double percent = mInterestRate * 4 / 12;
        double final_amount = (getAmount() * Math.pow((1 + percent), mDepositDuration)) - getAmount();

        calendar.add(Calendar.MONTH, mDepositDuration);

        map.put(calendar.getTime(), final_amount);
        calendar.add(Calendar.DATE, 2);
        map.put(calendar.getTime(), final_amount);


        return map;
    }


    private TreeMap<Date, Double> monthlyPayment() {
        TreeMap<Date, Double> map = new TreeMap<>();

        Calendar calendar = Calendar.getInstance();
        double tmp_balance = getAmount() * (-1);
        double monthly_payment_amount = getAmount() * mInterestRate / 12;

        map.put(calendar.getTime(), tmp_balance);

        for (int i = 0; i < mDepositDuration; i++) {
            calendar.add(Calendar.MONTH, 1);
            tmp_balance += monthly_payment_amount;
            map.put(calendar.getTime(), tmp_balance);
        }
        tmp_balance += getAmount();

        map.put(calendar.getTime(), tmp_balance);
        calendar.add(Calendar.DATE, 2);
        map.put(calendar.getTime(), tmp_balance);

        return map;
    }

    private TreeMap<Date, Double> quarterlyPayment() {
        TreeMap<Date, Double> map = new TreeMap<>();

        Calendar calendar = Calendar.getInstance();
        double tmp_balance = getAmount() * (-1);
        double monthly_payment_amount = getAmount() * mInterestRate / 12;

        map.put(calendar.getTime(), tmp_balance);
        int i;

        for (i = 1; i <= mDepositDuration; i += 3) {
            calendar.add(Calendar.MONTH, 3);
            tmp_balance += monthly_payment_amount;
            map.put(calendar.getTime(), tmp_balance);
        }
        tmp_balance += getAmount();
        if (i < mDepositDuration)
            calendar.add(Calendar.MONTH, mDepositDuration - i);

        map.put(calendar.getTime(), tmp_balance);
        calendar.add(Calendar.DATE, 2);
        map.put(calendar.getTime(), tmp_balance);

        return map;
    }

    private TreeMap<Date, Double> maturityPayment() {
        TreeMap<Date, Double> map = new TreeMap<>();

        Calendar calendar = Calendar.getInstance();
        map.put(calendar.getTime(), getAmount() * (-1));

        double monthly_payment_amount = getAmount() * mInterestRate / 12;
        calendar.add(Calendar.MONTH, mDepositDuration);
        map.put(calendar.getTime(), monthly_payment_amount * mInterestRate + getAmount());
        calendar.add(Calendar.DATE, 2);
        map.put(calendar.getTime(), monthly_payment_amount * mInterestRate + getAmount());

        return map;
    }


    public int getDepositDuration() {
        return mDepositDuration;
    }

    public void setDepositDuration(int depositDuration) {
        this.mDepositDuration = depositDuration;
    }

    public double getInterestRate() {
        return mInterestRate;
    }

    public void setInterestRate(double interestRate) {
        this.mInterestRate = interestRate / 100;
    }

    public void setDepositType(DepositType mDepositType) {
        this.mDepositType = mDepositType;
    }


    public enum DepositType {
        MONTHLY_CAPITALIZATION_INTEREST,
        QUARTERLY_CAPITALIZATION_INTEREST,
        MONTHLY_PAYMENT_INTEREST,
        QUARTERLY_PAYMENT_INTEREST,
        PAYMENT_OF_INTEREST_AT_MATURITY;

        public static DepositType fromInteger(int x) {
            switch (x) {
                case 0:
                    return MONTHLY_CAPITALIZATION_INTEREST;
                case 1:
                    return QUARTERLY_CAPITALIZATION_INTEREST;
                case 2:
                    return MONTHLY_PAYMENT_INTEREST;
                case 3:
                    return QUARTERLY_PAYMENT_INTEREST;
                case 4:
                    return PAYMENT_OF_INTEREST_AT_MATURITY;
            }
            return null;
        }

    }
}
