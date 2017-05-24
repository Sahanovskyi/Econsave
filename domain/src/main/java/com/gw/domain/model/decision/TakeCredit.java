package com.gw.domain.model.decision;


import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;


public class TakeCredit extends Decision{
    private int mCreditDuration;
    private double mInterestRate;


    @Override
    public TreeMap<Date, Double> getPaymentMap(){
        TreeMap<Date, Double> paymentMap = new TreeMap<>();

        Calendar cal = Calendar.getInstance();
        double monthlyBalance = 0;
        double monthlyPayment = calculateMonthlyPayment();

        for (int i = 0; i <= mCreditDuration; i++, monthlyBalance -= monthlyPayment) {
            paymentMap.put(cal.getTime(), monthlyBalance);
            cal.add(Calendar.MONTH, 1); //minus number would decrement the days
        }

        return paymentMap;
    }

    private double calculateMonthlyPayment(){
        final double monthlyInterestRate = mInterestRate/12/100;
        final double denominator = Math.pow(1 + monthlyInterestRate, mCreditDuration) - 1;
        double monthlyPayment = getAmount() * (monthlyInterestRate + (monthlyInterestRate / denominator));

        return monthlyPayment;
    }

    public int getCreditDuration() {
        return mCreditDuration;
    }

    public double getInterestRate() {
        return mInterestRate;
    }

    public void setCreditDuration(int creditDuration) {
        this.mCreditDuration = creditDuration;
    }

    public void setInterestRate(double interestRate) {
        this.mInterestRate = interestRate;
    }

}
