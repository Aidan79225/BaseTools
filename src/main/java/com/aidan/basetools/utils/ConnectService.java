package com.aidan.basetools.utils;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Aidan on 2018/1/28.
 */

public class ConnectService {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public interface HttpRequestDelegate {
        void didGetResponse(String url, String response);
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendPatchRequest(String url, HashMap<String, String> headers, JSONObject params, HttpRequestDelegate delegate) {

        OkHttpClient client = getUnsafeOkHttpClient();
        FormBody.Builder b = new FormBody.Builder();
        Iterator<String> keys = params.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                b.add(key, params.getString(key));
            } catch (Exception e) {
            }
        }
        b.add("Content-Type","application/x-www-form-urlencoded");
        RequestBody body = b.build();

        LogHelper.log("JSONObject: " + params.toString());
        Request.Builder builder = new Request.Builder()
                .url(url)
                .patch(body);

        for (String key : headers.keySet()) {
            builder.addHeader(key, headers.get(key));
        }
        LogHelper.log("SEND PATCH REQUEST: " + url);
        Request request = builder.build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogHelper.log("PATCH REQUEST EXCEPTION: " + e.toString());
                if (delegate != null) delegate.didGetResponse(url, null);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                int statusCode = response.code();
                if ((statusCode != 200 && statusCode != 201 && statusCode != 304)) {
                    LogHelper.log("statusCode: " + statusCode);
                    if (delegate != null) delegate.didGetResponse(url, null);
                } else {
                    String responseString = response.body().string();
                    LogHelper.log("Received: " + responseString);
                    if (delegate != null) delegate.didGetResponse(url, responseString);
                }
            }
        });
    }

    public static void sendGetRequest(String url, HashMap<String, String> headers, JSONObject params, HttpRequestDelegate delegate) {
        OkHttpClient client = getUnsafeOkHttpClient();
        String urlWithParams = getUrlWithParam(url, params);
        urlWithParams = urlWithParams.substring(0, urlWithParams.length() - 1);
        LogHelper.log("SEND GET REQUEST: " + urlWithParams);

        Request.Builder builder = new Request.Builder()
                .url(urlWithParams).get();
        for (String key : headers.keySet()) {
            builder.addHeader(key, headers.get(key));
        }
        Request request = builder.build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogHelper.log("GET REQUEST EXCEPTION: " + e.toString());
                if (delegate != null) delegate.didGetResponse(url, null);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                int statusCode = response.code();
                if ((statusCode != 200 && statusCode != 201 && statusCode != 304)) {
                    LogHelper.log("statusCode: " + statusCode);
                    if (delegate != null) delegate.didGetResponse(url, null);
                } else {
                    String responseString = response.body().string();
                    LogHelper.log("Received: " + responseString);
                    if (delegate != null) delegate.didGetResponse(url, responseString);
                }
            }
        });
    }

    public static String getUrlWithParam(String url , JSONObject params){
        StringBuilder urlWithParams = new StringBuilder(url + "?");
        Iterator it = params.keys();

        while (it.hasNext()) {
            try {
                String key = (String) it.next();
                if (params.get(key) instanceof JSONArray) {
                    JSONArray array = (JSONArray) params.get(key);
                    for (int i = 0; i < array.length(); i++) {
                        Object o = array.get(i);
                        urlWithParams.append(key).append("=").append(o.toString()).append("&");
                    }
                } else {
                    String value = params.get(key).toString();
                    urlWithParams.append(key).append("=").append(value).append("&");
                }
                it.remove(); // avoids a ConcurrentModificationException

            } catch (Exception e) {

            }
        }
        return urlWithParams.toString();
    }

    //HttpUrl.parse(url).newBuilder().port(Constants.PORT).build()
    public static void sendPostRequest(String url, HashMap<String, String> headers, JSONObject params, HttpRequestDelegate delegate) {

        OkHttpClient client = getUnsafeOkHttpClient();
        FormBody.Builder b = new FormBody.Builder();
        Iterator<String> keys = params.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                b.add(key, params.getString(key));
            } catch (Exception e) {
            }
        }
        RequestBody body = b.build();

        LogHelper.log("JSONObject: " + params.toString());
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);

        for (String key : headers.keySet()) {
            builder.addHeader(key, headers.get(key));
        }
        LogHelper.log("SEND POST REQUEST: " + url);
        Request request = builder.build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogHelper.log("POST REQUEST EXCEPTION: " + e.toString());
                if (delegate != null) delegate.didGetResponse(url, null);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                int statusCode = response.code();
                if ((statusCode != 200 && statusCode != 201 && statusCode != 304)) {
                    LogHelper.log("statusCode: " + statusCode);
                    if (delegate != null) delegate.didGetResponse(url, null);
                } else {
                    String responseString = response.body().string();
                    LogHelper.log("Received: " + responseString);
                    if (delegate != null) delegate.didGetResponse(url, responseString);
                }
            }
        });
    }
}
