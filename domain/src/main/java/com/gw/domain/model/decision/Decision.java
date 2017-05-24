package com.gw.domain.model.decision;

import java.io.Serializable;
import java.util.Date;
import java.util.TreeMap;

/**
 * Created by vadym on 17.05.17.
 */

public abstract class Decision implements Serializable{
    private double mAmount;
    private Priority mPriority;

    public double getAmount() {
        return mAmount;
    }

    public void setAmount(double amount) {
        this.mAmount = amount;
    }

    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority priority) {
        this.mPriority = priority;
    }

    public abstract TreeMap<Date, Double> getPaymentMap();

    public enum Priority {
        HIGH,
        MIDDLE,
        LOW;

        public static Priority fromInteger(int x) {
            switch(x) {
                case 0:
                    return HIGH;
                case 1:
                    return MIDDLE;
                case 2:
                    return LOW;
            }
            return null;
        }
    }

}
