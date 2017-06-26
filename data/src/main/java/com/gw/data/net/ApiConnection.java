package com.gw.data.net;


import android.support.annotation.Nullable;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Api Connection class used to retrieve communicate with the server.
 * Implements {@link java.util.concurrent.Callable} so when executed asynchronously can
 * return a value.
 */
class ApiConnection implements Callable<String> {

    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_VALUE_XML = "application/xml";

    private URL url;
    private String response;
    private String request;

    private ApiConnection(String url, String request) throws MalformedURLException {
        this.url = new URL(url);
        this.request = request;
    }

    static ApiConnection createPOST(String url, String request) throws MalformedURLException {
        return new ApiConnection(url, request);
    }

    /**
     * Do a request to an api synchronously.
     * It should not be executed in the main thread of the application.
     *
     * @return A String response
     */
    @Nullable
    String requestSyncCall() {
        connectToApi();
        return response;
    }


    private void connectToApi() {
        OkHttpClient okHttpClient = this.createClient();
        final Request request = new Request.Builder()
                .url(this.url)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE_XML)
                .post(createRequestBody())
                .build();

        try {
            this.response = okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private OkHttpClient createClient() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(10000, TimeUnit.MILLISECONDS);
        okHttpClient.setConnectTimeout(15000, TimeUnit.MILLISECONDS);

        return okHttpClient;
    }

    private static final MediaType XML = MediaType.parse(CONTENT_TYPE_VALUE_XML);
    private RequestBody createRequestBody(){
        return RequestBody.create(XML, request);
    }


    @Override public String call() throws Exception {
        return requestSyncCall();
    }
}
