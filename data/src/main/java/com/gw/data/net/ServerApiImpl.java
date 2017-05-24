package com.gw.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.gw.data.entity.TransactionItemEntity;
import com.gw.data.entity.mapper.TransactionItemEntityXMLMapper;
import com.gw.data.exception.NetworkConnectionException;
import com.gw.domain.exception.BankException;
import com.gw.domain.model.PrivatBank.PrivatBankClient;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.language.bm.Languages;
import org.w3c.dom.Document;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;


/**
 * {@link ServerApi} implementation for communication with the server.
 */
public class ServerApiImpl implements ServerApi {

    private final Context context;
    private final TransactionItemEntityXMLMapper entityXMLMapper;
    private final int PAYMENT_ID = 0;
    private final PrivatBankClient client;

    /**
     * Constructor of the class
     *
     * @param context {@link android.content.Context}.
     * @param entityXMLMapper {@link TransactionItemEntityXMLMapper}.
     */
    public ServerApiImpl(Context context, TransactionItemEntityXMLMapper entityXMLMapper, PrivatBankClient client) {
        if (context == null || entityXMLMapper == null || client == null) {
            throw new IllegalArgumentException("The constructor parameters cannot be null!!!");
        }
        this.context = context.getApplicationContext();
        this.entityXMLMapper = entityXMLMapper;
        this.client = client;
    }

    @Override
    public Observable<List<TransactionItemEntity>> transactionItemEntityList() {
        return Observable.create(emitter -> {
            if (isThereInternetConnection()) {
                try {
                    String responseTransactionEntities = getTransactionEntitiesFromApi();
                    if (responseTransactionEntities != null) {
                        emitter.onNext(entityXMLMapper.transformTransactionEntityCollection(
                                responseTransactionEntities));
                        emitter.onComplete();
                    } else {
                        emitter.onError(new NetworkConnectionException());
                    }
                }catch (BankException e){
                    emitter.onError(e);
                } catch (Exception e) {
                    emitter.onError(new NetworkConnectionException(e.getCause()));
                }
            } else {
                emitter.onError(new NetworkConnectionException());
            }
        });
    }


    private String getTransactionEntitiesFromApi() throws MalformedURLException {
        final Date end = new Date();
        final Date start = new Date(946677600000L); //01.01.2000
        return ApiConnection.createPOST(API_BASE_URL, createRequest(client, start, end)).requestSyncCall();
    }


    private String createRequest(PrivatBankClient client, Date start, Date end) {

        SimpleDateFormat dt = new SimpleDateFormat( "dd.MM.yyyy", Locale.getDefault());
        String dateStart = dt.format(start);
        String dateEnd = dt.format(end);

        String dataTag =
                "<oper>cmt</oper>" +
                        "<wait>0</wait>" +
                        "<test>0</test>" +
                        "<payment id=\"" + PAYMENT_ID + "\">" +
                        "<prop name=\"sd\" value=\"" + dateStart + "\" />" +
                        "<prop name=\"ed\" value=\"" + dateEnd + "\" />" +
                        "<prop name=\"card\" value=\"" + client.getCardNumber() + "\" />" +
                        "</payment>";


        String signature = new String(Hex.encodeHex(DigestUtils.sha1(
                new String(Hex.encodeHex(DigestUtils.md5(dataTag + client.getMerchantPassword()))))));
        String merchantTag =
                "<merchant>" +
                        "<id>" + client.getMerchantId() + "</id>" +
                        "<signature>" + signature + "</signature>" +
                        "</merchant>";
        String request =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                        "<request version=\"1.0\">" +
                        merchantTag +

                        "<data>" + dataTag + "</data>" +
                        "</request>";
        return request;
    }

    /**
     * Checks if the device has any active internet connection.
     *
     * @return true device with internet connection, otherwise false.
     */
    private boolean isThereInternetConnection() {
        boolean isConnected;

        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        return isConnected;
    }
}
