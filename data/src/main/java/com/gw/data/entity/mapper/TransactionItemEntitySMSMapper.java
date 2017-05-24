package com.gw.data.entity.mapper;

import com.gw.data.entity.SmsEntity;
import com.gw.data.entity.TransactionItemEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class TransactionItemEntitySMSMapper {

    @Inject
    public TransactionItemEntitySMSMapper() {
    }

    public List<TransactionItemEntity> transform(List<SmsEntity> smsEntityList, String number)
            throws ParseException {
        ArrayList<TransactionItemEntity> transactionItems;

        switch (number){
            case "Ukrsotsbank":
                transactionItems = parseUrksotsbank(smsEntityList);
                break;
            default:
                transactionItems = new ArrayList<>();
        }
        return transactionItems;

    }

    private ArrayList<TransactionItemEntity> parseUrksotsbank(List<SmsEntity> smsEntityList)
            throws ParseException {

        ArrayList<TransactionItemEntity> transactionItems = new ArrayList<>();

        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        for (SmsEntity smsEntity : smsEntityList) {
            if (smsEntity.getBody().contains("Spysano")) {
                String strDate = smsEntity.getBody().substring(0, 16);
                Date date = dt.parse(strDate);

                int startIndex = smsEntity.getBody().indexOf("Spysano") + 8;
                int endIndex = smsEntity.getBody().indexOf("z") - 5;

                String strAmount = smsEntity.getBody().substring(startIndex, endIndex).replaceAll(" ", "");
                double amount = Double.parseDouble(strAmount) * (-1);

                startIndex = endIndex + 17;
                endIndex = smsEntity.getBody().indexOf("Zalyshok") - 1;

                String descriptio = smsEntity.getBody().substring(startIndex, endIndex);

                startIndex = endIndex + 10;
                endIndex = smsEntity.getBody().indexOf(".", endIndex) + 2;
                String strBalance = smsEntity.getBody().substring(startIndex, endIndex).replaceAll(" ", "");

                double balance = Double.parseDouble(strBalance);
                String currency = "";

                TransactionItemEntity item = new TransactionItemEntity(amount, balance, date, descriptio, currency);
                transactionItems.add(item);


            } else if (smsEntity.getBody().contains("Operatsia popolnenie na sumu")) {
                Date date = dt.parse(smsEntity.getBody().substring(0, 16));

                int startIndex = smsEntity.getBody().indexOf("Operatsia popolnenie na sumu") + 29;
                int endIndex = smsEntity.getBody().indexOf("po ") - 5;

                String strAmount = smsEntity.getBody().substring(startIndex, endIndex).replaceAll(" ", "");
                double amount = Double.parseDouble(strAmount);

                startIndex = endIndex + 17;
                endIndex = smsEntity.getBody().indexOf("Zalyshok") - 1;

                String descriptio = smsEntity.getBody().substring(startIndex, endIndex);


                startIndex = endIndex + 10;
                endIndex = smsEntity.getBody().indexOf(".", endIndex) + 2;
                String strBalance = smsEntity.getBody().substring(startIndex, endIndex).replaceAll(" ", "");

                double balance = Double.parseDouble(strBalance);

                String currency = "";

                TransactionItemEntity item = new TransactionItemEntity(amount, balance, date, descriptio, currency);
                transactionItems.add(item);

            }

        }

        return transactionItems;
    }
}
