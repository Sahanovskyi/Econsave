package com.gw.data.net;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.gw.data.entity.SmsEntity;
import com.gw.data.entity.TransactionItemEntity;
import com.gw.data.entity.mapper.TransactionItemEntitySMSMapper;
import com.gw.data.exception.NumberNotFoundException;
import com.gw.domain.exception.SMSReadException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


public class SMSReader {

    private final Context context;
    private final TransactionItemEntitySMSMapper smsMapper;
    private final String number;


    public SMSReader(Context context, TransactionItemEntitySMSMapper smsMapper, String number){
        if (context == null || smsMapper == null || number == null) {
            throw new IllegalArgumentException("The constructor parameters cannot be null!!!");
        }
        this.context = context;
        this.smsMapper = smsMapper;
        this.number = number;
    }

    public Observable<List<TransactionItemEntity>> transactionItemEntityList(){
        return Observable.create(emiter -> {
            try{
                List<SmsEntity> smsEntityList = getTransactionEntitiesFromSms();
                emiter.onNext(smsMapper.transform(smsEntityList, number));
                emiter.onComplete();
            }
            catch (NumberNotFoundException e){
                emiter.onError(e);
            }
            catch (Exception e){
                emiter.onError(new SMSReadException());
            }

        });

    }

    private List<SmsEntity> getTransactionEntitiesFromSms() throws NumberNotFoundException{
        List<SmsEntity> smses = new ArrayList<>();
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);

        boolean isExist = false;
        // Read the sms data and store it in the list
        if (c.moveToFirst()) {
            for (int i = 0; i < c.getCount(); i++) {
                if (c.getString(c.getColumnIndexOrThrow("address")).toString().equals(number)) {
                    isExist = true;
                    SmsEntity smsEntity = new SmsEntity();
                    smsEntity.setBody(c.getString(c.getColumnIndexOrThrow("body")).toString());
                    smsEntity.setNumber(c.getString(c.getColumnIndexOrThrow("address")).toString());
                    smses.add(smsEntity);
                }
                c.moveToNext();
            }
        }
        c.close();
        if(!isExist)
            throw new NumberNotFoundException();
        return smses;
    }

}
