package chosen_new.com.chosen.Util;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class ServiceUtil extends AsyncTask<Void, Void, String> {


    private String url;
    private OnSuccessCallback onSuccessCallback;
    private OnFailureCallback onFailureCallback;
    private Request request;

    public ServiceUtil(String url, OnSuccessCallback successCallback, OnFailureCallback failureCallback){
        this.url = url;
        this.onSuccessCallback = successCallback;
        this.onFailureCallback = failureCallback;
        this.request = GetToServer(url);
    }

    public ServiceUtil(RequestBody requestBody, String url, OnSuccessCallback successCallback,
                       OnFailureCallback failureCallback){
        this.url = url;
        this.onSuccessCallback = successCallback;
        this.onFailureCallback = failureCallback;
        this.request = PostToServer(url, requestBody);
    }

    public Request GetToServer(String url){
        Request.Builder builder = new Request.Builder();
        return builder
                .url(url)
                .addHeader("Accept", "application/json; q=0.5")
                .addHeader("Accept", "application/vnd.github.v3+json")
                .build();
    }

    public Request PostToServer(String url, RequestBody requestBody){
        Request.Builder builder = new Request.Builder();
        return builder
                .url(url)
//                .addHeader("Accept", "application/json; q=0.5")
                .addHeader("Accept", "text/json; charset=utf-8")
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody)
                .build();
    }

    public static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {
            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, null, null);
                client.sslSocketFactory(new Tls12SocketFactoryUtil(sc.getSocketFactory()));

                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                client.connectionSpecs(specs);
            } catch (Exception exc) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
            }
        }
        return client;
    }

    public static OkHttpClient getNewHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .cache(null)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);

        return enableTls12OnPreLollipop(client).build();
    }

    @Override
    protected String doInBackground(Void... voids) {
        OkHttpClient okHttpClient = getNewHttpClient();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                onSuccessCallback.Call(response.body().string());
                return response.body().string();
            } else {
                onFailureCallback.Call("Error " + response.code());
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            onFailureCallback.Call("Time out");
        }
        return "";
    }

    @Override
    protected void onPostExecute(String response) {
        onSuccessCallback.Call(response);
    }

    public interface OnSuccessCallback {
        void Call(String response);
    }

    public interface OnFailureCallback {
        void Call(String message);
    }
}
