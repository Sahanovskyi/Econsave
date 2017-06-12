package com.gw.domain.interactor.forecast;

import com.gw.domain.model.Interval;
import com.gw.domain.model.Period;
import com.gw.domain.model.TransactionItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class TransactionsManager {

    @Inject
    public TransactionsManager() {
    }

    public static Date getStartDate(Interval interval) {
        Calendar calendar = Calendar.getInstance();

        switch (interval) {
            case LAST_WEEK:
                calendar.add(Calendar.DATE, -7);
                break;
            case LAST_MONTH:
                calendar.add(Calendar.MONTH, -1);
                break;
            case LAST_QUARTER:
                calendar.add(Calendar.MONTH, -3);
                break;
            case LAST_HALF_YEAR:
                calendar.add(Calendar.MONTH, -6);
                break;
            case LAST_YEAR:
                calendar.add(Calendar.YEAR, -1);
                break;
            case LAST_5_YEARS:
                calendar.add(Calendar.YEAR, -5);
                break;
        }
        return calendar.getTime();
    }

    public static Date getEndDate(Interval interval) {
        Calendar calendar = Calendar.getInstance();

        switch (interval) {
            case LAST_WEEK:
                calendar.add(Calendar.DATE, 7);
                break;
            case LAST_MONTH:
                calendar.add(Calendar.MONTH, 1);
                break;
            case LAST_QUARTER:
                calendar.add(Calendar.MONTH, 3);
                break;
            case LAST_HALF_YEAR:
                calendar.add(Calendar.MONTH, 6);
                break;
            case LAST_YEAR:
                calendar.add(Calendar.YEAR, 1);
                break;
            case LAST_5_YEARS:
                calendar.add(Calendar.YEAR, 5);
                break;
        }
        return calendar.getTime();
    }

    public TreeMap<Date, Double> getBalanceMap(List<TransactionItem> transactions, Interval interval) {
        if (transactions == null || transactions.isEmpty())
            return null;

        Date end = new Date();
        Date start = getStartDate(interval);

        TreeMap<Date, Double> balanceMap = new TreeMap<>();

        for (TransactionItem item : transactions) {
            if (item.getDate().getTime() <= end.getTime() && item.getDate().getTime() > start.getTime()) {
                balanceMap.put(item.getDate(), item.getBalance());
            }
        }


        long startTime = start.getTime();
        final long DAY_LENGTH = 1000 * 60 * 60 * 24;
        TreeMap<Date, Double> finalMap = new TreeMap<>();
        finalMap.putAll(balanceMap);

        for (Map.Entry<Date, Double> entry : balanceMap.entrySet()) {

            for (; startTime <= end.getTime(); startTime += DAY_LENGTH) {
                if (entry.getKey().getTime() > startTime + DAY_LENGTH) {
                    finalMap.put(new Date(startTime), entry.getValue());
                } else {
                    startTime += DAY_LENGTH;
                    break;
                }
            }
        }

        return groupBalanceByInterval(balanceMap, interval);
    }

    public TreeMap<Date, Double> getIncomeMap(List<TransactionItem> transactions, Interval interval) {
        if (transactions == null || transactions.isEmpty())
            return null;

        Date end = new Date();
        Date start = getStartDate(interval);

        TreeMap<Date, Double> incomeMap = new TreeMap<>();

        for (TransactionItem item : transactions) {
            if (item.getDate().getTime() <= end.getTime() && item.getDate().getTime() > start.getTime()
                    && item.getAmount() > 0) {

                if (!incomeMap.containsKey(item.getDate())) {
                    incomeMap.put(item.getDate(), item.getAmount());
                } else {
                    double tmp = incomeMap.get(item.getDate()) + item.getAmount();
                    incomeMap.put(item.getDate(), tmp);
                }
            }
        }

        return incomeMap;
    }

    public TreeMap<Date, Double> getExpensesMap(List<TransactionItem> transactions, Interval interval) {
        if (transactions == null || transactions.isEmpty())
            return null;

        Date end = new Date();
        Date start = getStartDate(interval);

        TreeMap<Date, Double> expensesMap = new TreeMap<>();

        for (TransactionItem item : transactions) {

            if (item.getDate().getTime() <= end.getTime() && item.getDate().getTime() > start.getTime()
                    && item.getAmount() < 0) {

                if (!expensesMap.containsKey(item.getDate())) {
                    expensesMap.put(item.getDate(), item.getAmount() * (-1));
                } else {
                    double tmp = expensesMap.get(item.getDate()) + item.getAmount() * (-1);
                    expensesMap.put(item.getDate(), tmp);
                }
            }
        }

        return expensesMap;
    }

    public double getIncome(List<TransactionItem> transactions, Interval interval) throws NullPointerException {
        if (transactions == null || transactions.isEmpty())
            throw new NullPointerException("Transaction item list cannot be empty!");

        Date end = new Date();
        Date start = getStartDate(interval);
        double income = 0;
        for (TransactionItem item : transactions) {
            if (item.getDate().getTime() <= end.getTime() && item.getDate().getTime() > start.getTime() && item.getAmount() > 0) {
                income += item.getAmount();
            }
        }
        return income;
    }

    public double getExpenses(List<TransactionItem> transactions, Interval interval) throws NullPointerException {
        if (transactions == null || transactions.isEmpty())
            throw new NullPointerException("Transaction item list cannot be empty!");

        Date end = new Date();
        Date start = getStartDate(interval);
        double expense = 0;
        for (TransactionItem item : transactions) {
            if (item.getDate().getTime() <= end.getTime() && item.getDate().getTime() > start.getTime() && item.getAmount() < 0) {
                expense += item.getAmount();
            }
        }

        return expense * (-1);
    }

    public double getAverageIncome(Map<Date, Double> transactions) throws NullPointerException {
        if (transactions == null || transactions.isEmpty())
            throw new NullPointerException("Transaction item list cannot be empty!");

        double income = 0;
        for (Double aDouble : transactions.values()) {
            income += aDouble;
        }

        return income / transactions.size();
    }

    public double getAverageExpenses(Map<Date, Double> transactions) throws NullPointerException {
        if (transactions == null || transactions.isEmpty())
            throw new NullPointerException("Transaction item list cannot be empty!");

        double expense = 0;
        for (Double aDouble : transactions.values()) {
            expense += aDouble;
        }
        return expense / transactions.size();
    }

    public double getBalance(List<TransactionItem> transactions, Interval interval) {
        if (transactions == null || transactions.isEmpty())
            throw new NullPointerException("Transaction item list cannot be empty!");

        double balance = 0;
        try {
            ArrayList<TransactionItem> list = new ArrayList<>(transactions);
            Collections.sort(list);
            balance = list.get(list.size() - 1).getBalance();
        } catch (NullPointerException e) {

        }
        return balance;
    }

    public TreeMap<Date, Double> groupBalanceByInterval(TreeMap<Date, Double> inputMap, Interval interval) {
        if (inputMap == null || inputMap.isEmpty())
            return null;

        long grouping_period;
        switch (interval) {
            case LAST_WEEK:
            case LAST_MONTH:
                return inputMap;
            case LAST_QUARTER:
                grouping_period = 1000L * 60 * 60 * 24;
                break;
            case LAST_HALF_YEAR:
                grouping_period = 1000L * 60 * 60 * 24;
                break;
            case LAST_YEAR:
                grouping_period = 1000L * 60 * 60 * 24 * 30;
                break;
            case LAST_5_YEARS:
                grouping_period = 1000L * 60 * 60 * 24 * 30;
                break;
            default:
                return inputMap;
        }

        TreeMap<Date, Double> groupedMap = new TreeMap<>();

        long start = inputMap.firstKey().getTime();

        double tmp_value = 0;
        int count = 0;
        Date startDate = new Date(start);


        for (Map.Entry<Date, Double> entry : inputMap.entrySet()) {

            if (entry.getKey().getTime() < start + grouping_period) {
                tmp_value += entry.getValue();
                count++;
            } else {
                if (count != 0)
                    groupedMap.put(startDate, tmp_value / count);

                count = 0;
                tmp_value = 0;

                while (entry.getKey().getTime() >= start + grouping_period)
                    start += grouping_period;

                tmp_value += entry.getValue();
                count++;
                startDate = new Date(start);
            }
        }

        if (count != 0)
            groupedMap.put(startDate, tmp_value / count);

        return groupedMap;

    }

    public TreeMap<Date, Double> groupIncome(TreeMap<Date, Double> inputMap, Period period) {
        if (inputMap == null || inputMap.isEmpty())
            return null;
        int grouping_period = 1;
        int period_length = 1;
        switch (period) {
            case NO:
                return inputMap;
            case DAY:
                grouping_period = Calendar.DATE;
                period_length = 1;
                break;
            case WEEK:
                grouping_period = Calendar.DATE;
                period_length = 7;
                break;
            case MONTH:
                grouping_period = Calendar.MONTH;
                period_length = 1;

                break;
            default:
                return inputMap;
        }


        TreeMap<Date, Double> groupedMap = new TreeMap<>();

        Calendar calendar = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(inputMap.firstKey());


        Iterator<Map.Entry<Date, Double>> it = inputMap.entrySet().iterator();
        Map.Entry<Date, Double> pair = it.next();

        double tmp_value = 0;


        calendar.setTime(inputMap.firstKey());

        while (calendar.getTime().before(inputMap.lastKey())) {
            calendarEnd.add(grouping_period, period_length);

            while (pair.getKey().getTime() < calendarEnd.getTime().getTime()) {
                tmp_value += pair.getValue();
                if (it.hasNext())
                    pair = it.next();
                else break;
            }

            if(tmp_value != 0)
                groupedMap.put(calendarEnd.getTime(), tmp_value);
            tmp_value = 0;


            calendar.add(grouping_period, period_length);
        }

//        TreeMap<Date, Double> groupedMap = new TreeMap<>();
//
//        long start = inputMap.firstKey().getTime();
//
//        double tmp_value = 0;
//        int count = 0;
//        Date startDate = new Date(start);
//
//
//        for (Map.Entry<Date, Double> entry : inputMap.entrySet()) {
//
//            if (entry.getKey().getTime() < start + grouping_period) {
//                tmp_value += entry.getValue();
//                count++;
//            } else {
//                if (count != 0)
//                    groupedMap.put(startDate, tmp_value);
//
//                count = 0;
//                tmp_value = 0;
//
//                while (entry.getKey().getTime() >= start + grouping_period)
//                    start += grouping_period;
//
//                tmp_value += entry.getValue();
//                count++;
//                startDate = new Date(start);
//            }
//        }
//
//        if (count != 0)
//            groupedMap.put(startDate, tmp_value);
        return groupedMap;

    }

    public TreeMap<Date, Double> groupExpenses(TreeMap<Date, Double> inputMap, Period period) {
        if (inputMap == null || inputMap.isEmpty())
            return null;

        int grouping_period = 1;
        int period_length = 1;
        switch (period) {
            case NO:
                return inputMap;
            case DAY:
                grouping_period = Calendar.DATE;
                period_length = 1;
                break;
            case WEEK:
                grouping_period = Calendar.DATE;
                period_length = 7;
                break;
            case MONTH:
                grouping_period = Calendar.MONTH;
                period_length = 1;

                break;
            default:
                return inputMap;
        }

        TreeMap<Date, Double> groupedMap = new TreeMap<>();

        Calendar calendar = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(inputMap.firstKey());


        Iterator<Map.Entry<Date, Double>> it = inputMap.entrySet().iterator();
        Map.Entry<Date, Double> pair = it.next();

        double tmp_value = 0;


        calendar.setTime(inputMap.firstKey());

        while (calendar.getTime().before(inputMap.lastKey())) {
            calendarEnd.add(grouping_period, period_length);

            while (pair.getKey().getTime() < calendarEnd.getTime().getTime()) {
                tmp_value += pair.getValue();
                if (it.hasNext())
                    pair = it.next();
                else break;
            }

            groupedMap.put(calendarEnd.getTime(), tmp_value);
            tmp_value = 0;


            calendar.add(grouping_period, period_length);
        }

//
//        for (Map.Entry<Date, Double> entry : inputMap.entrySet()) {
//
//            if (entry.getKey().getTime() < cal.getTime().getTime()) {
//                tmp_value += entry.getValue();
//                count++;
//
//            } else {
//                if (count != 0)
//                    groupedMap.put(startDate, tmp_value);
//
//                count = 0;
//                tmp_value = 0;
//
//                while (entry.getKey().getTime() >= start + grouping_period)
//                    start += grouping_period;
//
//                tmp_value += entry.getValue();
//                count++;
//                startDate = new Date(start);
//            }
//        }
//
//        if (count != 0)
//            groupedMap.put(startDate, tmp_value);
        return groupedMap;

    }


    public TreeMap<Date, Double> margeMaps(TreeMap<Date, Double> map1, TreeMap<Date, Double> map2) {

        if (map1 == null || map2 == null || map1.isEmpty() || map2.isEmpty())
            return null;

        TreeMap<Date, Double> resultMap = new TreeMap<>();

        Iterator<Map.Entry<Date, Double>> it = map2.entrySet().iterator();
        Map.Entry<Date, Double> pair = it.next();
        final long day = 1000 * 60 * 60 * 24;

        double balance = 0;


        for (Map.Entry<Date, Double> entry : map1.entrySet()) {

            if (entry.getKey().getTime() >= pair.getKey().getTime() - day / 2 &&
                    entry.getKey().getTime() < pair.getKey().getTime() + day / 2) {

                balance = pair.getValue();
                if (it.hasNext())
                    pair = it.next();
            }
            resultMap.put(entry.getKey(), entry.getValue() + balance);
        }

        return resultMap;
    }
}
